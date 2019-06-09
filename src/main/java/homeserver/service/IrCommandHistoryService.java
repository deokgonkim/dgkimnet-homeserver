package homeserver.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import homeserver.model.IrCmdHistory;

@Service
public class IrCommandHistoryService {

    private static final Logger LOG = LoggerFactory.getLogger(DbService.class);
    
    @Autowired
    private SqlSessionTemplate sqlSession;
    
    public List selectRecent() {
        return sqlSession.selectList("ir_cmd_history.select_recent");
    }
    
    public int insertCmdHistory(String agentId, String command) {
        Map map = new HashMap();
        map.put("agentId", agentId);
        map.put("cmd", command);
        sqlSession.insert("ir_cmd_history.insert_one", map);
        // TODO Is last_insert_id reliable?
        // Is there any possible cases resulting in wrong id?
        int id = sqlSession.selectOne("ir_cmd_history.last_insert_id");
        return id;
    }
    
    public IrCmdHistory updateCmdHistory(int id, String result) {
        Map map = new HashMap();
        map.put("id", id);
        map.put("result", result);
        sqlSession.update("ir_cmd_history.update_one", map);
        return sqlSession.selectOne("ir_cmd_history.select_one", id);
    }
}
