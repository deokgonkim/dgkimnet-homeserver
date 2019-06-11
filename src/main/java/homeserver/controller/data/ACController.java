package homeserver.controller.data;

import java.security.Principal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import homeserver.service.IrCommandHistoryService;
import homeserver.service.MqClientService;

@Controller
@RequestMapping("/ac")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ACController {

    private static final Logger LOG = LoggerFactory.getLogger(ACController.class);
    
    @Autowired
    private MqClientService service = null;
    
    @Autowired
    private IrCommandHistoryService irCmdHistoryService = null;

    @RequestMapping("/on")
    public @ResponseBody String acOn(@AuthenticationPrincipal String principal) {
        int id = irCmdHistoryService.insertCmdHistory(principal, "AC", "ac-on");
        String result = service.sendMessage("ircommand", "ac-on");
        irCmdHistoryService.updateCmdHistory(principal, id, result);
        return result;
    }
    
    @RequestMapping("/off")
    public @ResponseBody String acOff(@AuthenticationPrincipal Principal principal) {
        int id = irCmdHistoryService.insertCmdHistory(principal.getName(), "AC", "ac-off");
        String result = service.sendMessage("ircommand", "ac-off");
        irCmdHistoryService.updateCmdHistory(principal.getName(), id, result);
        return result;
    }
    
    @RequestMapping("/jet-on")
    public @ResponseBody String jetOn(@AuthenticationPrincipal String principal) {
        int id = irCmdHistoryService.insertCmdHistory(principal, "AC", "jet-on");
        String result = service.sendMessage("ircommand", "jet-on");
        irCmdHistoryService.updateCmdHistory(principal, id, result);
        return result;
    }
    
    @RequestMapping("/jet-off")
    public @ResponseBody String jetOff(@AuthenticationPrincipal String principal) {
        int id = irCmdHistoryService.insertCmdHistory(principal, "AC", "jet-off");
        String result = service.sendMessage("ircommand", "jet-off");
        irCmdHistoryService.updateCmdHistory(principal, id, result);
        return result;
    }
    
    @RequestMapping("/temp-26")
    public @ResponseBody String temp26(@AuthenticationPrincipal String principal) {
        int id = irCmdHistoryService.insertCmdHistory(principal, "AC", "temp-26");
        String result = service.sendMessage("ircommand", "temp-26");
        irCmdHistoryService.updateCmdHistory(principal, id, result);
        return result;
    }
    
    @RequestMapping("/history")
    public @ResponseBody Map history(ModelMap modelMap) {
        Map map = new HashMap();
        map.put("data", irCmdHistoryService.selectRecent());
        map.put("serverDateTime", Calendar.getInstance().getTime());
        return map;
    }
}
