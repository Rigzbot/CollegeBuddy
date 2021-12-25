package com.example.collegebuddy.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.collegebuddy.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}