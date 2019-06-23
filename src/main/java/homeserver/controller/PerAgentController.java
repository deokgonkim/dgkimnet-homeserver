package homeserver.controller;

import homeserver.service.WeeklyStorageService;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/agent")
@PreAuthorize("hasRole('ROLE_USERS')")
public class PerAgentController {
    
    private static final Logger LOG = LoggerFactory.getLogger(PerAgentController.class);
    
    @Autowired
    private WeeklyStorageService weeklyStorageService = null;
    
    @RequestMapping("/{agentId:.+}")
    public String perAgent(@PathVariable("agentId") String agentId,
            @RequestParam(value="from", required=false) String from,
            @RequestParam(value="to", required=false) String to,
            ModelMap modelMap) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        
        long now = System.currentTimeMillis();
        long yesterday = now - ( 24 * 60 * 60 * 1000 );
        
        Date fromDate = new Date(yesterday);
        Date toDate = new Date(now);
        try {
            if (from != null) {
                fromDate = sdf.parse(from);
            }
            if (to != null) {
                toDate = sdf.parse(to);
            }
        } catch (ParseException e) {
            LOG.info(e.getMessage(), e);
        }
        modelMap.addAttribute("agents", this.getAgentList());
        modelMap.addAttribute("from", sdf.format(fromDate));
        modelMap.addAttribute("to", sdf.format(toDate));
        return "agent";
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
