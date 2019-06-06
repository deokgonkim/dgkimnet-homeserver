package homeserver.service;

import homeserver.model.TimedSensorData;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MqClientService {
    
    private static final Logger LOG = LoggerFactory.getLogger(MqClientService.class);
    
    public Map currentValues = new HashMap();
    
    @Autowired
    private WeeklyStorageService weeklyStorage;
    
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
        
        try {
            weeklyStorage.insertOrUpdate(sensorData);
        } catch (SQLException e) {
            LOG.warn(e.getMessage(), e);
        }
        
        if (name.equals("temperature")) {
            currentValues.put("temperature", value);
            currentValues.put("temperatureDate", currentDate.getTime());
        } else if (name.equals("humidity")) {
            currentValues.put("humidity", value);
            currentValues.put("humidityDate", currentDate.getTime());
        }
    }
}
