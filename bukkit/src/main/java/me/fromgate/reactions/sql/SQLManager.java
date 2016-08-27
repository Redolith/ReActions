/*  
 *  ReActions, Minecraft bukkit plugin
 *  (c)2012-2014, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/reactions/
 *    
 *  This file is part of ReActions.
 *  
 *  ReActions is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ReActions is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with ReActions.  If not, see <http://www.gnorg/licenses/>.
 * 
 */

package me.fromgate.reactions.sql;

import me.fromgate.reactions.ReActions;
import me.fromgate.reactions.util.Param;
import me.fromgate.reactions.util.Variables;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class SQLManager {
    private static boolean enabled = false;
    private static String serverAdress = "127.0.0.1";
    private static String port = "3306";
    private static String dataBase;
    private static String userName;
    private static String password;
    private static String codepage = "UTF-8";


    public static void init() {
        loadCfg();
        saveCfg();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            enabled = true;
        } catch (ClassNotFoundException e) {
            ReActions.util.logOnce("mysqlinitfail", "MySQL JDBC Driver not found!");
            enabled = false;
            return;
        }
    }


    public static void loadCfg() {
        serverAdress = ReActions.instance.getConfig().getString("MySQL.server", "localhost");
        port = ReActions.instance.getConfig().getString("MySQL.port", "3306");
        dataBase = ReActions.instance.getConfig().getString("MySQL.database", "ReActions");
        userName = ReActions.instance.getConfig().getString("MySQL.username", "root");
        password = ReActions.instance.getConfig().getString("MySQL.password", "password");
        codepage = ReActions.instance.getConfig().getString("MySQL.codepage", "");
    }

    public static void saveCfg() {
        ReActions.instance.getConfig().set("MySQL.server", serverAdress);
        ReActions.instance.getConfig().set("MySQL.port", port);
        ReActions.instance.getConfig().set("MySQL.database", dataBase);
        ReActions.instance.getConfig().set("MySQL.username", userName);
        ReActions.instance.getConfig().set("MySQL.password", password);
        ReActions.instance.getConfig().set("MySQL.codepage", codepage);
        ReActions.instance.saveConfig();
    }


    public static void setQueryToVar(String player, String var, String query, int column, Param params) {
        if (!enabled) return;
        String result = SQLManager.executeSelect(query, column, params);
        Variables.setVar(player, var, result);
    }


    public static String executeSelect(String query, Param params) {
        return executeSelect(query, -1, params);
    }

    public static boolean compareSelect(String value, String query, int column, Param params) {
        String result = executeSelect(query, column, params);
        if (ReActions.util.isInteger(result, value)) return (Integer.parseInt(result) == Integer.parseInt(value));
        return result.equalsIgnoreCase(value);
    }

    public static Connection connectToMySQL() {
        return connectToMySQL(new Param());
    }

    // server port db user password codepage
    public static Connection connectToMySQL(Param params) {
        String сAddress = params.getParam("server", serverAdress);
        String cPort = params.getParam("port", port);
        String cDataBase = params.getParam("db", dataBase);
        String cUser = params.getParam("user", userName);
        String cPassword = params.getParam("password", password);
        String cCodepage = params.getParam("codepage", codepage);
        Properties prop = new Properties();
        if (!cCodepage.isEmpty()) {
            prop.setProperty("useUnicode", "true");
            prop.setProperty("characterEncoding", cCodepage);
        }
        prop.setProperty("user", cUser);
        prop.setProperty("password", cPassword);
        Connection connection = null;
        String connectionLine = "jdbc:mysql://" + сAddress + (cPort.isEmpty() ? "" : ":" + cPort) + "/" + cDataBase;
        try {
            connection = DriverManager.getConnection(connectionLine, prop);
        } catch (Exception e) {
            ReActions.util.logOnce("sqlconnect", "Failed to connect to database: " + connectionLine + " user: " + userName);
        }
        return connection;
    }


    public static String executeSelect(String query, int column, Param params) {
        if (!enabled) return "";

        Statement selectStmt = null;
        ResultSet result = null;
        String resultStr = "";
        Connection connection = connectToMySQL(params);

        try {
            selectStmt = connection.createStatement();
            result = selectStmt.executeQuery(query);
            if (result.first()) {
                int columns = result.getMetaData().getColumnCount();
                if (column > 0 && column <= columns) resultStr = result.getString(column);
            }
        } catch (Exception e) {
            ReActions.util.logOnce(query, "Failed to execute query: " + query);
        }
        try {
            if (result != null) result.close();
            if (selectStmt != null) selectStmt.close();
            if (connection != null) connection.close();
        } catch (Exception e) {
        }
        return resultStr;
    }


    public static boolean executeUpdate(String query, Param params) {
        if (!enabled) return false;
        Connection connection = connectToMySQL(params);
        if (connection == null) return false;
        Statement statement = null;
        boolean ok = false;
        try {
            statement = connection.createStatement();
            //statement.execute("SET NAMES 'utf8'");
            statement.executeUpdate(query);
            ok = true;
        } catch (Exception e) {
            ReActions.util.logOnce(query, "Failed to execute query: " + query);
            if (e.getMessage() != null) ReActions.util.logOnce(query + e.getMessage(), e.getMessage());
            e.printStackTrace();
        }
        try {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        } catch (Exception e) {
        }
        return ok;
    }


    public static boolean isEnabled() {
        return enabled;
    }

    public static boolean isSelectResultEmpty(String query) {
        if (!enabled) return false;
        Connection connection = connectToMySQL();
        if (connection == null) return false;

        Statement selectStmt = null;
        ResultSet result = null;
        boolean resultBool = false;

        try {
            selectStmt = connection.createStatement();
            result = selectStmt.executeQuery(query);
            resultBool = result.next();
        } catch (Exception e) {
            ReActions.util.logOnce(query, "Failed to execute query: " + query);
            if (e.getMessage() != null) ReActions.util.logOnce(query + e.getMessage(), e.getMessage());
        }
        try {
            if (result != null) result.close();
            if (selectStmt != null) selectStmt.close();
            if (connection != null) connection.close();
        } catch (Exception e) {
        }
        return resultBool;
    }
}
