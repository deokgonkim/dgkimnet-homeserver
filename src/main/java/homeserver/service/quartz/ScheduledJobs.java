package homeserver.service.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import homeserver.service.IrCommandHistoryService;
import homeserver.service.MqClientService;

@Component
public class ScheduledJobs {
    
    private static final Logger LOG = LoggerFactory.getLogger(ScheduledJobs.class);
    
    @Autowired
    private MqClientService mqClient = null;
    
    @Autowired
    private IrCommandHistoryService irCmdHistoryService = null;
    
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
        int id = irCmdHistoryService.insertCmdHistory("QUARTZ", "AC", "ac-on");
        String result = mqClient.sendMessage("ircommand", "jet-on");
        irCmdHistoryService.updateCmdHistory("QUARTZ", id, result);
    }
}
