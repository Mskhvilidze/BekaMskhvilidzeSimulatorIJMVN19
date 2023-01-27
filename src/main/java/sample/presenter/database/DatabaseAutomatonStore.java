package sample.presenter.database;

import sample.presenter.Service;
import java.sql.*;
import java.util.*;

public class DatabaseAutomatonStore implements AutomatonStore {

    private Connection connection;
    private static final String NAME = "Name";
    private static final String WIDTH = "Width";
    private static final String HEIGHT = "Height";
    private static final String PANEL_WIDTH = "Panel_Width";
    private static final String PANEL_HEIGHT = "Panel_Height";
    private static final String SPEED = "Speed";
    private static final String SIZE = "Size";
    private static final String X = "X";
    private static final String Y = "Y";


    public DatabaseAutomatonStore() {
        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        this.connection = dataBaseConnection.getConnection();
    }

    @Override
    public void createTable(String tableName, Map<String, Double> columns) {
        boolean checkExistenceAndUpdate = checkExistenceAndUpdate(tableName, columns);
        String query = "CREATE TABLE " + tableName + " (Name VARCHAR(55) PRIMARY KEY NOT NULL, Width double, Height double," +
                       "Panel_Width double, Panel_Height double, Speed Double, Size Double, X Double, Y Double)";
        boolean isValid = true;
        if (!checkExistenceAndUpdate) {
            try (PreparedStatement statement = this.connection.prepareStatement(query)) {
                isValid = statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
                Service.alert("Tabelle konnte nicht erstellt werden!", "");
            }
        }
        if (!isValid) {
            query = "INSERT INTO " + tableName +
                    " (Name, Width, Height, Panel_Width, Panel_Height, Speed, Size, X, Y) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            insertAutomaton(query, tableName, columns);
        }
    }

    @Override
    public Optional<String> findTable(String tableName) {
        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet result = databaseMetaData.getTables(null, null, "%", new String[]{"TABLE"});
            while (result.next()) {
                String name = result.getString("TABLE_NAME");
                if (name.equals(tableName)) {
                    return Optional.of(name);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Service.alert("Table not Fount", "");
        }
        return Optional.empty();
    }

    @Override
    public List<String> getTable(String tableName) {
        String query = "SELECT * FROM " + tableName + " WHERE " + NAME + " = ?";
        List<String> list = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, tableName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                list.add(resultSet.getString(WIDTH));
                list.add(resultSet.getString(HEIGHT));
                list.add(resultSet.getString(PANEL_WIDTH));
                list.add(resultSet.getString(PANEL_HEIGHT));
                list.add(resultSet.getString(SPEED));
                list.add(resultSet.getString(SIZE));
                list.add(resultSet.getString(X));
                list.add(resultSet.getString(Y));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Service.alert("Tabelle kann nicht hergestellt werden", "");
        }
        return list;
    }

    @Override
    public void dropTable(String tableName) {
        String query = "Drop table " + tableName;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            statement.execute();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            Service.alert("Tabelle konnte nicht gelöscht werden!", "Tabelle");
            try {
                connection.rollback();
            } catch (SQLException i) {
                i.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public List<String> getAllTables() {
        List<String> tables = new ArrayList<>();
        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet result = databaseMetaData.getTables(null, null, "%", new String[]{"TABLE"});
            while (result.next()) {
                String tableName = result.getString("TABLE_NAME");
                tables.add(tableName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Service.alert("Tabellen wurden nicht gefunden", "");
        }
        return tables;
    }

    @Override
    public void shutdown() {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                Service.alert("Verbindung kann nicht herunterfahren werden", "DataBase");
            }
        }
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException se) {
            Service.alert("Abschaltung der Datenbank", "DataBase");
        }
    }

    //Private Methode
    private void insertAutomaton(String query, String table, Map<String, Double> columns) {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            statement.setString(1, table);
            statement.setString(2, String.valueOf(columns.get("width")));
            statement.setString(3, String.valueOf(columns.get("height")));
            statement.setString(4, String.valueOf(columns.get("panelWidth")));
            statement.setString(5, String.valueOf(columns.get("panelHeight")));
            statement.setString(6, String.valueOf(columns.get("slider")));
            statement.setString(7, String.valueOf(columns.get("size")));
            statement.setString(8, String.valueOf(columns.get("x")));
            statement.setString(9, String.valueOf(columns.get("y")));
            statement.execute();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            Service.alert("Problem beim Einfügen", "Table");
            try {
                connection.rollback();
            } catch (SQLException i) {
                i.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkExistenceAndUpdate(String table, Map<String, Double> columns) {
        String query = "UPDATE  " + table +
                       " SET Width = ?, Height = ?, Panel_Width = ?, Panel_Height = ?, Speed = ?, Size = ?, X = ?, Y = ?  WHERE Name = ?";
        try {
            DatabaseMetaData databaseMetadata = connection.getMetaData();
            ResultSet resultSet = databaseMetadata.getTables(null, null, table, null);
            if (resultSet.next()) {
                Service.alert("Tabelle mit diesem Namen " + table + " existiert schon, daher wird sie aktualisiert!", "Tabelle");
            } else {
                System.out.println("tables not exists");
                return false;
            }
            resultSet.close();
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                connection.setAutoCommit(false);
                stmt.setString(1, String.valueOf(columns.get("width")));
                stmt.setString(2, String.valueOf(columns.get("height")));
                stmt.setString(3, String.valueOf(columns.get("panelWidth")));
                stmt.setString(4, String.valueOf(columns.get("panelHeight")));
                stmt.setString(5, String.valueOf(columns.get("slider")));
                stmt.setString(6, String.valueOf(columns.get("size")));
                stmt.setString(7, String.valueOf(columns.get("x")));
                stmt.setString(8, String.valueOf(columns.get("y")));
                stmt.setString(9, table);
                stmt.executeUpdate();
                connection.commit();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Service.alert("Prüfen, ob die Tabelle schon existiert", "Check");
            try {
                connection.rollback();
            } catch (SQLException i) {
                i.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
