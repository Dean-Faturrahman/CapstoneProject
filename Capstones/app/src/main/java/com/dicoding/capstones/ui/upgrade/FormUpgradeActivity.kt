package com.dicoding.capstones.ui.upgrade

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.dicoding.capstones.R
import com.dicoding.capstones.data.UpgradeModel
import com.dicoding.capstones.databinding.ActivityFormUpgradeBinding
import com.dicoding.capstones.helper.Const
import com.dicoding.capstones.helper.PrefHelper
import com.google.firebase.storage.FirebaseStorage
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.functions.Function3
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class FormUpgradeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormUpgradeBinding
    private val upgradeUserViewModel by viewModels<UpgradeUserModel>()
    private lateinit var sharedPref: PrefHelper
    private var imageUri : Uri? = null
    private var downloadUrl : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormUpgradeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = PrefHelper(this)

        actionBarConfig()
        action()
        registerObserver()
        errorObserver()
        validationForm()

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
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
        supportActionBar?.title = "Upgrade Menjadi Guru"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    @SuppressLint("CheckResult")
    private fun validationForm(){
        val nilai = RxTextView.textChanges(binding.inputNilai)
            .skipInitialValue()
            .map { phone ->
                phone.length < 0
            }
        nilai.subscribe {
            showNilaiAlert(it)
        }

        val year = RxTextView.textChanges(binding.inputExp)
            .skipInitialValue()
            .map { name ->
                name.length < 2
            }
        year.subscribe{
            showYearAlert(it)
        }

        val year1 = RxTextView.textChanges(binding.inputExp)
            .skipInitialValue()
            .map { name ->
                name.length < 2
            }
        year1.subscribe{
            showYearAlert(it)
        }


        val invalidFieldsStream = Observable.combineLatest(
            nilai,
            year,
            year1,
            Function3 { nilaiId: Boolean, yearId: Boolean, year1Id: Boolean ->
                !nilaiId && !yearId && !year1Id
            })
        invalidFieldsStream.subscribe { isValid ->
            if (isValid) {
                binding.buttonUpgrade.isEnabled = true
                binding.buttonUpgrade.setBackgroundResource(R.drawable.shape_button)
            } else {
                binding.buttonUpgrade.isEnabled = false
                binding.buttonUpgrade.setBackgroundResource(R.drawable.shape_hide_button)
            }
        }
    }

    private fun showNilaiAlert(isNotValid: Boolean) {
        binding.inputNilai.error = if (isNotValid) getString(R.string.email_not_valid) else null
    }

    private fun showYearAlert(isNotValid: Boolean) {
        binding.inputExp.error = if (isNotValid) getString(R.string.password_not_valid) else null
    }

    fun action() {
        binding.buttonUpgrade.setOnClickListener {
            uploadImage()
        }

        binding.btnImg.setOnClickListener{
//            selectImage()
            startTakePhoto()
        }
    }

        private fun setUpgradeUserData(): UpgradeModel {
            val subject = when (binding.radioGroup.checkedRadioButtonId) {
                binding.radio1.id -> "Matematika"
                binding.radio2.id -> "Biologi"
                binding.radio3.id -> "Fisika"
                binding.radio4.id -> "Bahasa Indonesia"
                binding.radio5.id -> "Bahasa Inggris"
                binding.radio6.id -> "Akuntansi"
                else -> ""
            }

            return UpgradeModel(
                sharedPref.getString(Const.PREF_USERID)!!,
                downloadUrl.toString(),
                binding.inputExp.text.toString().toInt(),
                subject,
                binding.inputNilai.text.toString().toInt()
            )
        }

        private fun upgradeUserService() {
            upgradeUserViewModel.upgradeUser(
                sharedPref.getString(Const.PREF_USERID)!!,
               setUpgradeUserData().idCard.toString(),
                setUpgradeUserData().teachExp.toString().toInt(),
                setUpgradeUserData().subject.toString(),
                setUpgradeUserData().score.toString().toInt()
            )
        }

    private fun uploadImage(){
        val progressBar = ProgressDialog(this)
        progressBar.setMessage("Save Data...")
        progressBar.setCancelable(false)
        progressBar.show()

        val formatDate  = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatDate.format(now)
        val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")

        imageUri?.let {
            storageReference.putFile(imageUri!!).continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                storageReference.downloadUrl

            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    downloadUrl = task.result
                }

            }.addOnSuccessListener {
                binding.imgCard.setImageURI(null)
                upgradeUserService()
                Toast.makeText(this@FormUpgradeActivity, "Berhasil diubah", Toast.LENGTH_SHORT).show()
                if (progressBar.isShowing) progressBar.dismiss()
                toHome()

            }.addOnFailureListener{
                if (progressBar.isShowing) progressBar.dismiss()
                Toast.makeText(this@FormUpgradeActivity, "Failed", Toast.LENGTH_SHORT).show()

            }
        }
    }

//    private fun selectImage() {
//        val intent = Intent()
//        intent.type = "image/*"
//        intent.action = Intent.ACTION_GET_CONTENT
//        startActivityForResult(intent, 100)
//    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val imageBitmap = it.data?.extras?.get("data") as Bitmap
            imageUri = getImageUri(this, imageBitmap)
            Log.e("megi", imageUri.toString())
            binding.imgCard.setImageBitmap(imageBitmap)
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }


    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        launcherIntentCamera.launch(intent)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == 100 && resultCode == RESULT_OK){
//            imageUri = data?.data!!
//            binding.imgCard.setImageURI(imageUri)
//        }
//    }

        private fun registerObserver() {
            upgradeUserViewModel.upgrade.observe(this) {
                if (it.status == 1) {
                    Toast.makeText(this@FormUpgradeActivity, it.message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@FormUpgradeActivity, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun errorObserver() {
            upgradeUserViewModel.errorMessage.observe(this) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        private fun toHome() {
            finish()
        }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}