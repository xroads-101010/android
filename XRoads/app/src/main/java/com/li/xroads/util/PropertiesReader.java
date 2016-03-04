package com.li.xroads.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// this class is getting load from Main Activity as it needs Context.
// Need to find better way to do this.
public class PropertiesReader {
   private static Properties properties =null;

    public PropertiesReader(Context context) {
        if (properties == null) {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = null;
            try {
                inputStream = assetManager.open("application.properties");
                properties.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

}
