package homeserver.controller.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import homeserver.service.MqClientService;

@Controller
@RequestMapping("/ac")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ACController {

    private static final Logger LOG = LoggerFactory.getLogger(ACController.class);
    
    @Autowired
    private MqClientService service = null;

    @RequestMapping("/on")
    public @ResponseBody String acOn() {
        return service.sendMessage("ircommand", "ac-on");
    }
    
    @RequestMapping("/off")
    public @ResponseBody String acOff() {
        return service.sendMessage("ircommand", "ac-off");
    }
    
    @RequestMapping("/temp-26")
    public @ResponseBody String temp26() {
        return service.sendMessage("ircommand", "temp-26");
    }
}
