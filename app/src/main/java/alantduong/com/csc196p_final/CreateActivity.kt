package alantduong.com.csc196p_final

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class CreateActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var createEmail: TextView
    lateinit var createPass: TextView
    lateinit var createButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_main)

        auth = FirebaseAuth.getInstance()

        createEmail = findViewById<TextView>(R.id.createEmail)
        createPass = findViewById<TextView>(R.id.createPass)
        createButton = findViewById<Button>(R.id.createButton)

        createButton.setOnClickListener() {
            val email = createEmail.text.toString().trim()
            val pass = createPass.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                createEmail.error = "Please enter an email."
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(pass)) {
                createPass.error = "Please enter an password."
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Account creation success
                        Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, TitleActivity::class.java))
                    } else {
                        // Account creation failed
                        val errorMessage = task.exception?.message ?: "Unknown error"
                        Toast.makeText(this, "Account creation failed: $errorMessage", Toast.LENGTH_LONG).show()
                        Log.e("CreateActivity", "Account creation failed: $errorMessage")
                    }
                }

        }
    }
}
