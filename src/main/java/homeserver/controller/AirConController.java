package homeserver.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/aircon")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AirConController {
    
    @RequestMapping("")
    public String list(ModelMap modelMap) {
        return "aircon/list";
    }
    
    @RequestMapping("/schedule")
    public String schedule(ModelMap modelMap) {
        return "aircon/schedule";
    }
}
