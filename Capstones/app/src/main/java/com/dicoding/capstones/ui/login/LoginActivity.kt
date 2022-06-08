package com.dicoding.capstones.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.dicoding.capstones.HomeActivity2
import com.dicoding.capstones.R
import com.dicoding.capstones.ui.register.RegisterActivity
import com.dicoding.capstones.data.UserLoginModel
import com.dicoding.capstones.databinding.ActivityLoginBinding
import com.dicoding.capstones.helper.Const
import com.dicoding.capstones.helper.PrefHelper
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.functions.Function3


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel>()
    private lateinit var sharedPref: PrefHelper

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = PrefHelper(this)


        validationForm()
        playAnimation()
        setupView()
        setupAction()
        loginObserver()
        errorObserver()
    }

    override fun onStart() {
        super.onStart()
        if (sharedPref.getBoolean(Const.PREF_IS_LOGIN)) {
            toHome()
        }
    }

    @SuppressLint("CheckResult")
    private fun validationForm() {
        val emailStream = RxTextView.textChanges(binding.editEmail)
            .skipInitialValue()
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        emailStream.subscribe {
            showEmailExistAlert(it)
        }

        val passwordStream = RxTextView.textChanges(binding.editPass)
            .skipInitialValue()
            .map { password ->
                password.length < 6
            }
        passwordStream.subscribe {
            showPasswordMinimalAlert(it)
        }

        val invalidFieldsStream = Observable.combineLatest(
            emailStream,
            passwordStream,
            passwordStream,
            Function3 { emailInvalid: Boolean, passwordInvalid: Boolean, passwordConfirmationInvalid: Boolean ->
                !emailInvalid && !passwordInvalid && !passwordConfirmationInvalid
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

    //validation for form
    private fun showEmailExistAlert(isNotValid: Boolean) {
        binding.editEmail.error = if (isNotValid) getString(R.string.email_not_valid) else null
    }
    private fun showPasswordMinimalAlert(isNotValid: Boolean) {
        binding.editPass.error = if (isNotValid) getString(R.string.password_not_valid) else null
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
            loginService()
        }

        binding.daftar.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setLoginData(): UserLoginModel {
        return UserLoginModel(
            binding.editEmail.text.toString(),
            binding.editPass.text.toString()
        )
    }

    private fun saveSession() {
        sharedPref.put(Const.PREF_IS_LOGIN, true)
        sharedPref.put(Const.PREF_USEREMAIL, binding.editEmail.text.toString())
    }

    private fun skipWelcome() {
        val pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE)
        val ed: SharedPreferences.Editor = pref.edit()
            ed.putBoolean("activity_executed", true)
            ed.apply()
    }

    private fun loginService() {
        loginViewModel.login(setLoginData().userEmail.toString(), setLoginData().userPassword.toString())
        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun loginObserver() {
        loginViewModel.login.observe(this) {
            if (it.status == 1) {
                saveSession()
                skipWelcome()
                Toast.makeText(this@LoginActivity, it.message, Toast.LENGTH_SHORT).show()
                toHome()
            } else {
                Toast.makeText(this@LoginActivity, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun errorObserver() {
        loginViewModel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun toHome() {
        val toHome = Intent(this, HomeActivity2::class.java)
        startActivity(toHome)
        finish()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView2, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        val img = ObjectAnimator.ofFloat(binding.imageView2, View.TRANSLATION_X, -30f, 30f).setDuration(600)
        val title = ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 1f).setDuration(300)
        val stittle = ObjectAnimator.ofFloat(binding.textView2, View.ALPHA, 1f).setDuration(300)
        val txtemail = ObjectAnimator.ofFloat(binding.textView3, View.ALPHA, 1f).setDuration(300)
        val email = ObjectAnimator.ofFloat(binding.editEmail, View.ALPHA, 1f).setDuration(300)
        val txtpass = ObjectAnimator.ofFloat(binding.txtPass, View.ALPHA, 1f).setDuration(300)
        val pass = ObjectAnimator.ofFloat(binding.editPass, View.ALPHA, 1f).setDuration(400)
        val buttonlogin = ObjectAnimator.ofFloat(binding.button, View.ALPHA, 1f).setDuration(400)
        val txt4 = ObjectAnimator.ofFloat(binding.textView4, View.ALPHA, 1f).setDuration(400)
        val daftar = ObjectAnimator.ofFloat(binding.daftar, View.ALPHA, 1f).setDuration(400)


        val together = AnimatorSet().apply {
            playTogether( buttonlogin,txt4, daftar)
        }

        AnimatorSet().apply {
            playSequentially(img,title,stittle, txtemail, email, txtpass, pass, together)
            start()
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}