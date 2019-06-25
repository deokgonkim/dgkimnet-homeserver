package homeserver.service.quartz;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import homeserver.service.IrCommandHistoryService;
import homeserver.service.MonthlyStorageService;
import homeserver.service.MqClientService;
import homeserver.service.WeeklyStorageService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class TestScheduledJobs {
    
    @Autowired
    private ScheduledJobs scheduledJobs = null;

    @Configuration
    @PropertySource("classpath:application.properties")
    @ImportResource({"classpath:spring/context-datasource.xml", "classpath:spring/context-mapper.xml"})
    static class Config {
        @Bean
        public ScheduledJobs scheduledJobs() {
            return new ScheduledJobs();
        }
        
        @Bean
        public WeeklyStorageService service() {
            return new WeeklyStorageService();
        }
        
        @Bean
        public MonthlyStorageService monthlyStorage() {
            return new MonthlyStorageService();
        }
        
        @Bean
        public MqClientService mqService() {
            return null;
        }
        
        @Bean
        public IrCommandHistoryService irCommandHistoryService() {
            return null;
        }
        
        @Bean
        public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
            return new PropertySourcesPlaceholderConfigurer();
        }
    }
    
    @Test
    public void test() {
        scheduledJobs.scheduleDataGardening();
    }

}
