package com.dicoding.capstones.ui.register

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dicoding.capstones.R
import com.dicoding.capstones.data.UserRegisterModel
import com.dicoding.capstones.databinding.ActivityRegisterBinding
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.functions.Function5
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel>()

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        validationForm()
        setupView()
        setupAction()
        datePicker()
        registerObserver()
        errorObserver()
    }

    @SuppressLint("CheckResult")
    private fun validationForm(){
        val emailStream = RxTextView.textChanges(binding.inputEmail)
            .skipInitialValue()
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        emailStream.subscribe {
            showEmailExistAlert(it)
        }

        val nameStream = RxTextView.textChanges(binding.inputNama)
            .skipInitialValue()
            .map { name ->
               name.length < 2
            }
        nameStream.subscribe{
            showNameAlert(it)
        }

        val passwordStream = RxTextView.textChanges(binding.inputPassword)
            .skipInitialValue()
            .map { password ->
                password.length < 8
            }
        passwordStream.subscribe {
            showPasswordMinimalAlert(it)
        }

        val passwordConfirmationStream = Observable.merge(
            RxTextView.textChanges(binding.inputPassword)
                .map { password ->
                    password.toString() != binding.inputPassConf.text.toString()
                },
            RxTextView.textChanges(binding.inputPassConf)
                .map { confirmPassword ->
                    confirmPassword.toString() != binding.inputPassword.text.toString()
                }
        )
        passwordConfirmationStream.subscribe {
            showPasswordConfirmationAlert(it)
        }

        val phoneStream = RxTextView.textChanges(binding.inputNumber)
            .skipInitialValue()
            .map { phone ->
                !Patterns.PHONE.matcher(phone).matches()
            }
        phoneStream.subscribe {
            showPhoneAlert(it)
        }

        val invalidFieldsStream = Observable.combineLatest(
            emailStream,
            passwordStream,
            passwordConfirmationStream,
            phoneStream,
            nameStream,
            Function5 { emailInvalid: Boolean, passwordInvalid: Boolean, passwordConfirmationInvalid: Boolean, phoneInvalid: Boolean, inputnameInvalid: Boolean ->
                !emailInvalid && !passwordInvalid && !passwordConfirmationInvalid && !phoneInvalid && !inputnameInvalid
            })
        invalidFieldsStream.subscribe { isValid ->
            if (isValid) {
                binding.button.isEnabled = true
                binding.button.setBackgroundResource(R.drawable.shape_button)
            } else {
                binding.button.isEnabled = false
                binding.button.setBackgroundResource(R.drawable.shape_hide_button)
            }
        }
    }

    private fun showEmailExistAlert(isNotValid: Boolean) {
        binding.inputEmail.error = if (isNotValid) getString(R.string.email_not_valid) else null
    }

    private fun showPasswordMinimalAlert(isNotValid: Boolean) {
        binding.inputPassword.error = if (isNotValid) getString(R.string.password_not_valid) else null
    }

    private fun showPasswordConfirmationAlert(isNotValid: Boolean) {
        binding.inputPassConf.error = if (isNotValid) getString(R.string.password_not_same) else null
    }
    private fun showPhoneAlert(isNotValid: Boolean) {
        binding.inputNumber.error = if (isNotValid) getString(R.string.not_phone) else null
    }
    private fun showNameAlert(isNotValid: Boolean) {
        binding.inputNama.error = if (isNotValid) getString(R.string.validate_name) else null
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction(){
        binding.button.setOnClickListener {
            registerService()
        }

        binding.tvToLogin.setOnClickListener {
            toLogin()
        }
    }

    private fun datePicker() {
        val today = Calendar.getInstance()
        binding.datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { _, year, month, day ->
            val month = month + 1
            val msg = "$year-$month-$day"
            binding.tvDate.text = msg
        }
    }

    private fun setRegisterData(): UserRegisterModel {
        val gender = when (binding.radioGroup.checkedRadioButtonId) {
            binding.radio1.id -> "Male"
            binding.radio2.id -> "Female"
            else -> ""
        }
        return UserRegisterModel(
            binding.inputEmail.text.toString(),
            binding.inputPassConf.text.toString(),
            binding.inputNama.text.toString(),
            binding.inputNumber.text.toString(),
            binding.tvDate.text.toString(),
            gender
        )
    }

    private fun registerService() {
        registerViewModel.register(
            setRegisterData().userEmail.toString(),
            setRegisterData().userPassword.toString(),
            setRegisterData().userName.toString(),
            setRegisterData().userPhone.toString(),
            setRegisterData().userDob.toString(),
            setRegisterData().userGender.toString(),
        )
        registerViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun registerObserver() {
        registerViewModel.register.observe(this) {
            if (it.status == 1) {
                Toast.makeText(this@RegisterActivity, it.message, Toast.LENGTH_SHORT).show()
                toLogin()
            } else {
                Toast.makeText(this@RegisterActivity, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun errorObserver() {
        registerViewModel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun toLogin() {
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}