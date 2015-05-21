package com.tvkdevelopment.larrylongbow;

import android.content.SharedPreferences;

/**
 * An enum used to manage preferences.
 */
public enum Pref {

    /** Whether or not advice should be spoken in the main activity */
    TTS("tts", false);

    /** The name of the preferences file */
    private final static String PREF_NAME = "larry";

    /** The object managing the preferences */
    private static SharedPreferences sPreferences;

    /** The key for the preference */
    private final String mKey;
    /** The default boolean value for the preference */
    private final Boolean mDefaultBoolean;
    /** The default integer value for the preference */
    private final Integer mDefaultInt;
    /** The default long value for the preference */
    private final Long mDefaultLong;
    /** The default string value for the preference */
    private final String mDefaultString;

    private Pref(final String key, final boolean defaultValue) {
        mKey = key;
        mDefaultBoolean = defaultValue;
        mDefaultInt = null;
        mDefaultLong = null;
        mDefaultString = null;
    }

    private Pref(final String key, final int defaultValue) {
        mKey = key;
        mDefaultBoolean = null;
        mDefaultInt = defaultValue;
        mDefaultLong = null;
        mDefaultString = null;
    }

    private Pref(final String key, final long defaultValue) {
        mKey = key;
        mDefaultBoolean = null;
        mDefaultInt = null;
        mDefaultLong = defaultValue;
        mDefaultString = null;
    }

    private Pref(final String key, final String defaultValue) {
        mKey = key;
        mDefaultBoolean = null;
        mDefaultInt = null;
        mDefaultLong = null;
        mDefaultString = defaultValue;
    }

    /**
     * Saves the preference value persistently for the user.
     *
     * @param value
     *            The value to save for this preference
     */
    public void put(final boolean value) {
        final SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(mKey, String.valueOf(value));
        editor.commit();
    }

    /**
     * Saves the preference value persistently for the user.
     *
     * @param value
     *            The value to save for this preference
     */
    public void put(final int value) {
        final SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(mKey, String.valueOf(value));
        editor.commit();
    }

    /**
     * Saves the preference value persistently for the user.
     *
     * @param value
     *            The value to save for this preference
     */
    public void put(final long value) {
        final SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(mKey, String.valueOf(value));
        editor.commit();
    }

    /**
     * Saves the preference value persistently for the user.
     *
     * @param value
     *            The value to save for this preference
     */
    public void put(final String value) {
        final SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(mKey, value);
        editor.commit();
    }

    /**
     * Retrieves a saved preference value.
     *
     * @return The saved value or a default one if it has not been put
     */
    public boolean getBoolean() {
        final String value = getPreferences().getString(mKey, null);
        if (value == null) {
            return mDefaultBoolean;
        } else {
            return Boolean.parseBoolean(value);
        }
    }

    /**
     * Retrieves a saved preference value.
     *
     * @return The saved value or a default one if it has not been put
     */
    public int getInteger() {
        final String value = getPreferences().getString(mKey, null);
        if (value == null) {
            return mDefaultInt;
        } else {
            return Integer.parseInt(value);
        }
    }

    /**
     * Retrieves a saved preference value.
     *
     * @return The saved value or a default one if it has not been put
     */
    public long getLong() {
        final String value = getPreferences().getString(mKey, null);
        if (value == null) {
            return mDefaultLong;
        } else {
            return Long.parseLong(value);
        }
    }

    /**
     * Retrieves a saved preference value.
     *
     * @return The saved value or a default one if it has not been put
     */
    public String getString() {
        final String value = getPreferences().getString(mKey, null);
        if (value == null) {
            return mDefaultString;
        } else {
            return value;
        }
    }

    /**
     * Resets the value to the default.
     */
    public void reset() {
        final SharedPreferences.Editor editor = getPreferences().edit();
        editor.remove(mKey);
        editor.commit();
    }

    /**
     * Retrieves the preferences manager.
     *
     * @param context
     *            The context to use to access the preferences
     *
     * @return The preferences manager
     */
    private static SharedPreferences getPreferences() {
        if (sPreferences == null) {
            sPreferences = App.getContext().getSharedPreferences(PREF_NAME, 0);
        }
        return sPreferences;
    }

}
