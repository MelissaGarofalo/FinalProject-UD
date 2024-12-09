package com.example.finalproject_uglydogs

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONArray

class Leaderboard : AppCompatActivity() {

    private lateinit var leaderboardTable: TableLayout
    private lateinit var menubttn3: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        leaderboardTable = findViewById(R.id.leaderboardTable)
        menubttn3 = findViewById(R.id.menubttn3)

        menubttn3.setOnClickListener {
            val intent = Intent(applicationContext, MainMenu::class.java)
            startActivity(intent)
        }

        fetchLeaderboardData()
    }

    private fun fetchLeaderboardData() {
        val url = "https://www.jwuclasses.com/ugly/leaderboard"
        val requestQueue = Volley.newRequestQueue(this)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                val petsArray = response.getJSONArray("pets")
                populateLeaderboardTable(petsArray)
            },
            { error ->
                Log.e("Leaderboard", "Error fetching data: ${error.message}")
            }
        )

        requestQueue.add(jsonObjectRequest)
    }

    private fun populateLeaderboardTable(petsArray: JSONArray) {
        for (i in 0 until petsArray.length()) {
            val pet = petsArray.getJSONObject(i)

            val id = pet.getString("id")
            val name = pet.getString("name")
            val rating = pet.getString("rating")
            val votes = pet.getString("votes")
            val imageUrl = pet.getString("url")

            // Create a new table row
            val tableRow = TableRow(this)
            tableRow.gravity = Gravity.CENTER_VERTICAL
            tableRow.setPadding(10, 10, 10, 10)

            // ID column
            val idTextView = TextView(this)
            idTextView.text = id
            idTextView.setPadding(10, 0, 10, 0)
            tableRow.addView(idTextView)

            // Image column
            val imageView = ImageView(this)
            val imageSize = resources.getDimensionPixelSize(R.dimen.image_size)
            imageView.layoutParams = TableRow.LayoutParams(imageSize, imageSize)
            Picasso.get().load(imageUrl).resize(imageSize, imageSize).centerCrop().into(imageView)
            tableRow.addView(imageView)

            // Name column
            val nameTextView = TextView(this)
            nameTextView.text = name
            nameTextView.setPadding(10, 0, 10, 0)
            tableRow.addView(nameTextView)

            // Rating column
            val ratingTextView = TextView(this)
            ratingTextView.text = rating
            ratingTextView.setPadding(10, 0, 10, 0)
            tableRow.addView(ratingTextView)

            // Votes column
            val votesTextView = TextView(this)
            votesTextView.text = votes
            votesTextView.setPadding(10, 0, 10, 0)
            tableRow.addView(votesTextView)

            // Add the row to the table
            leaderboardTable.addView(tableRow)
        }
    }
}
