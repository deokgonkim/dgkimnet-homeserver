package homeserver.service;

import homeserver.model.TimedSensorData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeeklyStorageService {
    
    private static final Logger LOG = LoggerFactory.getLogger(DbService.class);
    
    @Autowired
    private DataSource dataSource = null;
    
    @Autowired private SqlSessionTemplate sqlSession;

    public void insertOrUpdate(TimedSensorData data) throws SQLException {
        LOG.info(String.format("Got - {}", data));
        TimedSensorData existingData = sqlSession.selectOne("recent_week.select_one", data);
        
        if (existingData == null) {
            sqlSession.insert("recent_week.insert_one", data);
        } else {
            sqlSession.update("recent_week.update_one", data);
        }
    }
}
