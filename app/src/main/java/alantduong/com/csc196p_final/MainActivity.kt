package alantduong.com.csc196p_final

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.ktx.auth
import kotlin.random.Random
// Firebase Implementation
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    // Firebase database variable
    val db = Firebase.firestore

    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var mainLayout: ConstraintLayout

    // Initialize the variables for average calculation
    private var totalReflexTime = 0L
    private var scoreCount = 0
    private var startTime = 0L

    // Runnable function
    private val runnable = Runnable {
        // Set the background on the screen
        mainLayout.setBackgroundResource(R.color.green)

        // Get the system time in millisecond when the screen background is set
        startTime = System.currentTimeMillis()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainLayout = findViewById(R.id.main)
        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)

        // Function when the start button is clicked
        startButton.setOnClickListener {
            // Generate a random number from 1-10
            val num = Random.nextInt(1, 5)

            // Call the runnable function after a post delay of num seconds
            val handler = Handler()
            handler.postDelayed(runnable, num * 1000L)
        }

        // Function when stop button is clicked
        stopButton.setOnClickListener {
            // Get the system time in millisecond when the stop button is clicked
            val endTime = System.currentTimeMillis()
            val reflexTime = endTime - startTime
            totalReflexTime += reflexTime
            scoreCount++

            // Display reflex time in toast message
            Toast.makeText(
                applicationContext,
                "Your reflex time is: ${reflexTime}ms",
                Toast.LENGTH_LONG
            ).show()

            // If 5 scores have been collected, calculate the average and store it in Firebase
            if (scoreCount >= 5) {
                val averageReflexTime = totalReflexTime / scoreCount
                // Save average score to Firebase
                val currentUser = Firebase.auth.currentUser
                val userEmail = currentUser?.email
                if (userEmail != null) {
                    val scoreData = hashMapOf(
                        "email" to userEmail,
                        "averageReflexTime" to averageReflexTime
                    )
                    db.collection("scores")
                        .add(scoreData)
                        .addOnSuccessListener { documentReference ->
                            // Document successfully written
                            Toast.makeText(
                                applicationContext,
                                "Average score saved with ID: ${documentReference.id}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener { e ->
                            // Error handling
                            Toast.makeText(
                                applicationContext,
                                "Error saving score: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    // Handle case where user is not logged in
                    Toast.makeText(
                        applicationContext,
                        "User not logged in",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                // Start ResultsActivity
                val intent = Intent(this, ResultsActivity::class.java)
                intent.putExtra("averageReflexTime", averageReflexTime)
                startActivity(intent)

                // Reset variables for next set of scores
                totalReflexTime = 0
                scoreCount = 0
            }

            // Remove the background again
            mainLayout.setBackgroundResource(0)
        }
    }

}
