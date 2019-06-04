package com.belatrix.practicaltest.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;

public class LogDao {

    private Map dbParams;

    public void writeLog(String message, int logType) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection(dbParams);
            statement = connection.createStatement();
            statement.executeUpdate(" insert into Log_Values( '" + message + "', " + String.valueOf(logType) + ")");
        } catch (SQLException exception) {
            throw exception;
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    private Connection getConnection(Map dbParams) throws SQLException {
        Connection connection = null;
        try {
            Properties connectionProps = new Properties();
            connectionProps.put(" user ", dbParams.get(" userName "));
            connectionProps.put(" password ", dbParams.get(" password "));
            connection = DriverManager.getConnection(" jdbc:"
                            + dbParams.get(" dbms ") + "://" + dbParams.get("serverName")
                            + ":"
                            + dbParams.get(" portNumber ") + "/",
                    connectionProps);
            return connection;
        } catch (SQLException exception) {
            throw exception;
        }
    }

    public void setDbParams(Map dbParams) {
        this.dbParams = dbParams;
    }
}
