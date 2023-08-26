package com.example.winest_aplication.presentation.actitivties

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.winest_aplication.data.network.PostService
import com.example.winest_aplication.databinding.ActivityCreatePostBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.android.ext.android.inject
import java.io.File
import java.io.FileOutputStream
import java.util.*

class CreatePostActivity : AppCompatActivity() {
    private val apiService: PostService by inject()

    private lateinit var binding: ActivityCreatePostBinding

    private var imageUri: Uri? = null

    private val CAMERA_PERMISSION = 1

    private val CAMERA_REQUEST = 3

    private val FILE_PERMISSION = 2

    private val GALLERY_REQUEST = 4

    private var picBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClickListeners()
    }

    private fun onClickListeners() = with(binding) {
        ivCreatePostAddImage.setOnClickListener {
            selectImage()
        }
        btnCreatePostSend.setOnClickListener {
            createPost()
        }
    }

    private fun selectImage() {
        val options = arrayOf<CharSequence>("Tirar foto", "Escolher da galeria", "Cancelar")
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Adicionar foto!")
        builder.setItems(options) { dialog, item ->
            when {
                options[item] == "Tirar foto" -> {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.CAMERA
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.CAMERA),
                            CAMERA_PERMISSION
                        )
                    } else {
                        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(takePicture, CAMERA_REQUEST)
                    }
                }
                options[item] == "Escolher da galeria" -> {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            FILE_PERMISSION
                        )
                    } else {
                        val pickPhoto =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(pickPhoto, GALLERY_REQUEST)
                    }
                }

                options[item] == "Cancelar" -> dialog.dismiss()
            }
        }
        builder.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(takePicture, CAMERA_REQUEST)
            } else {
                // Toast(applicationContext)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST -> {
                    picBitmap = resizeImage(data!!.extras!!.get("data") as Bitmap)
                    binding.ivCreatePostImagePreview.setImageBitmap(picBitmap)
                    binding.ivCreatePostImagePreview.visibility = View.VISIBLE
                }
                GALLERY_REQUEST -> {
                    val selectedImage = data?.data
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor =
                        contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                    cursor!!.moveToFirst()
                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    val picturePath = cursor.getString(columnIndex)
                    cursor.close()
                    picBitmap = resizeImage(BitmapFactory.decodeFile(picturePath))
                }
            }
            binding.ivCreatePostImagePreview.setImageBitmap(picBitmap)
            binding.ivCreatePostImagePreview.visibility = View.VISIBLE
        }
    }

    private fun createPost() = with(binding) {
        val content =
            edtCreatePostDescription.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        var imagePart: MultipartBody.Part? = null

        if (picBitmap != null) {
            val file = File(externalCacheDir, "image.png")
            file.createNewFile()
            val fos = FileOutputStream(file)
            picBitmap?.compress(Bitmap.CompressFormat.PNG, 90, fos)
            fos.flush()
            fos.close()
            val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.createPost(content, imagePart)
                if (response.isSuccessful) {
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.e("CreatePost", "Error: $response")
                }
            } catch (e: Exception) {
                Log.e("GRTPromptCatch", "Error: $e")
            }
        }
    }

    private fun resizeImage(picBitmal: Bitmap): Bitmap {
        val scale =
            (350.toFloat() / picBitmal.width).coerceAtLeast(350.toFloat() / picBitmal.height)
        val resizedBitmap = Bitmap.createScaledBitmap(
            picBitmal,
            (picBitmal.width * scale).toInt(),
            (picBitmal.height * scale).toInt(),
            true
        )
        val x = (resizedBitmap.width - 350) / 2
        val y = (resizedBitmap.height - 350) / 2
        return Bitmap.createBitmap(resizedBitmap, x, y, 350, 350)
    }
}
