package sample.presenter;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Menu;
import sample.util.CreatePropertyFile;
import javafx.scene.text.Text;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class PropertyPresenter {

    private ObservableResourceFactory resourceFactory;
    private Properties properties;
    private static final String DE_US = "ii.Test_de_US.properties";
    private static final String US_DE = "ii.Test_US_de.properties";
    private ResourceBundle bundle;
    private String[] basketAutomataUS;
    private String[] basketAutomataDE;
    private String[] basketPopUS;
    private String[] basketPopDE;
    private String[] basketSimDE;
    private String[] basketSimUS;
    private String[] basketSettDE;
    private String[] basketSettUS;

    public PropertyPresenter(Menu automata, Menu pop, Menu sim, Menu settings, boolean isChangeEnglish, boolean isChangeGerman) {
        initList();
        this.properties = new Properties();
        this.resourceFactory = new ObservableResourceFactory();
        File file;
        if (isChangeEnglish && !isChangeGerman) {
            file = new File(US_DE);
            if (!file.exists()) {
                CreatePropertyFile.createFile(file, true);
                Service.alert("File existiert nicht, nachdem Programm neu gestartet wird, wird die Datei wiederherstellt", "");
                return;
            } else {
                bundle = ResourceBundle.getBundle("ii.Test", Locale.getDefault());
                this.resourceFactory.setResources(bundle);
                Service.alert("TEST " + bundle.getString("Laden"), "");
            }
        } else if (!isChangeEnglish && isChangeGerman) {
            file = new File(US_DE);
            if (!file.exists()) {
                CreatePropertyFile.createFile(file, false);
                Service.alert("File existiert nicht, nachdem Programm neu gestartet wird, wird die Datei wiederherstellt", "");
                return;
            } else {
                bundle = ResourceBundle.getBundle("ii.Test", new Locale("de"));
                this.resourceFactory.setResources(bundle);
                Service.alert("TEST " + bundle.getString("Load"), "");
            }
        } else {
            Service.alert("Oops, Etwas ist schief gelaufen, Variable in Presenter wurde nicht gesetzt", "");
            return;
        }
        changeAutomataLanguage(automata, isChangeEnglish);
        changePopLanguage(pop, isChangeEnglish);
        changeSimLanguage(sim, isChangeEnglish);
        changeSettingsLanguage(settings, isChangeEnglish);
    }

    private void initList() {
        basketAutomataUS = new String[]{"Automat", "Neu", "Laden", "Editor", "Beenden"};
        basketAutomataDE = new String[]{"Automatic", "New", "Load", "Finish"};
        basketPopUS = new String[]{"Population", "Groesse aendern", "Loeschen", "Erzeugen", "Vergroessern", "Verkleinern", "Speichern",
                "Druecken"};
        basketPopDE = new String[]{"Population", "Change Size", "Clear", "Generate", "Enlarge", "Zoom out", "Save", "Press"};
        basketSimUS = new String[]{"Simulation", "Schritt", "Start", "Stop"};
        basketSimDE = new String[]{"Simulation", "Step", "Begin", "Stop"};
        basketSettUS = new String[]{"Einstellungen", "SpeichernDB", "WiederherstellenDB", "LoeschenDB"};
        basketSettDE = new String[]{"Settings", "SaveDB", "RestoreDB", "RemoveDB"};

    }


    private void changeAutomataLanguage(Menu automata, boolean isChangeEnglish) {
        if (isChangeEnglish) {
            readePropertiesFile(Paths.get(new File(DE_US).getPath()));
            changePropertyAutomata(automata, basketAutomataUS, true);
        } else {
            readePropertiesFile(Paths.get(new File(US_DE).getPath()));
            changePropertyAutomata(automata, basketAutomataDE, false);
        }
    }


    private void changePopLanguage(Menu pop, boolean isChangeEnglish) {
        if (isChangeEnglish) {
            readePropertiesFile(Paths.get(new File(DE_US).getPath()));
            changePropertyAutomata(pop, basketPopUS, true);
        } else {
            readePropertiesFile(Paths.get(new File(US_DE).getPath()));
            changePropertyAutomata(pop, basketPopDE, false);
        }
    }

    private void changeSimLanguage(Menu sim, boolean isChangeEnglish) {
        if (isChangeEnglish) {
            readePropertiesFile(Paths.get(new File(DE_US).getPath()));
            changePropertyAutomata(sim, basketSimUS, true);
        } else {
            readePropertiesFile(Paths.get(new File(US_DE).getPath()));
            changePropertyAutomata(sim, basketSimDE, false);
        }
    }

    private void changeSettingsLanguage(Menu settings, boolean isChangeEnglish) {
        if (isChangeEnglish) {
            readePropertiesFile(Paths.get(new File(DE_US).getPath()));
            changePropertyAutomata(settings, basketSettUS, true);
        } else {
            readePropertiesFile(Paths.get(new File(US_DE).getPath()));
            changePropertyAutomata(settings, basketSettDE, false);
        }
    }

    /**
     * durch die Methode i18n() werden die Velues aus der Property-Dateig geholt und in MenutItem als Text eingetragen
     *
     * @param node   Manuitem
     * @param basket Array
     */
    private void changePropertyAutomata(Menu node, String[] basket, boolean isChangeEnglish) {
        node.textProperty().bind(i18n(basket[0]).isValid() ? i18n(basket[0]) : new Text(properties.getProperty(basket[0])).textProperty());
        int j = 0;
        int x;
        for (int i = 0; i < node.getItems().size(); i++) {
            if (node.getItems().get(i).getText() == null) {
                j++;
                continue;
            }
            x = (i - j) + 1 >= basket.length ? (i - j) - 1 : (i - j) + 1;
            // CreatePropertyFile.updateProperties(file, isChangeEnglish);
            node.getItems().get(i).textProperty()
                    .bind(i18n(basket[x]).isValid() ? i18n(basket[x]) : new Text(properties.getProperty(basket[x])).textProperty());

        }
    }

    private void readePropertiesFile(Path path) {
        InputStream stream = null;
        try {
            stream = Files.newInputStream(path);
            properties.load(stream);
            this.resourceFactory.setResources(bundle);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public StringBinding i18n(String value) {
        return this.resourceFactory.getStringBinding(value);
    }

    static class ObservableResourceFactory {

        private ObjectProperty<ResourceBundle> resources = new SimpleObjectProperty<>();

        public ObjectProperty<ResourceBundle> resourcesProperty() {
            return resources;
        }

        public final ResourceBundle getResources() {
            return resourcesProperty().get();
        }

        public final void setResources(ResourceBundle resources) {
            resourcesProperty().set(resources);
        }

        public StringBinding getStringBinding(String key) {
            return new StringBinding() {
                {
                    bind(resourcesProperty());
                }

                @Override
                public String computeValue() {
                    return getResources().getString(key);
                }
            };
        }
    }
}