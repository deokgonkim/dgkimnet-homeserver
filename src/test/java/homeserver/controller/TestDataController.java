package homeserver.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import homeserver.controller.data.DataController;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:spring/*.xml"
})
public class TestDataController {

    private static final Logger LOG = LoggerFactory.getLogger(TestDataController.class);
    
    @Autowired
    private DataController controller = null;
    
    private static final String AGENT_ID = "home.RPi.DHT11";
    private static final String NAME = "humidity";
    
    @Test
    public void testListRangeWithoutDate() {
        List data = null;
        try {
            data = controller.listRange(AGENT_ID, NAME, null, null);
            assertNotNull(data);
            assertTrue(data.size() > 0);
            LOG.info("data[0] : {}", data.get(0));
            LOG.info("data[-1] : {}", data.get(data.size() - 1));
        } catch(Exception e) {
            LOG.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testListRange() {
        List data = null;
        try {
            String from = "2019-06-05 00:00";
            String to = "2019-06-06 14:00";
            
            LOG.info("From : {}", from);
            LOG.info("To : {}", to);
            
            data = controller.listRange(AGENT_ID, NAME, from, to);
            assertNotNull(data);
            assertTrue(data.size() > 0);
            LOG.info("data[0] : {}", data.get(0));
            LOG.info("data[-1] : {}", data.get(data.size() - 1));
        } catch(Exception e) {
            LOG.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }
}
