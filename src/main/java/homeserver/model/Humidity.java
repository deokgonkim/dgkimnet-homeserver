package homeserver.model;

import java.util.Date;

public class Humidity extends AbstractSensorData {
    
    private double humidity;

    public Humidity(String agentId, Date date, double humidity) {
        super.setAgentId(agentId);
        super.setDate(date);
        this.humidity = humidity;
    }
    
    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }
}
