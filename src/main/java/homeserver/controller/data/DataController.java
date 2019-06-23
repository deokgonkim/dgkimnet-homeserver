package homeserver.controller.data;

import homeserver.service.WeeklyStorageService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/data")
//@PreAuthorize("isAuthenticated()")
public class DataController {
    
    private static final Logger LOG = LoggerFactory.getLogger(DataController.class);
    
    @Autowired
    private WeeklyStorageService service = null;

    @RequestMapping("/{agentId}/{name}/recent")
    public @ResponseBody List listRecent(@PathVariable("agentId") String agentId, @PathVariable("name") String name) {
        return service.selectRecentListFor(agentId, name);
    }
    
    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/{agentId}/{name}")
    public @ResponseBody List listRange(@PathVariable("agentId") String agentId,
            @PathVariable("name") String name,
            @RequestParam(value="from", required=false) String from,
            @RequestParam(value="to", required=false) String to) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        
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
        return service.selectListForTimeRange(agentId, name, fromDate, toDate);
    }
}
