package com.example.finalproject_uglydogs

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONObject

// :( didn't work

class VotePage : AppCompatActivity() {

    private lateinit var petImage: ImageView
    private lateinit var petName: TextView
    private lateinit var petBirthdate: TextView
    private lateinit var voteInput: EditText
    private lateinit var submitVoteButton: Button
    private lateinit var menubttn: Button

    private var petId: String? = null
    private val getVoteUrl = "https://www.jwuclasses.com/ugly/getvote"
    private val saveVoteUrl = "https://www.jwuclasses.com/ugly/savevote"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_vote_page)

        petImage = findViewById(R.id.petImage)
        petName = findViewById(R.id.petName)
        petBirthdate = findViewById(R.id.petBirthdate)
        voteInput = findViewById(R.id.voteInput)
        submitVoteButton = findViewById(R.id.submitVoteButton)
        menubttn = findViewById(R.id.menubttn)

        loadPet()

        submitVoteButton.setOnClickListener {
            val vote = voteInput.text.toString().toIntOrNull()
            if (vote == null || vote !in 1..5) {
                Toast.makeText(this, "Please enter a number between 1 and 5.", Toast.LENGTH_SHORT).show()
            } else {
                submitVote(vote)
            }
        }

        menubttn.setOnClickListener {
            val intent = Intent(applicationContext, MainMenu::class.java)
            startActivity(intent)
        }
    }

    private fun loadPet() {
        val queue = Volley.newRequestQueue(this)

        val request = JsonObjectRequest(Request.Method.GET, getVoteUrl, null, { response ->
            try {
                val petJson = response.getJSONObject("pet")

                val id = petJson.optString("id", "Unknown ID")
                val name = petJson.optString("name", "Unknown Name")
                val birthdate = petJson.optString("birthdate", "Unknown Birthdate")
                val url = petJson.optString("image_url", "")
                val pet = Pet2(id, name, birthdate, url)

                petId = pet.id
                petName.text = pet.name
                petBirthdate.text = pet.birthdate
                if (url.isNotEmpty()) {
                    Picasso.get().load(pet.url).into(petImage)
                } else {
                    petImage.setImageResource(R.drawable.placeholder_image) // Placeholder for missing images
                }

            } catch (e: Exception) {
                Toast.makeText(this, "Error loading pet data.", Toast.LENGTH_SHORT).show()
                Log.e("VotePage", "Error parsing pet data: ${e.message}")
            }
        }, { error ->
            Toast.makeText(this, "Failed to load pet data.", Toast.LENGTH_SHORT).show()
            Log.e("VotePage", "Network Error: ${error.message}")
        })

        queue.add(request)
    }


    private fun submitVote(rating: Int) {
        val token = getToken()
        if (token.isNullOrEmpty() || petId == null) {
            Toast.makeText(this, "Unable to submit vote. Try again later.", Toast.LENGTH_SHORT).show()
            return
        }

        val queue = Volley.newRequestQueue(this)

        val voteData = JSONObject().apply {
            put("token", token)
            put("pet_id", petId)
            put("rating", rating)
        }

        val request = JsonObjectRequest(Request.Method.POST, saveVoteUrl, voteData, { response ->
            Toast.makeText(this, "Vote submitted successfully!", Toast.LENGTH_SHORT).show()
            finish()
        }, { error ->
            Toast.makeText(this, "Failed to submit vote.", Toast.LENGTH_SHORT).show()
            Log.e("VotePage", "Error: ${error.message}")
        })

        queue.add(request)
    }

    private fun getToken(): String? {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        return sharedPreferences.getString("token", null)
    }
}
