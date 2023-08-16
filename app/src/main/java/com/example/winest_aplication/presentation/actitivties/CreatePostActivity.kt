package com.example.winest_aplication.presentation.actitivties

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
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
import java.text.SimpleDateFormat
import java.util.*

class CreatePostActivity : AppCompatActivity() {
    private val apiService: PostService by inject()

    private lateinit var binding: ActivityCreatePostBinding

    private var imageUri: Uri? = null

    private val CAMERA_PERMISSION = 1

    private val CAMERA_REQUEST = 3

    private val FILE_PERMISSION = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSubmitPost.setOnClickListener {
            selectImage()
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
                        startActivityForResult(pickPhoto, 1)
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
            if (requestCode == CAMERA_REQUEST) {
                val bitMapPic: Bitmap = data!!.extras!!.get("data") as Bitmap
                createPost(bitMapPic)
            }
            /*val file = File(Environment.getExternalStorageDirectory().getPath(), "nomeFoto")

            // Uri of camera image
            imageUri = FileProvider.getUriForFile(
                this,
                this.getApplicationContext().getPackageName() + ".provider",
                file
            )*/
        }
    }

    private fun createPost(picBitmap: Bitmap) {
        val content =
            binding.editTextContent.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        // create Image part
        // Converter o Bitmap em um arquivo temporário
        val file = File(externalCacheDir, "image.png")
        file.createNewFile()
        val fos = FileOutputStream(file)
        picBitmap.compress(Bitmap.CompressFormat.PNG, 90, fos)
        fos.flush()
        fos.close()

        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)

        // launch the coroutine to perform network request
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.createPost(content, imagePart)

            if (response.isSuccessful) {
                val intent = Intent(applicationContext, FeedActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Log.e("CreatePost", "Error: ${response}")
            }
        }
    }


}
