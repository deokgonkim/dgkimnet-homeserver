package homeserver.service;

import homeserver.model.Humidity;
import homeserver.model.Temperature;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MqClientService {
    
    private static final Logger LOG = LoggerFactory.getLogger(MqClientService.class);
    
    public Temperature currentTemperature;
    public Humidity currentHumidity;
    
    @Autowired
    private WeeklyStorageService weeklyStorage;
    
    /*@Autowired
    public MqClientService(ConnectionFactory cf) {
        RabbitAdmin admin = new RabbitAdmin(cf);
        Queue queue = new Queue("myQueue");
        admin.declareQueue(queue);
 
        //Exchange에 바인딩
        FanoutExchange exchange = new FanoutExchange("sensor");
        admin.declareBinding(BindingBuilder.bind(queue).to(exchange));
                
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(cf);
        
        //메시지 리스닝
        MessageListenerAdapter adapter = new MessageListenerAdapter(this);
        container.setMessageListener(adapter);
        container.setQueueNames("myQueue");
        container.start();
    } */

    public void handleMessage(byte[] body) throws IOException {
        String[] message = new String(body, "UTF-8").split(",");
        String agentId = message[0];
        String name = message[1];
        String type = message[2];
        String value = message[3];
        
        LOG.info("Received sensor data : {}", message);
        
        try {
            weeklyStorage.insertOrUpdate(agentId, name, type, value);
        } catch (SQLException e) {
            LOG.warn(e.getMessage(), e);
        }
        
        if (name.equals("temperature")) {
            currentTemperature = new Temperature("RPi.DHT11.Temperature", new Date(), Double.valueOf(value));
        } else if (name.equals("humidity")) {
            currentHumidity = new Humidity("RPi.DHT11.Humidity", new Date(), Double.valueOf(value));
        }
    }
}
