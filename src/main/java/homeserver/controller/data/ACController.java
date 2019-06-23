package homeserver.controller.data;

import java.security.Principal;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import homeserver.service.IrCommandHistoryService;
import homeserver.service.MqClientService;
import homeserver.service.quartz.ScheduledJobs;

@Controller
@RequestMapping("/ac")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ACController {

    private static final Logger LOG = LoggerFactory.getLogger(ACController.class);
    
    @Autowired
    private MqClientService service = null;
    
    @Autowired
    private IrCommandHistoryService irCmdHistoryService = null;
    
    @Autowired
    private SchedulerFactoryBean factory = null;
    
    @Autowired
    private ScheduledJobs scheduleJobHolder = null;

    @RequestMapping("/on")
    public @ResponseBody String acOn(@AuthenticationPrincipal String principal) {
        int id = irCmdHistoryService.insertCmdHistory(principal, "AC", "AcOn");
        String result = service.sendAcOn();
        irCmdHistoryService.updateCmdHistory(principal, id, result);
        return result;
    }
    
    @RequestMapping("/off")
    public @ResponseBody String acOff(@AuthenticationPrincipal Principal principal) {
        int id = irCmdHistoryService.insertCmdHistory(principal.getName(), "AC", "AcOff");
        String result = service.sendAcOff();
        irCmdHistoryService.updateCmdHistory(principal.getName(), id, result);
        return result;
    }
    
    @RequestMapping("/jet-on")
    public @ResponseBody String jetOn(@AuthenticationPrincipal String principal) {
        int id = irCmdHistoryService.insertCmdHistory(principal, "AC", "JetOn");
        String result = service.sendJetOn();
        irCmdHistoryService.updateCmdHistory(principal, id, result);
        return result;
    }
    
    @RequestMapping("/jet-off")
    public @ResponseBody String jetOff(@AuthenticationPrincipal String principal) {
        int id = irCmdHistoryService.insertCmdHistory(principal, "AC", "JetOff");
        String result = service.sendJetOff();
        irCmdHistoryService.updateCmdHistory(principal, id, result);
        return result;
    }
    
    @RequestMapping("/temp-18")
    public @ResponseBody String temp18(@AuthenticationPrincipal String principal) {
        int id = irCmdHistoryService.insertCmdHistory(principal, "AC", "Temp18");
        String result = service.sendTemp18();
        irCmdHistoryService.updateCmdHistory(principal, id, result);
        return result;
    }
    
    @RequestMapping("/temp-26")
    public @ResponseBody String temp26(@AuthenticationPrincipal String principal) {
        int id = irCmdHistoryService.insertCmdHistory(principal, "AC", "Temp26");
        String result = service.sendTemp26();
        irCmdHistoryService.updateCmdHistory(principal, id, result);
        return result;
    }
    
    @RequestMapping("/history")
    public @ResponseBody Map history(ModelMap modelMap) {
        Map<String, Object> map = new HashMap<>();
        map.put("data", irCmdHistoryService.selectRecent());
        map.put("serverDateTime", Calendar.getInstance().getTime());
        return map;
    }
    
    @RequestMapping("/schedule")
    public @ResponseBody String schedule(@RequestParam("cmd") String cmd,
            @RequestParam("hhmm") String hhmm,
            ModelMap modelMap) {
        Scheduler scheduler = this.factory.getScheduler();
        
        
        JobDetail jobDetail = null;
        if ("ac-on".equals(cmd)) {
            cmd += hhmm;
            jobDetail = scheduleJobHolder.jobDetail(cmd, ScheduledJobs.AcOn.class);
        } else if ("ac-off".equals(cmd)) {
            cmd += hhmm;
            jobDetail = scheduleJobHolder.jobDetail(cmd, ScheduledJobs.AcOff.class);
        } else if ("jet-on".equals(cmd)) {
            cmd += hhmm;
            jobDetail = scheduleJobHolder.jobDetail(cmd, ScheduledJobs.JetOn.class);
        } else if ("jet-off".equals(cmd)) {
            cmd += hhmm;
            jobDetail = scheduleJobHolder.jobDetail(cmd, ScheduledJobs.JetOff.class);
        } else if ("temp-18".equals(cmd)) {
            cmd += hhmm;
            jobDetail = scheduleJobHolder.jobDetail(cmd, ScheduledJobs.Temp18.class);
        } else if ("temp-26".equals(cmd)) {
            cmd += hhmm;
            jobDetail = scheduleJobHolder.jobDetail(cmd, ScheduledJobs.Temp26.class);
        } else {
            return "Error";
        }
        
        
        try {
            scheduler.scheduleJob(jobDetail, this.trigger(jobDetail, hhmm.substring(0, 2), hhmm.substring(2)));
        } catch (SchedulerException e) {
            LOG.error(e.getMessage(), e);
            return "Exception";
        }
        return "Ok";
    }
    
    @RequestMapping("/unschedule")
    public @ResponseBody String unschedule(@RequestParam("jobGroup") String jobGroup,
            @RequestParam("jobName") String jobName,
            @RequestParam("triggerGroup") String triggerGroupName,
            @RequestParam("triggerName") String triggerName,
            ModelMap modelMap) {
        Scheduler scheduler = this.factory.getScheduler();
        TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroupName);
        JobKey jobKey = new JobKey(jobName, jobGroup);
        try {
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            LOG.warn(e.getMessage(), e);
            return "Error";
        }
        return "Ok";
    }
    
    @RequestMapping("/list_schedule")
    public @ResponseBody Map<String, Object> listSchedule(ModelMap modelMap) {
        Scheduler scheduler = this.factory.getScheduler();
        List<Map<String, Object>> scheduleList = new LinkedList<>();
        
        try {
            for (String groupName : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    String jobName = jobKey.getName();
                    String jobGroup = jobKey.getGroup();
                    
                    // get job's trigger
                    List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
                    
                    String triggerName = "--";
                    String triggerGroup = "--";
                    Date nextFireTime = new Date(0);
                    
                    for (Trigger trigger : triggers) {
                        triggerName = trigger.getKey().getName();
                        triggerGroup = trigger.getKey().getGroup();
                        nextFireTime = trigger.getNextFireTime();
                    }
                    
                    Map<String, Object> job = new HashMap<>();
                    
                    job.put("jobName", jobName);
                    job.put("jobGroup", jobGroup);
                    job.put("nextFireTime", nextFireTime);
                    job.put("triggerGroup", triggerGroup);
                    job.put("triggerName", triggerName);
                    scheduleList.add(job);
                }
            }
        } catch (SchedulerException e) {
            LOG.error(e.getMessage(), e);
            Map<String, Object> map = new HashMap<>();
            map.put("data", Collections.emptyList());
            map.put("serverDateTime", Calendar.getInstance().getTime());
            return map;
        }
        
        
        Map<String, Object> map = new HashMap<>();
        map.put("data", scheduleList);
        map.put("serverDateTime", Calendar.getInstance().getTime());
        return map;
    }
    
    public Trigger trigger(JobDetail job, String hh, String mm) {
        return TriggerBuilder.newTrigger().forJob(job)
          .withIdentity(String.format("Trigger_%s%s", hh, mm))
          .withSchedule(CronScheduleBuilder.cronSchedule(String.format("00 %s %s * * ?", mm, hh)))
          .build();
    }
}
