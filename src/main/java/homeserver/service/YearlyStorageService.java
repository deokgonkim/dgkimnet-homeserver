package homeserver.service;

import homeserver.model.TimedSensorData;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YearlyStorageService {
    
    private static final Logger LOG = LoggerFactory.getLogger(YearlyStorageService.class);
    
    @Autowired private SqlSessionTemplate sqlSession;
    
    /**
     * returns Agents List for stored data
     * 
     * @see select_agents in file:mybatis/mapper/recent_year.xml
     * @return
     * @throws SQLException
     */
    public List<String> selectAgents() throws SQLException {
        return sqlSession.selectList("recent_year.select_agents");
    }
    
    public List selectRecentListFor(String agentId, String name) {
        Map parameters = new HashMap();
        parameters.put("agentId", agentId);
        parameters.put("name", name);
        return sqlSession.selectList("recent_year.select_recent", parameters);
    }
    
    public List selectListForTimeRange(String agentId, String name, Date from, Date to) {
        Map parameters = new HashMap();
        parameters.put("agentId", agentId);
        parameters.put("name", name);
        parameters.put("from", from);
        parameters.put("to", to);
        return sqlSession.selectList("recent_year.select_range", parameters);
    }

    public void insertOrUpdate(TimedSensorData data) {
        LOG.info("Got - {} {}", new Date(), data);
        TimedSensorData existingData = sqlSession.selectOne("recent_year.select_one", data);
        
        if (existingData == null) {
            sqlSession.insert("recent_year.insert_one", data);
        } else {
            sqlSession.update("recent_year.update_one", data);
        }
    }
    
    public List<TimedSensorData> selectUpdatedWithin(Date from, Date to) {
        Map parameters = new HashMap();
        parameters.put("from", from);
        parameters.put("to", to);
        return sqlSession.selectList("recent_year.updated_between", parameters);
    }
    
    public List<TimedSensorData> selectMinMaxAvg(String agentId, String name, Date from, Date to) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("agentId", agentId);
        parameters.put("name", name);
        parameters.put("from", from);
        parameters.put("to", to);
        return sqlSession.selectList("recent_year.select_min_max_avg", parameters);
    }
}
