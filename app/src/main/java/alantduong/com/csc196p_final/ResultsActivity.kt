package alantduong.com.csc196p_final

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ResultsActivity : AppCompatActivity()  {

    private lateinit var resultTextView: TextView
    private lateinit var highScoreTextView: TextView
    private lateinit var tryButton: Button
    private lateinit var exitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.results_main)

        val db = Firebase.firestore
        tryButton = findViewById(R.id.tryButton)
        exitButton = findViewById(R.id.exitButton)

        highScoreTextView = findViewById(R.id.highScoreTextView)

        db.collection("scores")
            .orderBy("averageReflexTime", Query.Direction.ASCENDING)
            .limit(3)
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
    }

}