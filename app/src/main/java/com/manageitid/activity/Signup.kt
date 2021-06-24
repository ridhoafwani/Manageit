package com.manageitid.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.manageitid.databinding.ActivitySignupBinding

class Signup : AppCompatActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding : ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.btnSignUp.setOnClickListener(this)
        binding.tvSignIn.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            Login.user = auth
            moveToMain()
        }
    }

    override fun onClick(v: View?) {
        when(v){
            binding.btnSignUp -> register()
            binding.tvSignIn -> moveToLogin()
        }
    }

    fun register(){
        val name = binding.edtName.text.toString()
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()

        when{
            TextUtils.isEmpty(name) -> {
                binding.edtName.setError("Name cannot empty")
                binding.edtName.requestFocus()
            }
            TextUtils.isEmpty(email) -> {
                binding.edtEmail.setError("Name cannot empty")
                binding.edtEmail.requestFocus()
            }
            TextUtils.isEmpty(password) -> {
                binding.edtPassword.setError("Name cannot empty")
                binding.edtPassword.requestFocus()
            }
            else -> {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(baseContext, "Registration Succsesfully",
                                Toast.LENGTH_SHORT).show()
                            val user = auth.currentUser
                            moveToLogin()
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

    }

    fun moveToLogin(){
        startActivity(Intent(baseContext, Login::class.java))
    }

    fun moveToMain(){
        startActivity(Intent(baseContext, MainActivity::class.java))
    }
}