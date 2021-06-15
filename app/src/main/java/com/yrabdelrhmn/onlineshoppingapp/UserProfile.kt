package com.yrabdelrhmn.onlineshoppingapp

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import io.grpc.Context
import kotlinx.android.synthetic.main.activity_login_page.*
import kotlinx.android.synthetic.main.activity_user_profile.*

class UserProfile : AppCompatActivity() {
    var db:FirebaseFirestore? = null
    var storage:FirebaseStorage?=null
    var reference:StorageReference?=null
    lateinit var auth :FirebaseAuth
    lateinit var progressDialog: ProgressDialog
    var path:String?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        db=Firebase.firestore
        auth = Firebase.auth
        storage = Firebase.storage
        reference = storage!!.reference
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)

        getProfileData()


    }

    private fun getProfileData() {
        db!!.collection("users").get()
            .addOnSuccessListener { d ->
                userName.text = d.documents[0].get("name").toString()
                userEmail.setText(auth.currentUser!!.email)
                userPhone.setText(d.documents[0].get("phone").toString())
                userAddress.setText(d.documents[0].get("address").toString())
            }.addOnFailureListener{
                e ->

            }
    }
}