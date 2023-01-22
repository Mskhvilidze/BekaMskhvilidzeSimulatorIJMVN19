package sample.presenter.database;

import java.util.List;
import java.util.Optional;

public interface AutomatonStore {

    void createTable(String tableName, double width, double height, double panelWidth, double panelHeight, double speed);
    void shutdown();
    void dropTable(String tableName);
    List<String> getAllTables();
    Optional<String> findTable(String tableName);

    List<String> getTable(String tableName);
}
