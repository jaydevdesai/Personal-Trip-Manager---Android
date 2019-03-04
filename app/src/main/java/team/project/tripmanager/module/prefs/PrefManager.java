package team.project.tripmanager.module.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * This is a Utility for reading and writing to shared preferences.
 * This class also contains the constants for the preference names and the keys.
 * These constants are defined in inner classes <code>Pref</code> and <code>Key</code>.
 */
public class PrefManager {

    private Context context;
    private String prefName;

    //FIXME - we should use MAApplication's context to clean up
    //the code.
    public PrefManager(Context context, String prefName) {
        this.context = context;
        this.prefName = prefName;
    }

    /**
     * Puts given key-value pair to the Shared Preferences.
     *
     * @param key
     * @param value - String
     */
    public void put(String key, String value) {
        SharedPreferences.Editor edit = context.getSharedPreferences(prefName, Context.MODE_PRIVATE).edit();
        edit.putString(key, value).apply();
    }

    /**
     * Puts given key-value pair to the Shared Preferences.
     *
     * @param key
     * @param value - boolean
     */
    public void put(String key, boolean value) {
        SharedPreferences.Editor edit = context.getSharedPreferences(prefName, Context.MODE_PRIVATE).edit();
        edit.putBoolean(key, value).apply();
    }

    /**
     * Puts given key-value pair to the Shared Preferences.
     *
     * @param key
     * @param value - long
     */
    public void put(String key, long value) {
        SharedPreferences.Editor edit = context.getSharedPreferences(prefName, Context.MODE_PRIVATE).edit();
        edit.putLong(key, value).apply();
    }

    /**
     * Puts given key-value pair to the Shared Preferences.
     *
     * @param key
     * @param value - float
     */
    public void put(String key, float value) {
        SharedPreferences.Editor edit = context.getSharedPreferences(prefName, Context.MODE_PRIVATE).edit();
        edit.putFloat(key, value).apply();
    }

    /**
     * Puts given key-value pair to the Shared Preferences.
     *
     * @param key
     * @param value - int
     */
    public void put(String key, int value) {
        SharedPreferences.Editor edit = context.getSharedPreferences(prefName, Context.MODE_PRIVATE).edit();
        edit.putInt(key, value).apply();
    }

    /**
     * Returns String value for the given key, null if no value is found.
     *
     * @param key
     * @return String
     */
    public String getString(String key) {
        if (context != null) {
            return context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
                    .getString(key, null);
        }
        return null;
    }


    /**
     * Returns boolean value for the given key, can set default value as well.
     *
     * @param key,default value
     * @return boolean
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        if (context != null) {
            return context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
                    .getBoolean(key, defaultValue);
        }
        return defaultValue;
    }

    /**
     * Returns long value for the given key, -1 if no value is found.
     *
     * @param key
     * @return long
     */
    public long getLong(String key) {
        if (context != null) {
            return context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
                    .getLong(key, -1);
        }
        return -1;
    }

    /**
     * Returns float value for the given key, -1.0 if no value is found.
     *
     * @param key
     * @return float
     */
    public float getFloat(String key) {
        return context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
                .getFloat(key, -1.0f);
    }

    /**
     * Returns float value for the given key, defaultValue if no value is found.
     *
     * @param key
     * @param defaultValue
     * @return float
     */
    public float getFloat(String key, float defaultValue) {
        if (context != null) {
            return context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
                    .getFloat(key, defaultValue);
        }
        return defaultValue;
    }

    /**
     * Returns int value for the given key, -1 if no value is found.
     *
     * @param key
     * @return int
     */
    public int getInt(String key) {
        return context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
                .getInt(key, -1);
    }

    /**
     * Contains preference name constants. These must be unique.
     */
    public static final class Pref {
        public static final String MAIN = "pref_main";

        public static String[] getAll() {
            return new String[]{MAIN};
        }

        public static String[] getAllPreferenceFileNames() {
            String[] preferencesFilesList = PrefManager.Pref.getAll();
            for (int i = 0; i < preferencesFilesList.length; i++) {
                preferencesFilesList[i] += ".xml";
            }
            return preferencesFilesList;
        }
    }

    /**
     * Contains preference key constants.
     */
    public static final class Key {
        public static final String AUTH_JSON = "auth_json";
    }
}
