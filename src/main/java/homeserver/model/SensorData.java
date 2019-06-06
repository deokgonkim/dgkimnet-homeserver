package homeserver.model;

/**
 * Value object that holds sensor's data.
 * 
 * @author dgkim
 *
 */
public class SensorData {

    protected String agentId;
    protected String name;
    protected String type;
    protected String value;
    
    public SensorData() {
        
    }
    
    public SensorData(String agentId, String name, String type, String value) {
        this.agentId = agentId;
        this.name = name;
        this.type = type;
        this.value = value;
    }
    
    public String getAgentId() {
        return agentId;
    }
    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
}
