package com.example.winest_aplication.presentation.actitivties

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.winest_aplication.databinding.ActivityCreatePostBinding
import com.example.winest_aplication.data.network.PostService
import com.example.winest_aplication.presentation.uiUtils.GetPath
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.android.ext.android.inject
import java.io.File


class CreatePostActivity : AppCompatActivity() {
    private val apiService: PostService by inject()

    private lateinit var binding: ActivityCreatePostBinding

    private var imageUri: Uri? = null

    private var CAMERA_PERMISSION = 1

    private var FILE_PERMISSION = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSubmitPost.setOnClickListener {
            if (imageUri == null) {
                selectImage()
            } else {
                createPost()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão concedida, você pode usar a câmera
            } else {
                // Permissão negada, informe ao usuário e lide com a situação
            }
        }
    }

    private fun selectImage() {
        val options = arrayOf<CharSequence>("Tirar foto", "Escolher da galeria", "Cancelar")
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Adicionar foto!")
        builder.setItems(options) { dialog, item ->
            when {
                options[item] == "Tirar foto" -> {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION)
                    } else {
                        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(takePicture, 0)
                    }

                }
                options[item] == "Escolher da galeria" -> {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), FILE_PERMISSION)
                    } else {
                        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(pickPhoto, 1)
                    }

                }
                options[item] == "Cancelar" -> dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun createPost() {
        val content = binding.editTextContent.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        // create Image part
        val getPath = GetPath()
        val file = File(getPath.getPath(this, imageUri))
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)

        // launch the coroutine to perform network request
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.createPost(content, imagePart)

            if (response.isSuccessful) {
                Log.d("CreatePost", "Post successfully created.")
                finish() // close this activity and go back to FeedActivity
            } else {
                Log.e("CreatePost", "Error: ${response.code()}")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                imageUri = data?.data
                createPost()
            } else if (requestCode == 1) {
                imageUri = data?.data
                createPost()
            }
        }
    }
}
