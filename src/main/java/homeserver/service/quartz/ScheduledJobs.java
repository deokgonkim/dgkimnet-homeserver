package homeserver.service.quartz;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import homeserver.model.TimedSensorData;
import homeserver.service.IrCommandHistoryService;
import homeserver.service.MonthlyStorageService;
import homeserver.service.MqClientService;
import homeserver.service.TelegramBot;
import homeserver.service.WeeklyStorageService;
import homeserver.service.YearlyStorageService;
import homeserver.util.DateTimeUtil;

@Component
public class ScheduledJobs {
    
    private static final Logger LOG = LoggerFactory.getLogger(ScheduledJobs.class);
    
    private static final String ME = "QUARTZ";
    
    @Autowired
    private MqClientService mqClient = null;
    
    @Autowired
    private WeeklyStorageService weeklyStorage;
    
    @Autowired
    private MonthlyStorageService monthlyStorage;
    
    @Autowired
    private YearlyStorageService yearlyStorage;
    
    @Autowired
    private IrCommandHistoryService irCmdHistoryService = null;
    
    @Autowired
    private SchedulerFactoryBean factory = null;
    
    public JobDetail jobDetail(String name, Class c) {
        return JobBuilder.newJob().ofType(c)
          //.storeDurably()
          .withIdentity(name)
          .build();
    }
    
