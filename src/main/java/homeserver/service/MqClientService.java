package homeserver.service;

import homeserver.model.TimedSensorData;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MqClientService {
    
    private static final Logger LOG = LoggerFactory.getLogger(MqClientService.class);
    
    private Map<String, Object> currentValues = new HashMap<>();
    
    @Value("${mq.ircommand}")
    private String queueIrCommand = null;
    
    @Value("${mq.ircommand.acon}")
    private String irCommandAcOn = null;
    
    @Value("${mq.ircommand.acoff}")
    private String irCommandAcOff = null;
    
    @Value("${mq.ircommand.jeton}")
    private String irCommandJetOn = null;
    
    @Value("${mq.ircommand.jetoff}")
    private String irCommandJetOff = null;
    
    @Value("${mq.ircommand.temp18}")
    private String irCommandTemp18 = null;
    
    @Value("${mq.ircommand.temp26}")
    private String irCommandTemp26 = null;
    
    
    @Autowired
    private WeeklyStorageService weeklyStorage;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    public Map<String, Object> getCurrentValues() {
        return currentValues;
    }
    
    public String sendAcOn() {
        return sendMessage(queueIrCommand, irCommandAcOn);
    }
    
    public String sendAcOff() {
        return sendMessage(queueIrCommand, irCommandAcOff);
    }
    
    public String sendJetOn() {
        return sendMessage(queueIrCommand, irCommandJetOn);
    }
    
    public String sendJetOff() {
        return sendMessage(queueIrCommand, irCommandJetOff);
    }
    
    public String sendTemp18() {
        return sendMessage(queueIrCommand, irCommandTemp18);
    }
    
    public String sendTemp26() {
        return sendMessage(queueIrCommand, irCommandTemp26);
    }
    
    protected String sendMessage(String queue, String message) {
        Object obj = rabbitTemplate.convertSendAndReceive(queue, message);
        String response = "";
        try {
            if (obj != null) {
                response = new String((byte[])obj, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            LOG.warn(e.getMessage(), e);
        }
        return response;
    }
    
    public void handleMessage(byte[] body) throws IOException {
        LOG.info("Received sensor data : {}", new String(body, "UTF-8"));
        String[] message = new String(body, "UTF-8").split(",");
        String agentId = message[0];
        String name = message[1];
        String type = message[2];
        String value = message[3];
        
        Calendar currentDate = Calendar.getInstance();
        currentDate.set(Calendar.SECOND, 0);
        
        TimedSensorData sensorData = new TimedSensorData(agentId, name, type, value, currentDate.getTime());
        
        weeklyStorage.insertOrUpdate(sensorData);
        
        if (name.equals("temperature")) {
            currentValues.put("temperature", value);
            currentValues.put("temperatureDate", currentDate.getTime());
        } else if (name.equals("humidity")) {
            currentValues.put("humidity", value);
            currentValues.put("humidityDate", currentDate.getTime());
        }
    }
}
