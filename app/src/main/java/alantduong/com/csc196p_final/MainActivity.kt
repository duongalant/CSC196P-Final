package alantduong.com.csc196p_final

import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var relativeLayout: RelativeLayout

    // Runnable function
    private val runnable = Runnable {
        // Set the background on the screen
        relativeLayout.setBackgroundResource(R.color.green)

        // Get the system time in millisecond when the screen background is set
        val time = System.currentTimeMillis()

        // Function when stop button is clicked
        stopButton.setOnClickListener {
            // get the system time in millisecond when the stop button is clicked
            val time1 = System.currentTimeMillis()

            // Display reflex time in toast message
            Toast.makeText(
                applicationContext,
                "Your reflex time is: ${time1 - time}ms",
                Toast.LENGTH_LONG
            ).show()

            // Remove the background again
            relativeLayout.setBackgroundResource(0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        relativeLayout = findViewById(R.id.relativeLayout)
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
    }
}
