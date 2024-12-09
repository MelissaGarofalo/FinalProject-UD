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
import org.json.JSONObject

class RegisterPage : AppCompatActivity() {

    private lateinit var backbttn: Button
    private lateinit var registerbttn2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        this.backbttn = findViewById(R.id.backbttn)
        this.registerbttn2 = findViewById(R.id.registerbttn2)

        backbttn.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

        registerbttn2.setOnClickListener {
            val registerEmailText: EditText = findViewById(R.id.registerEmail)
            val registerEmail: String = registerEmailText.text.toString()
            val registerPasswordText: EditText = findViewById(R.id.registerPassword)
            val registerPassword: String = registerPasswordText.text.toString()
            if (registerEmail.isNotEmpty() && registerPassword.isNotEmpty()) {
                registerUser(registerEmail, registerPassword)
            } else {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    val url = "https://www.jwuclasses.com/ugly/register"

    private fun registerUser(registerEmail: String, registerPassword: String) {
        val infoPassed = JSONObject().apply {
            put("email", registerEmail)
            put("password", registerPassword)
        }
        Log.d("RegisterPage", "JSON Payload: $infoPassed")

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, infoPassed, {
            response ->
                val success = response.getInt("success")
                Log.d("Register", "$success")

                if (success == 1) {
                    Toast.makeText(this, "Registration Success", Toast.LENGTH_SHORT).show()
                    Log.d("RegisterPage", "$success")
                    val intent = Intent(applicationContext, LoginPage::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Can now Login", Toast.LENGTH_LONG).show()
                } else {
                    val errorMessage = response.getString("errormessage")
                    Toast.makeText(this, "Registration Failed: $errorMessage", Toast.LENGTH_LONG).show()
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
}