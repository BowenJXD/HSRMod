package hsrmod.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import hsrmod.modcore.HSRMod;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Persistent key-value store backed by a JSON file, analogous to LibGDX Prefs
 * but mod-owned and manually flushed. Call load() to read, save() to write.
 * File is stored at <game_dir>/HSRMod_prefs.json via Gdx.files.local().
 */
public class ModPrefs {
    private static final String SAVE_PATH = "HSRMod_prefs.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static Map<String, Object> data = new HashMap<>();

    public static void load() {
        try {
            FileHandle file = Gdx.files.local(SAVE_PATH);
            if (file.exists()) {
                Type type = new TypeToken<Map<String, Object>>() {}.getType();
                Map<String, Object> loaded = GSON.fromJson(file.readString("UTF-8"), type);
                data = loaded != null ? loaded : new HashMap<>();
            }
        } catch (Exception e) {
            HSRMod.logger.error("ModPrefs: failed to load " + SAVE_PATH, e);
            data = new HashMap<>();
        }
    }

    public static void save() {
        try {
            FileHandle file = Gdx.files.local(SAVE_PATH);
            file.writeString(GSON.toJson(data), false, "UTF-8");
        } catch (Exception e) {
            HSRMod.logger.error("ModPrefs: failed to save " + SAVE_PATH, e);
        }
    }

    // ---- int ----------------------------------------------------------------

    public static int getInteger(String key, int defaultValue) {
        Object val = data.get(key);
        if (val == null) return defaultValue;
        if (val instanceof Number) return ((Number) val).intValue();
        try { return Integer.parseInt(val.toString()); } catch (Exception e) { return defaultValue; }
    }

    public static void putInteger(String key, int value) {
        data.put(key, value);
    }

    // ---- float --------------------------------------------------------------

    public static float getFloat(String key, float defaultValue) {
        Object val = data.get(key);
        if (val == null) return defaultValue;
        if (val instanceof Number) return ((Number) val).floatValue();
        try { return Float.parseFloat(val.toString()); } catch (Exception e) { return defaultValue; }
    }

    public static void putFloat(String key, float value) {
        data.put(key, value);
    }

    // ---- boolean ------------------------------------------------------------

    public static boolean getBoolean(String key, boolean defaultValue) {
        Object val = data.get(key);
        if (val == null) return defaultValue;
        if (val instanceof Boolean) return (Boolean) val;
        return Boolean.parseBoolean(val.toString());
    }

    public static void putBoolean(String key, boolean value) {
        data.put(key, value);
    }

    // ---- String -------------------------------------------------------------

    public static String getString(String key, String defaultValue) {
        Object val = data.get(key);
        return val != null ? val.toString() : defaultValue;
    }

    public static void putString(String key, String value) {
        data.put(key, value);
    }

    // ---- misc ---------------------------------------------------------------

    public static boolean has(String key) {
        return data.containsKey(key);
    }

    public static void remove(String key) {
        data.remove(key);
    }

    public static void clear() {
        data.clear();
    }
}
