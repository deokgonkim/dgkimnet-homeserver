package test;

import static org.junit.Assert.*;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import homeserver.util.DateTimeUtil;

public class TestJava {
    
    private static final Logger LOG = LoggerFactory.getLogger(TestJava.class);

    @Test
    public void testCalendar() {
        Date currentTime = DateTimeUtil.getCurrentDateTime();
        assertNotNull(currentTime);
        LOG.info(MessageFormat.format("Current date : {0}", currentTime));
        
        Date roundedTime = DateTimeUtil.roundTo5Minute(currentTime);
        assertNotNull(roundedTime);
        LOG.info(MessageFormat.format("Rounded date : {0}", roundedTime));
        assertEquals(0, roundedTime.getSeconds());
        
        Date fiveMinuteLater = DateTimeUtil.addMinutes(roundedTime, 5);
        assertNotNull(fiveMinuteLater);
        LOG.info(MessageFormat.format("5Minutes later : {0}", fiveMinuteLater));
        assertEquals(roundedTime.getMinutes() + 5, fiveMinuteLater.getMinutes());
        
        //LOG.info(MessageFormat.format("Current minute : {0} - cutoff - {1}", minute, ( minute / 5 ) * 5));
    }

}
