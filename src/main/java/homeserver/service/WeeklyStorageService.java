package homeserver.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeeklyStorageService {
    
    private static final Logger LOG = LoggerFactory.getLogger(DbService.class);
    
    @Autowired
    private DataSource dataSource = null;

    public void insertOrUpdate(String agentId, String name, String type, String lastValue) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement("insert into recent_week (agent_id, datetime, name, type, last_value, min_value, max_value, updated) values(?, ?, ?, ?, ?, 0.0, 0.0, now())");
            pstmt.setString(1, agentId);
            pstmt.setTimestamp(2, new java.sql.Timestamp(cal.getTimeInMillis()));
            pstmt.setString(3, name);
            pstmt.setString(4, type);
            pstmt.setDouble(5, Double.valueOf(lastValue));
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            if (conn != null) {
                conn.close();
                conn = null;
            }
        }
    }
}
