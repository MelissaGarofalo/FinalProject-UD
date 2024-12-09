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
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import org.json.JSONArray
import org.json.JSONObject

class AddPet : AppCompatActivity() {
    private lateinit var addbttn: Button
    private lateinit var backbttn3: Button

    private lateinit var petLink: EditText
    private lateinit var petName: EditText
    private lateinit var petDescription: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_pet)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        this.addbttn = findViewById(R.id.addbttn)
        this.backbttn3 = findViewById(R.id.backbttn3)

        this.petLink = findViewById(R.id.petLink)
        this.petName = findViewById(R.id.petName)
        this.petDescription = findViewById(R.id.petDescription)

        addbttn.setOnClickListener {
            val link = petLink.text.toString()
            val name = petName.text.toString()
            val description = petDescription.text.toString()

            val pet = Pet(link, name, description)
            registerPet(pet)

            // Log the pet added and move to the pet list
            Log.d("AddPet", "Pet added: $pet")
            Toast.makeText(this, "Pet added successfully", Toast.LENGTH_LONG).show()
            val listIntent = Intent(applicationContext, ListPets::class.java)
            startActivity(listIntent)
        }

        backbttn3.setOnClickListener {
            val intent = Intent(applicationContext, ListPets::class.java)
            startActivity(intent)
        }
    }

    // :(
    private fun getToken(): String? {
        val masterKey = MasterKey.Builder(this)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val sharedPreferences = EncryptedSharedPreferences.create(
            this,
            "secured_key",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        return sharedPreferences.getString("token", null)
    }

    private fun registerPet(pet: Pet) {
        // Create a JSONObject with the pet's data
        val petData = JSONObject().apply {
            put("link", pet.link)
            put("name", pet.name)
            put("description", pet.description)
        }

        // Retrieve the existing pet list from SharedPreferences, or create a new one
        val sharedPreferences = getSharedPreferences("pet_database", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val existingPetData = sharedPreferences.getString("pet_list", null)
        val petArray = if (existingPetData != null) {
            JSONArray(existingPetData)
        } else {
            JSONArray() // Start with an empty list if no pets exist yet
        }

        // Add the new pet data to the existing list
        petArray.put(petData)

        // Save the updated pet list back to SharedPreferences
        editor.putString("pet_list", petArray.toString())
        editor.apply()
    }
}
