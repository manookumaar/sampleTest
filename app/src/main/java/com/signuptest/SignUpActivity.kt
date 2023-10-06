package com.signuptest

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.signuptest.databinding.ActivitySignUpBinding
import com.signuptest.util.EmailValidator
import com.signuptest.util.SharedPreferenceEntry
import com.signuptest.util.SharedPreferencesHelper
import java.util.Calendar

class SignUpActivity : AppCompatActivity() {
    // Logger for this class.
    private val TAG = "SignUpActivity" // The helper that manages writing to SharedPreferences.

    private var mSharedPreferencesHelper: SharedPreferencesHelper? = null // The input field where the user enters his name.

    private var mNameText: EditText? = null // The date picker where the user enters his date of birth.

    private var mDobPicker: DatePicker? = null // The input field where the user enters his email.

    private var mEmailText: EditText? = null // The validator for the email input field.

    private lateinit var mEmailValidator: EmailValidator

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root) // Shortcuts to input fields.
        mNameText = binding.userNameInput//findViewById<View>(R.id.userNameInput) as EditText
        mDobPicker = binding.dateOfBirthInput //findViewById<View>(R.id.dateOfBirthInput) as DatePicker
        mEmailText = binding.emailInput //findViewById<View>(R.id.emailInput) as EditText // Setup field validators.
        mEmailValidator = EmailValidator()
        mEmailText!!.addTextChangedListener(mEmailValidator) // Instantiate a SharedPreferencesHelper.
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        mSharedPreferencesHelper =
            SharedPreferencesHelper(sharedPreferences) // Fill input fields from data retrieved from the SharedPreferences.
        populateUi()
    }

    /**
     * Initialize all fields from the personal info saved in the SharedPreferences.
     */
    private fun populateUi() {
        val sharedPreferenceEntry: SharedPreferenceEntry? = mSharedPreferencesHelper?.getPersonalInfo()

        mNameText?.setText(sharedPreferenceEntry?.getName())
        val dateOfBirth: Calendar? = sharedPreferenceEntry?.getDateOfBirth()
        dateOfBirth?.let {
            mDobPicker?.init(
                it.get(Calendar.YEAR), it.get(Calendar.MONTH),
                it.get(Calendar.DAY_OF_MONTH), null
            )
        }

        mEmailText?.setText(sharedPreferenceEntry?.getEmail())
    }

    /**
     * Called when the "Save" button is clicked.
     */
    fun onSaveClick(view: View?) {
        // Don't save if the fields do not validate.
        if (!mEmailValidator.isValid()) {
            mEmailText!!.error = "Invalid email"
            Log.w(TAG, "Not saving personal information: Invalid email")
            return
        } // Get the text from the input fields.
        val name = mNameText!!.text.toString()
        val dateOfBirth: Calendar = Calendar.getInstance()
        dateOfBirth.set(mDobPicker!!.year, mDobPicker!!.month, mDobPicker!!.dayOfMonth)
        val email = mEmailText!!.text.toString() // Create a Setting model class to persist.
        val sharedPreferenceEntry =
            SharedPreferenceEntry(name, dateOfBirth, email) // Persist the personal information.
        val isSuccess: Boolean? = mSharedPreferencesHelper?.savePersonalInfo(sharedPreferenceEntry)
        if (isSuccess == true) {
            Toast.makeText(this, "Personal information saved", Toast.LENGTH_LONG).show()
            Log.i(TAG, "Personal information saved")
        } else {
            Log.e(TAG, "Failed to write personal information to SharedPreferences")
        }
    }

    /**
     * Called when the "Revert" button is clicked.
     */
    fun onRevertClick(view: View?) {
        populateUi()
        Toast.makeText(this, "Personal information reverted", Toast.LENGTH_LONG).show()
        Log.i(TAG, "Personal information reverted")
    }
}