package homeserver.service.quartz;

import javax.annotation.PostConstruct;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import homeserver.service.IrCommandHistoryService;
import homeserver.service.MqClientService;
import homeserver.service.TelegramBot;

@Component
public class ScheduledJobs {
    
    private static final Logger LOG = LoggerFactory.getLogger(ScheduledJobs.class);
    
    @Autowired
    private MqClientService mqClient = null;
    
    @Autowired
    private IrCommandHistoryService irCmdHistoryService = null;
    
    public JobDetail jobDetail(String name, Class c) {
        return JobBuilder.newJob().ofType(c)
          //.storeDurably()
          .withIdentity(name)
          .build();
    }
    
    public void heartbeat() {
        LOG.info("heartbeat of job");
    }
    
    public void turnOnAc() {
        LOG.info("scheduled ac-on");
        int id = irCmdHistoryService.insertCmdHistory("QUARTZ", "AC", "ac-on");
        String result = mqClient.sendMessage("ircommand", "ac-on");
        irCmdHistoryService.updateCmdHistory("QUARTZ", id, result);
    }
    
    public void turnOffAc() {
        LOG.info("scheduled ac-off");
        int id = irCmdHistoryService.insertCmdHistory("QUARTZ", "AC", "ac-off");
        String result = mqClient.sendMessage("ircommand", "ac-off");
        irCmdHistoryService.updateCmdHistory("QUARTZ", id, result);
    }
    
    public void turnOnJet() {
        LOG.info("scheduled jet-on");
        int id = irCmdHistoryService.insertCmdHistory("QUARTZ", "AC", "jet-on");
        String result = mqClient.sendMessage("ircommand", "jet-on");
        irCmdHistoryService.updateCmdHistory("QUARTZ", id, result);
    }
    
    public void turnOffJet() {
        LOG.info("scheduled jet-off");
        int id = irCmdHistoryService.insertCmdHistory("QUARTZ", "AC", "jet-off");
        String result = mqClient.sendMessage("ircommand", "jet-off");
        irCmdHistoryService.updateCmdHistory("QUARTZ", id, result);
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
}
