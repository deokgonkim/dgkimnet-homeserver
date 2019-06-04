package homeserver.controller.data;

import homeserver.service.WeeklyStorageService;

import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
        List<String> strings = new LinkedList<>();
        try {
            List list = service.selectRecentListFor(agentId, name);
            return list;
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
            return Collections.EMPTY_LIST;
        }
    }
}
