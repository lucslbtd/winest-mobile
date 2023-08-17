package com.example.winest_aplication.presentation.actitivties

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.winest_aplication.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setData()
        buttonsActions()
    }

    private fun setData() = with(binding) {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("userName", null)
        val userEmail = sharedPreferences.getString("userEmail", null)
        val userBio = sharedPreferences.getString("userBio", null)
        val userBirthday = sharedPreferences.getString("userBirthday", null)
        val userPhone = sharedPreferences.getString("userPhone", null)

        tvProfileFullNameField.text = userName.toString()
        tvProfileUsernameField.text = userName.toString()
        tvProfileEmailField.text = userEmail.toString()
        tvProfileAboutYouField.text = userBio.toString()
        tvProfileBirthdayField.text = userBirthday.toString()
        tvProfilePhoneField.text = userPhone.toString()
    }

    private fun buttonsActions() = with(binding){
        ivProfileHeaderBack.setOnClickListener {
            finish()
        }
    }
}
