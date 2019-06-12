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

import homeserver.service.IrCommandHistoryService;
import homeserver.service.MqClientService;

@Component
public class ScheduledJobs implements Job {
    
    private static final Logger LOG = LoggerFactory.getLogger(ScheduledJobs.class);
    
    @Autowired
    private MqClientService mqClient = null;
    
    @Autowired
    private IrCommandHistoryService irCmdHistoryService = null;
    
    @Autowired
    private SchedulerFactoryBean factory = null;
    
    @PostConstruct
    public void scheduleJobs() {
        Scheduler scheduler = factory.getScheduler();
        try {
            scheduler.scheduleJob(this.jobDetail(), this.trigger(this.jobDetail()));
            
            scheduler.start();
            
        } catch (SchedulerException e) {
            LOG.warn(e.getMessage(), e);
        }
         
    }
    
    public JobDetail jobDetail() {
        return JobBuilder.newJob().ofType(ScheduledJobs.class)
          .storeDurably()
          .withIdentity("Heartbeat Job")  
          .withDescription("Invoke Sample Job service...")
          .build();
    }
    
    public Trigger trigger(JobDetail job) {
        return TriggerBuilder.newTrigger().forJob(job)
          .withIdentity("Qrtz_Trigger")
          .withDescription("Sample trigger")
          .withSchedule(SimpleScheduleBuilder.simpleSchedule().repeatForever().withIntervalInSeconds(10))
          .build();
    }
    
    public void execute(JobExecutionContext context) throws JobExecutionException {
        this.heartbeat();
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
    
    public void turnJetOn() {
        LOG.info("scheduled ac-on");
        int id = irCmdHistoryService.insertCmdHistory("QUARTZ", "AC", "jet-on");
        String result = mqClient.sendMessage("ircommand", "jet-on");
        irCmdHistoryService.updateCmdHistory("QUARTZ", id, result);
    }
}
