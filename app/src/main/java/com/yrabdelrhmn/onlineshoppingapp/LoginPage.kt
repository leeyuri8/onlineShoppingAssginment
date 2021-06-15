package com.yrabdelrhmn.onlineshoppingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login_page.*

class LoginPage : AppCompatActivity() {

 lateinit var auth: FirebaseAuth
 var db: FirebaseFirestore? = null
 private val MIN_PASSWORD_LENGTH = 6
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

            loginBtn.setOnClickListener {
                if (loginValidate()) {
                    val intent = Intent(this@LoginPage, ProductActivity::class.java)
                    startActivity(intent)
                    createNewAccount(emailBtn.toString(), passBtn.toString())
                }
            }
    }


    private fun createNewAccount(email: String, pass: String) {
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                Log.e("Auth", "user ${user!!.uid} + ${user.email}")
                addUser(
                    user.uid,
                    name.text.toString(),
                    user.email!!,
                    phone.text.toString(),
                    address.text.toString()
                )

            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun addUser(id: String, name: String, email: String, phone: String, address: String) {
        var user =
            hashMapOf(
                "id" to id,
                "name" to name,
                "email" to email,
                "phone" to phone,
                "address" to address
            )

        db!!.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
            Log.e("Document ID", documentReference.id)

        }.addOnFailureListener { exception ->

        }

    }
    private fun loginValidate(): Boolean {
        if (emailBtn.text.toString().isEmpty()) {
            emailBtn.error = "Please Enter Email"

        } else if (!Patterns.EMAIL_ADDRESS
                .matcher(emailBtn.text.toString()).matches()
        ) {
            emailBtn.error = resources.getString(R.string.error_invalid_email)

        } else {
            return true
        }
        if (passBtn.text.toString().isEmpty()) {
            passBtn.error = "Please Enter Password!"
            return false

        }else if(passBtn.text.length<MIN_PASSWORD_LENGTH){
            passBtn.error = "Please Enter Valid password"
            return false


        }else{
            return true
        }



        }

    }
