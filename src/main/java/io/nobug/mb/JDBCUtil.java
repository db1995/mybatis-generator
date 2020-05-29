package io.nobug.mb;


import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Connect to database.
 *
 * @author db1995
 */
public class JDBCUtil {
    private final static String DRIVER = "com.mysql.cj.jdbc.Driver";
    private final static String ALL_SIGN = "*";

    /**
     * Connect to the database.
     */
    public static void connect() throws ClassNotFoundException, IOException, SQLException {
        System.out.println("Start to connect database...");
        Class.forName(DRIVER);
        Connection connection;
        connection = DriverManager.getConnection(
                "jdbc:mysql://" + DevelopInfo.host + ":3306/" + DevelopInfo.database + "?useSSL=FALSE",
                DevelopInfo.user, DevelopInfo.password);
        System.out.println("OK.\n");
        checkTableAmount(connection);
        connection.close();
    }

    /**
     * Check the amount of table(s).
     */
    public static void checkTableAmount(Connection connection) throws SQLException {
        System.out.println("Start to create directories and files...");
        if (DevelopInfo.tableList.stream().anyMatch(t -> ALL_SIGN.equals(t))) {
            ResultSet resultSet = connection.prepareStatement("SHOW TABLES").executeQuery();
            while (resultSet.next()) {
                query(connection, (String) resultSet.getObject(1));
            }
        } else {
            DevelopInfo.tableList.forEach(table -> query(connection, table));
        }
        System.out.println("OK.");
    }

    public static void query(Connection connection, String table) {
        try {
            PreparedStatement prepareStatement;
            prepareStatement = connection.prepareStatement("SHOW COLUMNS FROM " + table);
            ResultSet result = prepareStatement.executeQuery();
            handleResult(result, table);
            prepareStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void handleResult(ResultSet result, String table) throws SQLException, IOException {
        List<String> list = new ArrayList<>();
        while (result.next()) {
            String field = result.getString(1);
            list.add(field);
            String type = result.getString(2);
            list.add(type);
        }
        //Create files.
        FileUtil.createDirectoryAndFiles(list, table);
    }
}