package alantduong.com.csc196p_final

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class InstructionsActivity : AppCompatActivity() {

    lateinit var readyButton: Button
    lateinit var scaredButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.instruction_main)

        readyButton = findViewById(R.id.readyButton)
        scaredButton = findViewById(R.id.scaredButton)

        readyButton.setOnClickListener {
            // Move to MainActivity
            startActivity(Intent(this, MainActivity::class.java))
        }

        scaredButton.setOnClickListener {
            // Exit the game
            finish()
        }
    }

}
