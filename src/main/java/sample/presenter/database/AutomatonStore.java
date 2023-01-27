package sample.presenter.database;

import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AutomatonStore {

    void createTable(String tableName, Map<String, Double> columns);

    void shutdown();

    void dropTable(String tableName);

    List<String> getAllTables();

    Optional<String> findTable(String tableName);

    List<String> getTable(String tableName);
}
