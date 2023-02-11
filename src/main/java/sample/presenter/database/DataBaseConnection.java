package sample.presenter.database;

import sample.presenter.Service;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DataBaseConnection {

    private Connection dataSource;
    public static final String DATABASE_NAME = "AutomatonDB";

    /**
     * Constructor
     * Sets the database url
     * The variables that are required for the connection to the database are initialized in the constructor
     */
    public DataBaseConnection() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            File file = new File("AutomatonDB/");
            if (file.exists()) {
                this.dataSource = DriverManager.getConnection("jdbc:derby:" + DATABASE_NAME + ";create=false");
            } else {
                this.dataSource = DriverManager.getConnection("jdbc:derby:" + DATABASE_NAME + ";create=true");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            Service.alert("Driver wurde nicht gefunden", "Driver");
        }

    }

    /**
     * Returns a new {@code Connection} to the sql database
     *
     * @return a new {@code Connection} to the sql database
     */
    public Connection getConnection() {
        try {
            if (dataSource != null && !dataSource.isClosed() && dataSource.isValid(0)) {
                return dataSource;
            }
            if (dataSource != null) {
                dataSource.close();
                dataSource = null;
                return getConnection();
            }
            dataSource = DriverManager.getConnection("jdbc:derby:" + DATABASE_NAME + ";create=true");
            return dataSource;
        } catch (SQLException exc) {
            return null;
        }
    }
}
