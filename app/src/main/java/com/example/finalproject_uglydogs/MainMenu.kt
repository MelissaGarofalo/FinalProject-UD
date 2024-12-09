package com.example.finalproject_uglydogs

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class MainMenu : AppCompatActivity() {

    private lateinit var petsbttn: Button
    private lateinit var votebttn: Button
    private lateinit var leaderboardbttn: Button
    private lateinit var logoutbttn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        petsbttn = findViewById(R.id.petsbttn)
        votebttn = findViewById(R.id.votebttn)
        leaderboardbttn = findViewById(R.id.leaderboardbttn)
        logoutbttn = findViewById(R.id.logoutbttn)

        petsbttn.setOnClickListener {
            val intent = Intent(applicationContext, ListPets::class.java)
            startActivity(intent)
        }
        votebttn.setOnClickListener {
            val intent = Intent(applicationContext, VotePage::class.java)
            startActivity(intent)
        }
        leaderboardbttn.setOnClickListener {
            val intent = Intent(applicationContext, Leaderboard::class.java)
            startActivity(intent)
        }
        logoutbttn.setOnClickListener {
            tokenDel()
            Toast.makeText(this, "Logout Success", Toast.LENGTH_LONG).show()
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun tokenDel() {
        val masterKey = MasterKey.Builder(this)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val SharedPreferences = EncryptedSharedPreferences.create(this, "secured_key", masterKey, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)

        SharedPreferences.edit().remove("auth_token").apply()
        Log.d("Token", "Token removed")
    }
}