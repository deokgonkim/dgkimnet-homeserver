package homeserver.service;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import homeserver.controller.HelloController;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:spring/context-common.xml",
        "classpath:spring/context-datasource.xml",
        "classpath:spring/context-mapper.xml"
})
public class TestWeeklyStorageService {
    private static final Logger LOG = LoggerFactory.getLogger(TestWeeklyStorageService.class);

    @Autowired
    private WeeklyStorageService service = null;
    
    private static final String AGENT_ID = "home.RPi.DHT11";
    private static final String NAME = "temperature";
    
    @Test
    public void testSelectAgents() {
        List<String> agents = null;
        try {
            agents = service.selectAgents();
            assertNotNull(agents);
            assertTrue(agents.size() > 0);
            LOG.info("agents : {}", agents.toArray());
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testSelectRecentListFor() {
        List data = null;
        try {
            data = service.selectRecentListFor(AGENT_ID, NAME);
            LOG.info("data[0] : {}", data.get(0));
        } catch(Exception e) {
            LOG.error(e.getMessage(), e);
            fail(e.getMessage());
        }
        assertNotNull(data);
        assertTrue(data.size() > 0);
        LOG.debug("data : {}", data);
    }
    
    @Test
    public void testSelectListFor() {
        List data = null;
        try {
            Date from = null;
            Date to = null;
            
            long now = System.currentTimeMillis();
            // one day = 24 hours * 60 minutes * 60 seconds * 1000 miliseconds
            long duration = 24 * 60 * 60 * 1000;
            
            to = new Date(now);
            from = new Date(now - duration);
            
            LOG.info("From : {}", from);
            LOG.info("To : {}", to);
            
            data = service.selectListForTimeRange(AGENT_ID, NAME, from, to);
            LOG.info("data[0] : {}", data.get(0));
            LOG.info("data[-1] : {}", data.get(data.size() - 1));
        } catch(Exception e) {
            LOG.error(e.getMessage(), e);
            fail(e.getMessage());
        }
        assertNotNull(data);
        assertTrue(data.size() > 0);
        LOG.debug("data : {}", data);
    }

}
