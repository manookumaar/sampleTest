package com.signuptest

import android.content.SharedPreferences
import com.signuptest.util.FormDataEntry
import com.signuptest.util.SharedPreferencesHelper
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Matchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.runners.MockitoJUnitRunner
import java.util.Calendar

/**
 * Unit tests for the [SharedPreferencesHelper] that mocks [SharedPreferences].
 */
@RunWith(MockitoJUnitRunner::class)
class SharedPreferencesHelperTest {
    private var mFormDataEntry: FormDataEntry? = null
    private var mMockSharedPreferencesHelper: SharedPreferencesHelper? = null
    //private var mMockBrokenSharedPreferencesHelper: SharedPreferencesHelper? = null

    @Mock
    var mMockSharedPreferences: SharedPreferences? = null

    @Mock
    var mMockEditor: SharedPreferences.Editor? = null

    @Before
    fun initMocks() {
        // Create SharedPreferenceEntry to persist.
        mFormDataEntry = FormDataEntry(
            TEST_NAME,
            TEST_DATE_OF_BIRTH,
            TEST_EMAIL
        )

        mMockSharedPreferencesHelper = createMockSharedPreference() // Create a mocked SharedPreferences that fails at saving data.
    }

    @Test
    fun sharedPreferencesHelper_SaveAndReadPersonalInformation() {
        // Save the personal information to SharedPreferences
        val success = mMockSharedPreferencesHelper?.savePersonalInfo(mFormDataEntry)

        Mockito.verify(mMockEditor, times(1))?.commit()

        MatcherAssert.assertThat(
            "Checking that SharedPreferenceEntry.save... returns true",
            success,
            CoreMatchers.`is`(true)
        ) // Read personal information from SharedPreferences
        val savedSharedPreferenceEntry = mMockSharedPreferencesHelper?.personalInfo // Make sure both written and retrieved personal information are equal.

        MatcherAssert.assertThat(
            "Checking that SharedPreferenceEntry.name has been persisted and read correctly",
            mFormDataEntry?.name,
            CoreMatchers.`is`(
                CoreMatchers.equalTo(savedSharedPreferenceEntry?.name)
            )
        )
        MatcherAssert.assertThat(
            "Checking that SharedPreferenceEntry.dateOfBirth has been persisted and read "
                    + "correctly",
            mFormDataEntry?.dateOfBirth,
            CoreMatchers.`is`(CoreMatchers.equalTo(savedSharedPreferenceEntry?.dateOfBirth))
        )
        MatcherAssert.assertThat(
            "Checking that SharedPreferenceEntry.email has been persisted and read "
                    + "correctly",
            mFormDataEntry?.email,
            CoreMatchers.`is`(CoreMatchers.equalTo(savedSharedPreferenceEntry?.email))
        )
    }

    /**
     * Creates a mocked SharedPreferences.
     */
    private fun createMockSharedPreference(): SharedPreferencesHelper {
        // Mocking reading the SharedPreferences as if mMockSharedPreferences was previously written
        // correctly.
        Mockito.`when`(
            mMockSharedPreferences?.getString(
                Matchers.eq(SharedPreferencesHelper.KEY_NAME),
                Matchers.anyString()
            )).thenReturn(mFormDataEntry?.name)

        Mockito.`when`(
            mMockSharedPreferences?.getString(
                Matchers.eq(SharedPreferencesHelper.KEY_EMAIL),
                Matchers.anyString()
            )
        ).thenReturn(mFormDataEntry?.email)

        Mockito.`when`(
            mMockSharedPreferences?.getLong(
                Matchers.eq(SharedPreferencesHelper.KEY_DOB),
                Matchers.anyLong()
            )
        ).thenReturn(mFormDataEntry?.dateOfBirth?.timeInMillis)/// Mocking a successful commit.


        Mockito.`when`(mMockSharedPreferences?.edit()).thenReturn(mMockEditor)
        Mockito.`when`(mMockEditor?.commit()).thenReturn(true) // Return the MockEditor when requesting it.

        return SharedPreferencesHelper(mMockSharedPreferences)
    }

    companion object {
        private const val TEST_NAME = "Test name"
        private const val TEST_EMAIL = "test@email.com"
        private val TEST_DATE_OF_BIRTH = Calendar.getInstance()

        init {
            TEST_DATE_OF_BIRTH[1980, 1] = 1
        }
    }
}