    @PostConstruct
    public void scheduleDataGardeningJobs() {
        Scheduler scheduler = this.factory.getScheduler();
        List<Map<String, Object>> scheduleList = new LinkedList<>();
        
        boolean gardenWeeklyStorage = false;
        boolean gardenMonthlyStorage = false;
        
        try {
            for (String groupName : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    String jobName = jobKey.getName();
                    String jobGroup = jobKey.getGroup();
                    
                    if (jobName.equals("gardenWeeklyStorage")) {
                        gardenWeeklyStorage = true;
                    } else if (jobName.equals("gardenMonthlyStorage")) {
                        gardenMonthlyStorage = true;
                    }
                }
            }
            
            if (!gardenWeeklyStorage) {
                JobDetail jobDetail = jobDetail("gardenWeeklyStorage", ScheduledJobs.GardenWeeklyStorage.class);
                Trigger trigger = TriggerBuilder.newTrigger().forJob(jobDetail)
                        .withIdentity("gardenWeeklyStorage")
                        .withSchedule(CronScheduleBuilder.cronSchedule("0 0/5 * * * ?"))
                        .build();
                scheduler.scheduleJob(jobDetail, trigger);
                LOG.info("Gardening schedule 'gardenWeeklyStorage' registered");
            }
            
            if (!gardenMonthlyStorage) {
                JobDetail jobDetail = jobDetail("gardenMonthlyStorage", ScheduledJobs.GardenMonthlyStorage.class);
                Trigger trigger = TriggerBuilder.newTrigger().forJob(jobDetail)
                        .withIdentity("gardenMonthlyStorage")
                        .withSchedule(CronScheduleBuilder.cronSchedule("0 0 * * * ?"))
                        .build();
                scheduler.scheduleJob(jobDetail, trigger);
                LOG.info("Gardening schedule 'gardenMonthlyStorage' registered");
            }
            
        } catch (SchedulerException e) {
            LOG.error(e.getMessage(), e);
        }
    }
    
    /**
     * run every five minutes
     * 
     */
    public void scheduleDataGardening5Minutely() {
        Date currentTime = DateTimeUtil.roundTo5Minute(DateTimeUtil.getCurrentDateTime());
        Date fiveMinuteAgo = DateTimeUtil.addMinutes(currentTime, -5);
        LOG.info(String.format("scheduleDataGardening : %s", fiveMinuteAgo));
        
        
        List<TimedSensorData> recent5Minute = weeklyStorage.selectUpdatedWithin(fiveMinuteAgo, currentTime);
        
        // key : agentId+name, value : [ Date(rounded to 5 minute), ... ]
        Map<String, Set<Date>> updatedTimeSet = new HashMap<>();
        
        for (TimedSensorData item : recent5Minute) {
            LOG.info(String.format("Found item : %s", item));
            String key = item.getAgentId() + "," + item.getName();
            Date rounded = DateTimeUtil.roundTo5Minute(item.getDatetime());
            if (updatedTimeSet.containsKey(key)) {
                Set<Date> set = updatedTimeSet.get(key);
                if (!set.contains(rounded)) {
                    set.add(rounded);
                }
            } else {
                Set<Date> newSet = new HashSet<>();
                newSet.add(rounded);
                updatedTimeSet.put(key, newSet);
            }
        }
        
        
        LOG.info("To be updated");
        for (String key : updatedTimeSet.keySet()) {
            LOG.info(String.format("%s : %s", key, updatedTimeSet.get(key)));
            for (Date from : updatedTimeSet.get(key)) {
                Date to = DateTimeUtil.addMinutes(from, 5);
                
                TimedSensorData data = weeklyStorage.selectMinMaxAvg(key.split(",")[0], key.split(",")[1], from, to);
                data.setDatetime(from);
                monthlyStorage.insertOrUpdate(data);
                LOG.info("Got data - {}", data);
            }
        }
        
        
        //yearlyStorage.insertOrUpdate(recent1Hour);
        
    }
    
    /**
     * run every 1 hour
     */
    public void scheduleDataGardeningHourly() {
        Date currentTime = DateTimeUtil.roundTo5Minute(DateTimeUtil.getCurrentDateTime());
        Date oneHourAgo = DateTimeUtil.addMinutes(currentTime, -60);
        LOG.info(String.format("scheduleDataGardening : %s", oneHourAgo));
        
        
        List<TimedSensorData> recent1Hour = monthlyStorage.selectUpdatedWithin(oneHourAgo, currentTime);
        
        // key : agentId+name, value : [ Date(rounded to 1 hour), ... ]
        Map<String, Set<Date>> updatedTimeSet = new HashMap<>();
        
        for (TimedSensorData item : recent1Hour) {
            LOG.info(String.format("Found item : %s", item));
            String key = item.getAgentId() + "," + item.getName();
            Date rounded = DateTimeUtil.roundTo1Hour(item.getDatetime());
            if (updatedTimeSet.containsKey(key)) {
                Set<Date> set = updatedTimeSet.get(key);
                if (!set.contains(rounded)) {
                    set.add(rounded);
                }
            } else {
                Set<Date> newSet = new HashSet<>();
                newSet.add(rounded);
                updatedTimeSet.put(key, newSet);
            }
        }
        
        
        LOG.info("To be updated");
        for (String key : updatedTimeSet.keySet()) {
            LOG.info(String.format("%s : %s", key, updatedTimeSet.get(key)));
            for (Date from : updatedTimeSet.get(key)) {
                Date to = DateTimeUtil.addMinutes(from, 60);
                
                TimedSensorData data = weeklyStorage.selectMinMaxAvg(key.split(",")[0], key.split(",")[1], from, to);
                data.setDatetime(from);
                yearlyStorage.insertOrUpdate(data);
                LOG.info("Got data - {}", data);
            }
        }
    }
    
    
    public void heartbeat() {
        LOG.info("heartbeat of job");
    }
    
    public void turnOnAc() {
        LOG.info("scheduled ac-on");
        int id = irCmdHistoryService.insertCmdHistory(ME, "AC", "AcOn");
        String result = mqClient.sendAcOn();
        irCmdHistoryService.updateCmdHistory(ME, id, result);
    }
    
    public void turnOffAc() {
        LOG.info("scheduled ac-off");
        int id = irCmdHistoryService.insertCmdHistory(ME, "AC", "AcOff");
        String result = mqClient.sendAcOff();
        irCmdHistoryService.updateCmdHistory(ME, id, result);
    }
    
    public void turnOnJet() {
        LOG.info("scheduled jet-on");
        int id = irCmdHistoryService.insertCmdHistory(ME, "AC", "JetOn");
        String result = mqClient.sendJetOn();
        irCmdHistoryService.updateCmdHistory(ME, id, result);
    }
    
    public void turnOffJet() {
        LOG.info("scheduled jet-off");
        int id = irCmdHistoryService.insertCmdHistory(ME, "AC", "JetOff");
        String result = mqClient.sendJetOff();
        irCmdHistoryService.updateCmdHistory(ME, id, result);
    }
    
    public void temp18() {
        LOG.info("scheduled temp-18");
        int id = irCmdHistoryService.insertCmdHistory(ME, "AC", "Temp18");
        String result = mqClient.sendTemp18();
        irCmdHistoryService.updateCmdHistory(ME, id, result);
    }
    
    public void temp24() {
        LOG.info("scheduled temp-24");
        int id = irCmdHistoryService.insertCmdHistory(ME, "AC", "Temp24");
        String result = mqClient.sendTemp24();
        irCmdHistoryService.updateCmdHistory(ME, id, result);
    }
    
    public void temp26() {
        LOG.info("scheduled temp-26");
        int id = irCmdHistoryService.insertCmdHistory(ME, "AC", "Temp26");
        String result = mqClient.sendTemp26();
        irCmdHistoryService.updateCmdHistory(ME, id, result);
    }
    
    public void temp28() {
        LOG.info("scheduled temp-28");
        int id = irCmdHistoryService.insertCmdHistory(ME, "AC", "Temp28");
        String result = mqClient.sendTemp28();
        irCmdHistoryService.updateCmdHistory(ME, id, result);
    }
    
    public static class AcOn implements Job {
        
        @Autowired 
        private ScheduledJobs service;
        
        @Autowired
        private TelegramBot telegramBot;
        
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
            service.turnOnAc();
            telegramBot.sendMessage("Turned on AC");
        }
    }
    
    public static class AcOff implements Job {
        @Autowired 
        private ScheduledJobs service;
        
        @Autowired
        private TelegramBot telegramBot;
        
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
            service.turnOffAc();
            telegramBot.sendMessage("Turned off AC");
        }
    }
    
    public static class JetOn implements Job {
        @Autowired 
        private ScheduledJobs service;
        
        @Autowired
        private TelegramBot telegramBot;
        
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
            service.turnOnJet();
            telegramBot.sendMessage("Turned on AC Jet-mode");
        }
    }
    
    public static class JetOff implements Job {
        @Autowired 
        private ScheduledJobs service;
        
        @Autowired
        private TelegramBot telegramBot;
        
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
            service.turnOffJet();
            telegramBot.sendMessage("Turned off AC Jet-mode");
        }
    }
    
    public static class Temp18 implements Job {
        @Autowired 
        private ScheduledJobs service;
        
        @Autowired
        private TelegramBot telegramBot;
        
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
            service.temp18();
            telegramBot.sendMessage("Setted temperature to 18");
        }
    }
    
    public static class Temp24 implements Job {
        @Autowired 
        private ScheduledJobs service;
        
        @Autowired
        private TelegramBot telegramBot;
        
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
            service.temp24();
            telegramBot.sendMessage("Setted temperature to 24");
        }
    }
    
    public static class Temp26 implements Job {
        @Autowired 
        private ScheduledJobs service;
        
        @Autowired
        private TelegramBot telegramBot;
        
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
            service.temp26();
            telegramBot.sendMessage("Setted temperature to 26");
        }
    }
    
    public static class Temp28 implements Job {
        @Autowired 
        private ScheduledJobs service;
        
        @Autowired
        private TelegramBot telegramBot;
        
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
            service.temp28();
            telegramBot.sendMessage("Setted temperature to 28");
        }
    }
    
    public static class GardenWeeklyStorage implements Job {
        
        @Autowired 
        private ScheduledJobs service;
        
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
            service.scheduleDataGardening5Minutely();
        }
    }
    
    public static class GardenMonthlyStorage implements Job {
        
        @Autowired 
        private ScheduledJobs service;
        
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
            service.scheduleDataGardeningHourly();
        }
    }
}
