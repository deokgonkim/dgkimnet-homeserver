package homeserver.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DbService {

    private static final Logger LOG = LoggerFactory.getLogger(DbService.class);
    
    @Autowired
    private DataSource dataSource = null;
    
    public String getDbName() throws SQLException {
        String name = null;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT NAME FROM DB_NAME")){
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    name = rs.getString(1);
                }
            }
        } catch(SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        return name;
    }
    
    public Date getNow() throws SQLException {
        Date now = null;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT NOW()");) {
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    now = rs.getDate(1);
                }
            }
        } catch(SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        return now;
    }
}
