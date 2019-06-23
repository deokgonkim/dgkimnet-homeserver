package homeserver.service;

import static org.junit.Assert.*;

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
public class MqClientServiceIT {
    
    private static final Logger LOG = LoggerFactory.getLogger(MqClientServiceIT.class);

    @Autowired
    private MqClientService service = null;
    
    @Test
    public void testSendMessage() {
        service.sendMessage("ircommand", "acon");
    }

}
