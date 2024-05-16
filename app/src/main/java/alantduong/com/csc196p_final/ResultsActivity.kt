package alantduong.com.csc196p_final

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ResultsActivity : AppCompatActivity()  {

    private lateinit var resultTextView: TextView
    private lateinit var highScoreTextView: TextView
    private lateinit var tryButton: Button
    private lateinit var exitButton: Button
    private lateinit var deleteButton: Button
    private var deleteButtonClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.results_main)

        val db = Firebase.firestore
        tryButton = findViewById(R.id.tryButton)
        exitButton = findViewById(R.id.exitButton)
        deleteButton = findViewById(R.id.deleteButton)

        highScoreTextView = findViewById(R.id.highScoreTextView)

        db.collection("scores")
            .orderBy("averageReflexTime", Query.Direction.ASCENDING)
            .limit(7)
            .get()
            .addOnSuccessListener { documents ->
                val highScores = StringBuilder()
                for (document in documents) {
                    val email = document.getString("email") ?: "Unknown User"
                    val averageReflexTime = document.getLong("averageReflexTime") ?: 0L
                    val scoreText = "$email - $averageReflexTime ms\n"
                    highScores.append(scoreText)
                }
                // Display high scores in TextView
                highScoreTextView.text = highScores.toString()
            }
            .addOnFailureListener { e ->
                // Handle errors
                highScoreTextView.text = "Error fetching high scores: ${e.message}"
            }

        val averageReflexTime = intent.getLongExtra("averageReflexTime", 0L)

        resultTextView = findViewById(R.id.resultTextView)
        resultTextView.text = "Average Reflex Time: $averageReflexTime ms"


        tryButton.setOnClickListener {
            // Move to Instructions Activity
            startActivity(Intent(this, InstructionsActivity::class.java))
        }

        exitButton.setOnClickListener {
            // Exit the game
            finishAffinity()
        }

        deleteButton.setOnClickListener {
            if (!deleteButtonClicked) {
                deleteButtonClicked = true
                deleteMostRecentScore()
            } else {
                Toast.makeText(this, "You've already deleted your score!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun deleteMostRecentScore() {
        val scoresRef = Firebase.firestore.collection("scores")

        // Query for the most recent score document
        scoresRef.orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Check if there are any documents returned
                if (!querySnapshot.isEmpty) {
                    // Delete the most recent score document
                    val document = querySnapshot.documents[0]
                    document.reference.delete()
                        .addOnSuccessListener {
                            Toast.makeText(this, "This score has been removed from the database.", Toast.LENGTH_LONG).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Sorry, there was an issue removing this score.", Toast.LENGTH_LONG).show()
                        }
                } else {
                    Toast.makeText(this, "Sorry for the confusion, this score was never saved.", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Sorry, there was an issue accessing the database.", Toast.LENGTH_LONG).show()
                deleteButtonClicked = false
            }
    }

}