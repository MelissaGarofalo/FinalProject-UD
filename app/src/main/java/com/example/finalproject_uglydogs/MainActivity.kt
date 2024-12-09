package com.example.finalproject_uglydogs

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var loginbttn: Button
    private lateinit var registerbttn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loginbttn = findViewById(R.id.loginbttn)
        registerbttn = findViewById(R.id.registerbttn)

        findViewById<Button>(R.id.loginbttn).setOnClickListener {
            val intent = Intent(applicationContext, LoginPage::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.registerbttn).setOnClickListener {
            val intent = Intent(applicationContext, RegisterPage::class.java)
            startActivity(intent)

        }
    }
}