package com.signuptest

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.signuptest.databinding.ActivityMyProfileBinding
import com.signuptest.util.EmailValidator
import com.signuptest.util.FormDataEntry
import com.signuptest.util.SharedPreferencesHelper
import java.util.Calendar

class MyProfileActivity : AppCompatActivity() {
    // Logger for this class.
    private val TAG = "MyProfileActivity" // The helper that manages writing to SharedPreferences.

    private var mSharedPreferencesHelper: SharedPreferencesHelper? =
        null // The input field where the user enters his name.

    private lateinit var mEmailValidator: EmailValidator

    @VisibleForTesting
    lateinit var binding: ActivityMyProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        mEmailValidator = EmailValidator()
        binding.emailInput.addTextChangedListener(mEmailValidator) // Instantiate a SharedPreferencesHelper.
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        mSharedPreferencesHelper =
            SharedPreferencesHelper(sharedPreferences) // Fill input fields from data retrieved from the SharedPreferences.
        populateUi()
    }

    /**
     * Initialize all fields from the personal info saved in the SharedPreferences.
     */
    private fun populateUi() {
        val formDataEntry: FormDataEntry? = mSharedPreferencesHelper?.personalInfo

        binding.userNameInput.setText(formDataEntry?.name)
        val dateOfBirth: Calendar? = formDataEntry?.dateOfBirth
        dateOfBirth?.let {
            binding.dateOfBirthInput.init(
                it.get(Calendar.YEAR), it.get(Calendar.MONTH),
                it.get(Calendar.DAY_OF_MONTH), null
            )
        }

        binding.emailInput.setText(formDataEntry?.email)
    }

    /**
     * Called when the "Save" button is clicked.
     */
    fun onSaveClick(view: View?) {
        // Don't save if the fields do not validate.
        if (!mEmailValidator.isValid) {
            binding.emailInput.error = "Invalid email"
            Log.w(TAG, "Not saving personal information: Invalid email")
            return
        }

        // Get the text from the input fields.
        val name = binding.userNameInput.text.toString()
        val dateOfBirth: Calendar = Calendar.getInstance()
        dateOfBirth.set(
            binding.dateOfBirthInput.year,
            binding.dateOfBirthInput.month,
            binding.dateOfBirthInput.dayOfMonth
        )

        val email = binding.emailInput.text.toString() // Create a Setting model class to persist.

        val formDataEntry = FormDataEntry(
            name,
            dateOfBirth,
            email
        ) // Persist the personal information.

        val isSuccess: Boolean? = mSharedPreferencesHelper?.savePersonalInfo(formDataEntry)
        if (isSuccess == true) {
            view?.let { Snackbar.make(it, "Personal information saved", Toast.LENGTH_LONG).show() }
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