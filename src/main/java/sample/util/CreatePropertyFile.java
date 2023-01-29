package sample.util;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CreatePropertyFile {
    private static Map<String, String> english;
    private static Map<String, String> german;
    private static Properties properties;

    static {
        english = new HashMap<>();
        english.put("Automat", "_Automatic");
        english.put("Neu", "_New");
        english.put("Laden", "_Load");
        english.put("Editor", "_Editor");
        english.put("Beenden", "_Finish");
        english.put("Population", "_Population");
        english.put("Groesse aendern", "_Change Size");
        english.put("Loeschen", "_Clear");
        english.put("Erzeugen", "_Generate");
        english.put("Vergroessern", "_Enlargee");
        english.put("Verkleinern", "_Zoom out");
        english.put("Speichern", "_Save");
        english.put("Druecken", "_Press");
        english.put("Simulation", "_Simulation");
        english.put("Schritt", "_Step");
        english.put("Start", "_Begin");
        english.put("Stop", "_Stop");
        english.put("Einstellungen", "_Settings");
        english.put("SpeichernDB", "_SaveDB");
        english.put("WiederherstellenDB", "_RestoreDB");
        english.put("LoeschenDB", "RemoveDB");
    }

    static {
        german = new HashMap<>();
        german.put("Automatic", "_Automat");
        german.put("New", "_Neu");
        german.put("Load", "_Laden");
        german.put("Editor", "_Editor");
        german.put("Finish", "Beenden");
        german.put("Population", "_Population");
        german.put("Change Size", "_Groesse aendern");
        german.put("Clear", "_Loeschen");
        german.put("Generate", "_Erzeugen");
        german.put("Enlarge", "_Vergroessern");
        german.put("Zoom out", "_Verkleinern");
        german.put("Save", "_Speichern");
        german.put("Press", "_Druecken");
        german.put("Simulation", "_Simulation");
        german.put("Step", "_Schritt");
        german.put("Begin", "_Start");
        german.put("Stop", "_Stop");
        german.put("Settings", "_Einstellungen");
        german.put("SaveDB", "_SpeichernDB");
        german.put("RestoreDB", "_WiederherstellenDB");
        german.put("RemoveDB", "_LoeschenDB");
    }

    private CreatePropertyFile() {

    }

    public static void createFile(File file, boolean isChangeEnglish) {
        OutputStream outputStream = null;
        try {
            properties = new Properties();
            writeProperties(isChangeEnglish);
            outputStream = Files.newOutputStream(Paths.get(file.getPath()));
            properties.store(outputStream, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException io) {
                io.printStackTrace();
            }
        }
    }

    private static void writeProperties(boolean isChangeEnglish) {
        if (isChangeEnglish) {
            for (Map.Entry<String, String> entry : english.entrySet()) {
                properties.setProperty(entry.getKey(), entry.getValue());
            }
        } else {
            for (Map.Entry<String, String> entry : german.entrySet()) {
                properties.setProperty(entry.getKey(), entry.getValue());
            }
        }
    }


    public static void updateProperties(File file, boolean isChangeEnglish) {
        clear(file);
        if (isChangeEnglish) {
            for (Map.Entry<String, String> entry : english.entrySet()) {
                changedReloadingStrategy(file, entry.getKey(), entry.getValue());
            }
        } else {
            for (Map.Entry<String, String> entry : german.entrySet()) {
                properties.setProperty(entry.getKey(), entry.getValue());
            }
        }
    }

    public static void changedReloadingStrategy(File file, String key, String value) {
        try {
            PropertiesConfiguration config = new PropertiesConfiguration(file);
            config.setProperty(key, value);
            config.save();
            config.reload();
            config.refresh();
        } catch (ConfigurationException c) {
            c.printStackTrace();
        }
    }

    public static void clear(File file) {
        try {
            PropertiesConfiguration config = new PropertiesConfiguration(file);
            config.clear();
            config.save();
        } catch (ConfigurationException c) {
            c.printStackTrace();
        }
    }
}
