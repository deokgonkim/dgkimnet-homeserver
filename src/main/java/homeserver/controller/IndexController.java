package homeserver.controller;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import homeserver.service.MqClientService;
import homeserver.service.WeeklyStorageService;

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
    
    @Autowired
    private WeeklyStorageService weeklyStorageService = null;
    
    @RequestMapping("")
    public String index(ModelMap modelMap) {
        modelMap.addAttribute("agents", this.getAgentList());
        modelMap.addAttribute("current", service.getCurrentValues());
        return "home";
    }
    
    public List<String> getAgentList() {
        List<String> agents = null;
        try {
            agents = weeklyStorageService.selectAgents();
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
            agents = Collections.emptyList();
        }
        return agents;
    }
}
