package example.controller;

import static org.junit.Assert.*;
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
        "classpath:spring/*.xml"
})
public class HelloControllerIT {

    private static final Logger LOG = LoggerFactory.getLogger(HelloControllerIT.class);

    @Autowired
    private HelloController controller = null;

    @Test
    public void test() {
        String body = controller.hello();
        LOG.debug("test : " + body);
        assertNotNull(body);
    }

}
