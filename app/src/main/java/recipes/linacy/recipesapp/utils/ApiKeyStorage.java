package recipes.linacy.recipesapp.utils;

import android.content.Context;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ApiKeyStorage {

    private static final String LOCAL_PROPERTIES_FILE = "local.properties";

    public static String getApiKey(Context context) {
        Properties properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream(context.getFilesDir() + "/" + LOCAL_PROPERTIES_FILE);
            properties.load(fis);
            return properties.getProperty("api.key");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
