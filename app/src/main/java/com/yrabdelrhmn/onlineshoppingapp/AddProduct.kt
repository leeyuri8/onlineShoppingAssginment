package com.yrabdelrhmn.onlineshoppingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.products_item.*

class AddProduct : AppCompatActivity() {
    private var storage: FirebaseStorage? = null
    private var reference: StorageReference? = null
    private var db: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        db = FirebaseFirestore.getInstance()
        storage = Firebase.storage
        reference = storage!!.reference

        addProduct("id",product_name.toString(),
        product_price.toString(),
        product_image.toString())
    }

    fun addProduct(id: String, name: String, price: String, image: String) {
        var product =
            hashMapOf(
                "id" to id,
                "name" to name,
                "price" to price,
                "image" to image
            )
        db!!.collection("products").add(product).addOnSuccessListener { dr ->
            Log.e("Document ID", dr.id)

        }.addOnFailureListener { e ->

        }


    }
}