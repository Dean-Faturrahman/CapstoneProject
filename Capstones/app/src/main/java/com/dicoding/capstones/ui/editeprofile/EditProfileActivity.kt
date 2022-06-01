package com.dicoding.capstones.ui.editeprofile

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.bumptech.glide.Glide
import com.dicoding.capstones.R
import com.dicoding.capstones.data.UserEditProfileModel
import com.dicoding.capstones.data.UserRegisterModel
import com.dicoding.capstones.databinding.ActivityEditProfileBinding
import com.dicoding.capstones.helper.Const
import com.dicoding.capstones.helper.PrefHelper
import com.dicoding.capstones.ui.register.RegisterViewModel
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.functions.Function4
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.*
import java.util.*

class EditProfileActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityEditProfileBinding
    private val editProfileViewModel by viewModels<EditProfileViewModel>()
    private lateinit var sharedPref: PrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     binding = ActivityEditProfileBinding.inflate(layoutInflater)
     setContentView(binding.root)

        sharedPref = PrefHelper(this)

        actionBarConfig()
        setDataForm()
        editObserver()
        errorObserver()
        setupAction()
        validationForm()
        datePicker()
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> true
        }
    }

    private fun actionBarConfig() {
        val name = intent.getStringExtra(EXTRA_NAME)
        supportActionBar?.title = "Edit profile $name"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    @SuppressLint("CheckResult")
    private fun validationForm(){

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
            passwordStream,
            passwordConfirmationStream,
            phoneStream,
            nameStream,
            Function4 {passwordInvalid: Boolean, passwordConfirmationInvalid: Boolean, phoneInvalid: Boolean, inputnameInvalid: Boolean ->
                 !passwordInvalid && !passwordConfirmationInvalid && !phoneInvalid && !inputnameInvalid
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



    private fun setEditData(): UserEditProfileModel {
        val gender = when (binding.radioGroup.checkedRadioButtonId) {
            binding.radio1.id -> "Male"
            binding.radio2.id -> "Female"
            else -> ""
        }
        return UserEditProfileModel(
            sharedPref.getString(Const.PREF_USERID)!!,
            "https://storage.googleapis.com/cobacobaa/capstone/user.png",
            binding.inputPassword.text.toString(),
            binding.inputNama.text.toString(),
            binding.inputNumber.text.toString(),
            binding.tvDate.text.toString(),
            gender
        )
    }



    private fun editService() {
//        val file = reduceFileImage(getFile as File)
//        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
//        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
//            "photo",
//            file.name,
//            requestImageFile
//        )
        if(binding.inputPassword.text.isNotEmpty()){
            editProfileViewModel.editProfileWithPass(
                sharedPref.getString(Const.PREF_USERID)!!,
                "https://storage.googleapis.com/cobacobaa/capstone/user.png",
                setEditData().userPassword.toString(),
                setEditData().userName.toString(),
                setEditData().userPhone.toString(),
                setEditData().userDob.toString(),
                setEditData().userGender.toString(),
            )
        }else{
            editProfileViewModel.editProfile(
                sharedPref.getString(Const.PREF_USERID)!!,
                "https://storage.googleapis.com/cobacobaa/capstone/user.png",
                setEditData().userName.toString(),
                setEditData().userPhone.toString(),
                setEditData().userDob.toString(),
                setEditData().userGender.toString(),
            )
        }
        editProfileViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

//    fun reduceFileImage(file: File): File {
//        val bitmap = BitmapFactory.decodeFile(file.path)
//        var compressQuality = 100
//        var streamLength: Int
//        do {
//            val bmpStream = ByteArrayOutputStream()
//            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
//            val bmpPicByteArray = bmpStream.toByteArray()
//            streamLength = bmpPicByteArray.size
//            compressQuality -= 5
//        } while (streamLength > 1000000)
//        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
//        return file
//    }

    private fun editObserver() {
        editProfileViewModel.editProfile.observe(this) {
            if (it.status == 1) {
                Toast.makeText(this@EditProfileActivity, it.message, Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this@EditProfileActivity, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun errorObserver() {
        editProfileViewModel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupAction(){
        binding.button.setOnClickListener {
            editService()
        }

        binding.btnEditPhoto.setOnClickListener{
            startGallery()
        }

        binding.imgItemPhoto.setOnClickListener{
            startGallery()
        }
    }

    private fun setDataForm(){
        val name = intent.getStringExtra(EXTRA_NAME)
        val hp = intent.getStringExtra(EXTRA_NOMOR)
        val gender = intent.getStringExtra(EXTRA_GENDER)
        val photo = intent.getStringExtra(EXTRA_PHOTO)
        val date = intent.getStringExtra(EXTRA_DATE)
        val pass = intent.getStringExtra(EXTRA_PASS)

        binding.inputPassword.setText(pass)
        binding.inputNama.setText(name)
        binding.inputNumber.setText(hp)
        binding.tvDate.setText(date)
        Glide.with(this)
            .load(photo)
            .into(binding.imgItemPhoto)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private var getFile: File? = null

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@EditProfileActivity)
            getFile = myFile
            binding.imgItemPhoto.setImageURI(selectedImg)
        }
    }

    fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createTempFile(context.toString())

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    companion object{
        const val EXTRA_USERID = "user_id"
        const val EXTRA_PHOTO = "extra_photo"
        const val EXTRA_NAME  = "extra_name"
        const val EXTRA_NOMOR = "extra_nomor"
        const val EXTRA_GENDER = "extra_gender"
        const val EXTRA_DATE = "extra_gender"
        const val EXTRA_PASS = "extra_pass"
    }
}