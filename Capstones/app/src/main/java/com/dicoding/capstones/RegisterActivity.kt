package com.dicoding.capstones
import io.reactivex.functions.Function3

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.WindowInsets
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.dicoding.capstones.databinding.ActivityRegisterBinding
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.functions.Function4
import kotlinx.android.synthetic.main.nav.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vadidationForm()
        setupView()
        setupAction()
    }

    @SuppressLint("CheckResult")
    private fun vadidationForm(){
        val emailStream = RxTextView.textChanges(binding.inputEmail)
            .skipInitialValue()
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        emailStream.subscribe {
            showEmailExistAlert(it)
        }

        val passwordStream = RxTextView.textChanges(binding.inputPassword)
            .skipInitialValue()
            .map { password ->
                password.length < 6
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
            Function4 { emailInvalid: Boolean, passwordInvalid: Boolean, passwordConfirmationInvalid: Boolean, phoneInvalid: Boolean ->
                !emailInvalid && !passwordInvalid && !passwordConfirmationInvalid && !phoneInvalid
            })
        invalidFieldsStream.subscribe { isValid ->
            if (isValid) {
                binding.button.isEnabled = true
                binding.button.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_200))
            } else {
                binding.button.isEnabled = false
                binding.button.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray))
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
        binding.inputNumber.error = if (isNotValid) getString(R.string.password_not_same) else null
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
        binding.daftar.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}