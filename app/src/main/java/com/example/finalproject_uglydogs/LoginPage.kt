package com.example.finalproject_uglydogs

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import org.json.JSONObject

class LoginPage : AppCompatActivity() {

    private lateinit var backbttn2: Button
    private lateinit var loginbttn2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        backbttn2 = findViewById(R.id.backbttn2)
        loginbttn2 = findViewById(R.id.loginbttn2)

        backbttn2.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

        loginbttn2.setOnClickListener {
            val loginEmailText: EditText = findViewById(R.id.loginEmail)
            val loginEmail = loginEmailText.text.toString()
            val loginPasswordText: EditText = findViewById(R.id.loginPassword)
            val loginPassword = loginPasswordText.text.toString()
            if (loginEmail.isNotEmpty() && loginPassword.isNotEmpty()) {
                loginUser(loginEmail, loginPassword)
            } else {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun loginUser(loginEmail: String, loginPassword: String) {
        val url = "https://www.jwuclasses.com/ugly/login"
        val infoPassed = JSONObject().apply {
            put("email", loginEmail)
            put("password", loginPassword)
        }
        Log.d("LoginPage", "infoPassed: $infoPassed")

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, null, {
            response ->
            val success = response.getInt("success")

            if (success == 1) {
                val token = response.getString("token")
                saveToken(token)
                Toast.makeText(this, "Login successful!", Toast.LENGTH_LONG).show()
                val intent = Intent(applicationContext, MainMenu::class.java)
                startActivity(intent)
            } else {
                val errorMessage = response.getString("errormessage")
                Toast.makeText(this, "Login failed: $errorMessage", Toast.LENGTH_LONG).show()
                Log.d("LoginPage", "Login failed: $errorMessage")
            }

        },
            { error ->
            error.printStackTrace()
            Toast.makeText(this, "Error: ${error.message ?: "Unknown error"}", Toast.LENGTH_LONG).show()
            }
        )

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(jsonObjectRequest)
    }

    private fun saveToken(token: String) {
        val masterKey = MasterKey.Builder(this)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPreferences = EncryptedSharedPreferences.create(this, "secured_key", masterKey, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)

        sharedPreferences.edit().putString("token",token).apply()

        Log.d("LoginPage", "Token saved: $token")
    }
}
