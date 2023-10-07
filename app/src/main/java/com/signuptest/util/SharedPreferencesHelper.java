package com.signuptest.util;

import android.content.SharedPreferences;

import java.util.Calendar;

/**
 * Helper class to manage access to {@link SharedPreferences}.
 */
public class SharedPreferencesHelper {    // Keys for saving values in SharedPreferences.
    public static final String KEY_NAME = "key_name";
    public static final String KEY_DOB = "key_dob_millis";
    public static final String KEY_EMAIL = "key_email";    // The injected SharedPreferences implementation to use for persistence.
    SharedPreferences mSharedPreferences;

    /**
     * Constructor with dependency injection.
     *
     * @param sharedPreferences The {@link SharedPreferences} that will be used in this DAO.
     */
    public SharedPreferencesHelper(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    /**
     * Saves the given {@link FormDataEntry} that contains the user's settings to
     * {@link SharedPreferences}.
     *
     * @param formDataEntry contains data to save to {@link SharedPreferences}.
     * @return {@code true} if writing to {@link SharedPreferences} succeeded. {@code false}
     * otherwise.
     */
    public boolean savePersonalInfo(FormDataEntry formDataEntry) {
        // Start a SharedPreferences transaction.
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(KEY_NAME, formDataEntry.getName());
        editor.putLong(KEY_DOB, formDataEntry.getDateOfBirth().getTimeInMillis());
        editor.putString(KEY_EMAIL, formDataEntry.getEmail());        // Commit changes to SharedPreferences.
        return editor.commit();
    }

    /**
     * Retrieves the {@link FormDataEntry} containing the user's personal information from
     * {@link SharedPreferences}.
     *
     * @return the Retrieved {@link FormDataEntry}.
     */
    public FormDataEntry getPersonalInfo() {
        // Get data from the SharedPreferences.
        String name = mSharedPreferences.getString(KEY_NAME, "");
        Long dobMillis =
                mSharedPreferences.getLong(KEY_DOB, Calendar.getInstance().getTimeInMillis());
        Calendar dateOfBirth = Calendar.getInstance();
        dateOfBirth.setTimeInMillis(dobMillis);
        String email = mSharedPreferences.getString(KEY_EMAIL, "");        // Create and fill a SharedPreferenceEntry model object.
        return new FormDataEntry(name, dateOfBirth, email);
    }
}