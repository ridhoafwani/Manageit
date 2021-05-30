package com.manageitid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.manageitid.databinding.ActivityLoginBinding
import android.widget.Toast

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var email : String
    private lateinit var password : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.btnSignIn.setOnClickListener{
            email = binding.edtEmail.text.toString()
            password = binding.edtPassword.text.toString()
            signIn()
        }
        binding.tvSignUp.setOnClickListener{
            moveToSignUp()
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            moveToMain()
        }
    }


    fun moveToMain(){
        startActivity(Intent(baseContext, MainActivity::class.java))
    }

    fun moveToSignUp(){
        startActivity(Intent(baseContext, Signup::class.java))
    }

    fun signIn(){

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(baseContext, "Signing In",
                        Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    moveToMain()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}