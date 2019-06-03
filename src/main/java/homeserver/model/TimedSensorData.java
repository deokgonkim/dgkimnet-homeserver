package homeserver.model;

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
}
