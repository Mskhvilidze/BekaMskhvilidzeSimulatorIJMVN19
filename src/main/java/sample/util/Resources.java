package sample.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

public class Resources {


    public static String path;

    static {
        try {
            URI uri = Resources.class.getResource("/resources").toURI();
            if (uri.getScheme().equals("jar")) {
                path = "/resources/";
            } else {
                path = "/";
            }
        } catch (Exception exc) {
            path = "/";
        }
    }

    public static String readResourcesFile(String resourcesFiles) {
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(Resources.class.getResourceAsStream(path + resourcesFiles)))) {
            StringBuilder buffer = new StringBuilder();
            String eingabe = null;
            while ((eingabe = in.readLine()) != null) {
                buffer.append(eingabe);
                buffer.append(System.lineSeparator());
            }
            return buffer.toString();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return "";
    }

}
