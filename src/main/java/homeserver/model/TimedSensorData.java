package homeserver.model;

import java.text.MessageFormat;
import java.util.Date;

/**
 * Value object that holds sensor data with Timestamp
 * 
 * @author dgkim
 *
 */
public class TimedSensorData extends SensorData {
    private Date datetime;
    
    public TimedSensorData() {
        
    }
    
    public TimedSensorData(String agentId, String name, String type, String value, Date datetime) {
        super(agentId, name, type, value);
        this.datetime = datetime;
    }
    
    public Date getDatetime() {
        return datetime;
    }
    
    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
    
    @Override
    public String toString() {
        return MessageFormat.format("homeserver.model.TimedSensorData({0}, {1}, {2}, {3}, {4})", agentId, name, type, value, datetime);
    }
}
