package com.example.finalproject_uglydogs

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONArray

class DeletePet : AppCompatActivity() {

    private lateinit var deletebttn: Button
    private lateinit var backbttn4: Button
    private lateinit var petId: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_delete_pet)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        deletebttn = findViewById(R.id.deletebttn)
        backbttn4 = findViewById(R.id.backbttn4)
        petId = findViewById(R.id.petId)


        deletebttn.setOnClickListener {
            val petIdInput = petId.text.toString()
            deletePetById(petIdInput.toInt())
        }

        backbttn4.setOnClickListener {
            val intent = Intent(applicationContext, ListPets::class.java)
            startActivity(intent)
        }
    }

    private fun deletePetById(petId: Int) {
        val sharedPreferences = getSharedPreferences("pet_database", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Get existing pets list
        val existingPetData = sharedPreferences.getString("pet_list", null)
        if (existingPetData == null) {
            Toast.makeText(this, "No pets found to delete", Toast.LENGTH_SHORT).show()
            return
        }

        val petsArray = JSONArray(existingPetData)
        if (petId <= 0 || petId > petsArray.length()) {
            Toast.makeText(this, "Invalid Pet ID", Toast.LENGTH_SHORT).show()
            return
        }

        // Remove the pet at ID
        petsArray.remove(petId - 1)

        // Save the updated list of pets back to SharedPreferences
        editor.putString("pet_list", petsArray.toString())
        editor.apply()

        // Show a message and return to pet list
        Toast.makeText(this, "Pet Deleted Successfully", Toast.LENGTH_LONG).show()
        val intent = Intent(this, ListPets::class.java)
        startActivity(intent)
    }
}
