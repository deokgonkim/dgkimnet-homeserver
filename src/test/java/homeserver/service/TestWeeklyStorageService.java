package homeserver.service;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import homeserver.model.TimedSensorData;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class TestWeeklyStorageService {
    private static final Logger LOG = LoggerFactory.getLogger(TestWeeklyStorageService.class);
    
    private static final String TEST_AGENT_ID = "home.RPi.DHT11";
    private static final String TEST_NAME = "temperature";
    
    @Autowired
    private WeeklyStorageService service = null;
    
    private static final String AGENT_ID = "home.RPi.DHT11";
    private static final String NAME = "temperature";
    
    @Configuration
    @PropertySource("classpath:application.properties")
    @ImportResource({"classpath:spring/context-datasource.xml", "classpath:spring/context-mapper.xml"})
    static class Config {
        @Bean
        public WeeklyStorageService service() {
            return new WeeklyStorageService();
        }
        @Bean
        public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
            return new PropertySourcesPlaceholderConfigurer();
        }
    }
    
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

    @Test
    public void testSelectUpdatedWithin() {
        long currentTimeMillis = System.currentTimeMillis();
        Calendar calFrom = null;
        Calendar calTo = null;
        Date from = null;
        Date to = null;
        calTo = Calendar.getInstance();
        calFrom = (Calendar)calTo.clone();
        calFrom.add(Calendar.MINUTE, -5);
        
        from = calFrom.getTime();
        to = calTo.getTime();
        LOG.info("testSelectUpdatedWithin");
        LOG.info(MessageFormat.format("From - {0} To - {1}", from, to));
        
        List<TimedSensorData> items = null;
        
        items = service.selectUpdatedWithin(from, to);
        assertNotNull(items);
        
        for (TimedSensorData item : items) {
            LOG.info(MessageFormat.format("data - {0}", item));
        }
    }
    
//    @Test
//    public void testSelectMinMaxAvg() {
//        List<TimedSensorData> items = null;
//        Calendar calFrom = null;
//        Calendar calTo = null;
//        Date from = null;
//        Date to = null;
//        calTo = Calendar.getInstance();
//        calFrom = (Calendar)calTo.clone();
//        calFrom.add(Calendar.MINUTE, -5);
//        
//        from = calFrom.getTime();
//        to = calTo.getTime();
//        
//        items = service.selectMinMaxAvg(TEST_AGENT_ID, TEST_NAME, from, to);
//        assertNotNull(items);
//        assertTrue(items.size() > 0);
//        
//        for (TimedSensorData item : items) {
//            LOG.info(MessageFormat.format("data - {0}", item));
//        }
//    }
}
