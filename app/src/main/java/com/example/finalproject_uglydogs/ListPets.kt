package com.example.finalproject_uglydogs

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.squareup.picasso.Picasso
import org.json.JSONArray

class ListPets : AppCompatActivity() {
    private lateinit var menubttn2: Button
    private lateinit var addpetbttn: Button
    private lateinit var deletepetbttn: Button
    private lateinit var petTable: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list_pets)

        // Initialize the UI elements
        menubttn2 = findViewById(R.id.menubttn2)
        addpetbttn = findViewById(R.id.addpetbttn)
        deletepetbttn = findViewById(R.id.deletepetbttn)
        petTable = findViewById(R.id.petTable)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Button actions
        menubttn2.setOnClickListener {
            val intent = Intent(applicationContext, MainMenu::class.java)
            startActivity(intent)
        }

        addpetbttn.setOnClickListener {
            val intent = Intent(applicationContext, AddPet::class.java)
            startActivity(intent)
        }

        deletepetbttn.setOnClickListener {
            val intent = Intent(applicationContext, DeletePet::class.java)
            startActivity(intent)
        }

        // Fetch the pet list from SharedPreferences
        fetchPetList()
    }

    private fun fetchPetList() {
        val sharedPreferences = getSharedPreferences("pet_database", MODE_PRIVATE)
        val petListData = sharedPreferences.getString("pet_list", null)

        if (petListData == null) {
            Toast.makeText(this, "No pets found", Toast.LENGTH_SHORT).show()
            return
        }

        val petArray = JSONArray(petListData)
        populateTable(petArray)
    }

    private fun populateTable(petsArray: JSONArray) {
        petTable.removeAllViews()

        // Add header row to the table
        val headerRow = TableRow(this).apply {
            addView(createTextView("ID", true))
            addView(createTextView("Image", true))
            addView(createTextView("Name", true))
            addView(createTextView("Description", true))
        }
        petTable.addView(headerRow)

        // Create rows for each pet
        for (i in 0 until petsArray.length()) {
            val pet = petsArray.getJSONObject(i)
            val tableRow = TableRow(this).apply {
                addView(createTextView((i + 1).toString()))
                addView(createImageView(pet.getString("link")))
                addView(createTextView(pet.getString("name")))
                addView(createTextView(pet.getString("description")))
            }
            petTable.addView(tableRow)
        }
    }

    // Create text view with header
    private fun createTextView(text: String, isHeader: Boolean = false): TextView {
        return TextView(this).apply {
            this.text = text
            setPadding(16, 16, 16, 16)
            if (isHeader) {
                setTextColor(resources.getColor(android.R.color.black))
                textSize = 18f
            }
        }
    }

    // Create image view from URL using picasso
    private fun createImageView(imageUrl: String): ImageView {
        val imageView = ImageView(this)

        // Load the image
        Picasso.get().load(imageUrl).into(imageView)

        // Set default layout parameters
        val defaultParams = TableRow.LayoutParams(100, 100)
        imageView.layoutParams = defaultParams

        // Set click listener for zoom
        imageView.setOnClickListener {
            // increase size on click
            val zoomedParams = TableRow.LayoutParams(500, 500)
            imageView.layoutParams = zoomedParams

            Toast.makeText(this, "Tap the image again to return to normal size.", Toast.LENGTH_SHORT).show()

            // Restore to original size on second click
            imageView.setOnClickListener {
                imageView.layoutParams = defaultParams
            }
        }

        return imageView
    }
}
