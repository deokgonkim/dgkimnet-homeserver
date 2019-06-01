package homeserver.controller;

import homeserver.service.MqClientService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController {

    private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);
    
    @Autowired
    private MqClientService service = null;
    
    @RequestMapping("")
    public String index(ModelMap modelMap) {
        modelMap.addAttribute("temperature", service.currentTemperature);
        modelMap.addAttribute("humidity", service.currentHumidity);
        return "home";
    }
}
