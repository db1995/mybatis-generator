package io.nobug.mb;

import java.io.IOException;
import java.sql.SQLException;

/**
 * This is the entry point for program execution.
 *
 * @author db1995
 */
public class Main {
    /**
     * You just need to run this main(String[] args) method to generate.
     * The main(String[] args) method will invoke other method.
     */
    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        ConfigLoader.load();
        JDBCUtil.connect();
    }
}