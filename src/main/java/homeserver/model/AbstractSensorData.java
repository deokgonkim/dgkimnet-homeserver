package homeserver.model;

import java.util.Date;

public abstract class AbstractSensorData {

    private String agentId;
    private Date date;
    
    public String getAgentId() {
        return agentId;
    }
    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
}
