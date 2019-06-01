package homeserver.model;

import java.util.Date;

public class Temperature extends AbstractSensorData {
    
    private double temperature;

    public Temperature(String agentId, Date date, double temperature) {
        super.setAgentId(agentId);
        super.setDate(date);
        this.temperature = temperature;
    }
    
    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
