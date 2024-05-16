package alantduong.com.csc196p_final

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
/*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
 */

class TitleActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailText: TextView
    private lateinit var passText: TextView
    private lateinit var loginButton: Button
    private lateinit var ssoButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.title_main)

        auth = FirebaseAuth.getInstance()
        emailText = findViewById<TextView>(R.id.emailText)
        passText = findViewById<TextView>(R.id.passText)
        loginButton = findViewById<Button>(R.id.loginButton)
        ssoButton = findViewById<Button>(R.id.ssoButton)
/*        ssoButton.setOnClickListener {
            signInWithGoogle()
        }*/

        loginButton.setOnClickListener {
            val email = emailText.text.toString().trim()
            val pass = passText.text.toString().trim()

            if (email.isEmpty() && pass.isEmpty()) {
                Toast.makeText(this, "Please enter a username and password.", Toast.LENGTH_SHORT).show()
            } else if (pass.isEmpty()) {
                Toast.makeText(this, "Please enter a password.", Toast.LENGTH_SHORT).show()
            } else if (email.isEmpty()) {
                Toast.makeText(this, "Please enter a username.", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email, pass)
            }
        }

        ssoButton.setOnClickListener {
            // Implement SSO functionality (e.g., Firebase Authentication)
            // Proceed to game activity
            startGameActivity()
        }

    }

    private fun startGameActivity() {
        // Start the game activity
        startActivity(Intent(this, MainActivity::class.java))
        finish() // Finish the current activity to prevent the user from navigating back to it
    }

    fun onCreateAccountClick(view: View) {
        startActivity(Intent(this, CreateActivity::class.java))
    }

    private fun loginUser(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login success
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    // Login failed
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    /*
    private fun signInWithGoogle() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Handle sign-in failure
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    // Proceed to game activity or handle other logic
                    startGameActivity()
                } else {
                    // If sign in fails, display a message to the user.
                }
            }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
     */
}
