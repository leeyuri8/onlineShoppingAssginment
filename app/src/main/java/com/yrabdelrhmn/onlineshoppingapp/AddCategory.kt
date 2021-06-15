package com.yrabdelrhmn.onlineshoppingapp

import android.app.ProgressDialog
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.vansuita.pickimage.bean.PickResult
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.vansuita.pickimage.listeners.IPickResult
import com.google.firebase.storage.ktx.storage
import io.grpc.LoadBalancer
import kotlinx.android.synthetic.main.activity_add_category.*
import kotlinx.android.synthetic.main.category_item.*
import kotlinx.android.synthetic.main.products_item.*
import java.util.*

class AddCategory : AppCompatActivity() {
    lateinit var progressDialog: ProgressDialog
    var storage: FirebaseStorage? = null
    var reference: StorageReference? = null
    var db: FirebaseFirestore? = null
    var path: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)
        db = Firebase.firestore
        storage = Firebase.storage
        reference = storage!!.reference
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading..")
        progressDialog.setCancelable(false)

        addCategory("id",categoryName.toString())

        cateImg.setOnClickListener{
            PickImageDialog.build(PickSetup()).show(this)
        }
    }

    private fun addCategory(id : String ,name : String ) {
        var category = hashMapOf(
            "id" to id,
           "name" to name
            )
        db!!.collection("category").add(category).addOnSuccessListener { dr ->
            Log.e("Document ID", dr.id)

        }.addOnFailureListener { e ->
            Log.e("log",e.message!!)

        }
    }
     override fun onPickResult(r: PickResult?) {
        category_image.setImageBitmap(r!!.bitmap)
        uploadImage(r.uri)
         return
    }

    private fun uploadImage(uri: Uri?) {
        progressDialog.show()
        reference!!.child("category/" + UUID.randomUUID().toString()).putFile(uri!!)
            .addOnSuccessListener { taskSnapshot ->
                progressDialog.dismiss()
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    path = uri.toString()
                }
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener { exception ->

            }
    }
}