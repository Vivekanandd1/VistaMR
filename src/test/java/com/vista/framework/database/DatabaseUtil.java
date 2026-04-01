package com.vista.framework.database;

import com.vista.framework.config.ConfigKeys;
import com.vista.framework.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Database Utility for SQL operations.
 * Provides connection management and query execution.
 */
public class DatabaseUtil {
    
    private static final Logger logger = LogManager.getLogger(DatabaseUtil.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    
    private static DatabaseUtil instance;
    private Connection connection;
    
    private DatabaseUtil() {}
    
    public static DatabaseUtil getInstance() {
        if (instance == null) {
            instance = new DatabaseUtil();
        }
        return instance;
    }
    
    /**
     * Get database connection
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = createConnection();
        }
        return connection;
    }
    
    private Connection createConnection() throws SQLException {
        String host = config.get(ConfigKeys.DB_HOST, "localhost");
        String port = config.get(ConfigKeys.DB_PORT, "3306");
        String database = config.get(ConfigKeys.DB_NAME, "UserCred");
        String username = config.get(ConfigKeys.DB_USER, "root");
        String password = config.get(ConfigKeys.DB_PASSWORD, "");
        
        String url = String.format("jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                host, port, database);
        
        logger.info("Connecting to database: {}", url);
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, username, password);
            logger.info("Database connection established");
            return conn;
        } catch (ClassNotFoundException e) {
            logger.error("MySQL Driver not found", e);
            throw new SQLException("MySQL Driver not found", e);
        }
    }
    
    /**
     * Execute a SELECT query and return results as list of maps
     */
    public List<Map<String, Object>> executeQuery(String query) throws SQLException {
        logger.debug("Executing query: {}", query);
        
        List<Map<String, Object>> results = new ArrayList<>();
        
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = rs.getObject(i);
                    row.put(columnName, value);
                }
                results.add(row);
            }
            
            logger.info("Query returned {} rows", results.size());
        }
        
        return results;
    }
    
    /**
     * Execute a SELECT query with parameters
     */
    public List<Map<String, Object>> executeQuery(String query, Object... params) throws SQLException {
        logger.debug("Executing parameterized query: {}", query);
        
        List<Map<String, Object>> results = new ArrayList<>();
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(query)) {
            setParameters(pstmt, params);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        Object value = rs.getObject(i);
                        row.put(columnName, value);
                    }
                    results.add(row);
                }
                
                logger.info("Query returned {} rows", results.size());
            }
        }
        
        return results;
    }
    
    /**
     * Execute INSERT, UPDATE, or DELETE query
     */
    public int executeUpdate(String query) throws SQLException {
        logger.debug("Executing update query: {}", query);
        
        try (Statement stmt = getConnection().createStatement()) {
            int rowsAffected = stmt.executeUpdate(query);
            logger.info("Update affected {} rows", rowsAffected);
            return rowsAffected;
        }
    }
    
    /**
     * Execute INSERT, UPDATE, or DELETE query with parameters
     */
    public int executeUpdate(String query, Object... params) throws SQLException {
        logger.debug("Executing parameterized update query: {}", query);
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(query)) {
            setParameters(pstmt, params);
            int rowsAffected = pstmt.executeUpdate();
            logger.info("Update affected {} rows", rowsAffected);
            return rowsAffected;
        }
    }
    
    /**
     * Execute insert and return generated key
     */
    public long executeInsertAndGetKey(String query, Object... params) throws SQLException {
        logger.debug("Executing insert query: {}", query);
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(query, 1)) {
            setParameters(pstmt, params);
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    long generatedKey = rs.getLong(1);
                    logger.info("Generated key: {}", generatedKey);
                    return generatedKey;
                }
            }
        }
        
        throw new SQLException("Failed to retrieve generated key");
    }
    
    /**
     * Check if record exists
     */
    public boolean exists(String tableName, String columnName, String value) throws SQLException {
        String query = String.format("SELECT COUNT(*) FROM %s WHERE %s = ?", tableName, columnName);
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(query)) {
            pstmt.setString(1, value);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    logger.debug("Record exists: {}", count > 0);
                    return count > 0;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Get single value from database
     */
    public Object getSingleValue(String query, Object... params) throws SQLException {
        logger.debug("Getting single value: {}", query);
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(query)) {
            setParameters(pstmt, params);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Object value = rs.getObject(1);
                    logger.debug("Single value: {}", value);
                    return value;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Get single value as string
     */
    public String getStringValue(String query, Object... params) throws SQLException {
        Object value = getSingleValue(query, params);
        return value != null ? value.toString() : null;
    }
    
    /**
     * Get single value as integer
     */
    public Integer getIntValue(String query, Object... params) throws SQLException {
        Object value = getSingleValue(query, params);
        return value != null ? ((Number) value).intValue() : null;
    }
    
    /**
     * Delete record by ID
     */
    public int deleteById(String tableName, String idColumn, String idValue) throws SQLException {
        String query = String.format("DELETE FROM %s WHERE %s = ?", tableName, idColumn);
        return executeUpdate(query, idValue);
    }
    
    /**
     * Close database connection
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                logger.info("Database connection closed");
            } catch (SQLException e) {
                logger.error("Error closing database connection", e);
            }
        }
    }
    
    private void setParameters(PreparedStatement pstmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
    }
    
    /**
     * Execute a stored procedure
     */
    public Map<String, Object> executeStoredProcedure(String procedureCall, Object... params) throws SQLException {
        logger.debug("Executing stored procedure: {}", procedureCall);
        
        Map<String, Object> results = new HashMap<>();
        
        try (CallableStatement cstmt = getConnection().prepareCall(procedureCall)) {
            setParameters(cstmt, params);
            
            boolean hasResultSet = cstmt.execute();
            
            if (hasResultSet) {
                try (ResultSet rs = cstmt.getResultSet()) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    
                    List<Map<String, Object>> rows = new ArrayList<>();
                    while (rs.next()) {
                        Map<String, Object> row = new HashMap<>();
                        for (int i = 1; i <= columnCount; i++) {
                            String columnName = metaData.getColumnName(i);
                            Object value = rs.getObject(i);
                            row.put(columnName, value);
                        }
                        rows.add(row);
                    }
                    results.put("rows", rows);
                }
            }
        }
        
        return results;
    }
}
