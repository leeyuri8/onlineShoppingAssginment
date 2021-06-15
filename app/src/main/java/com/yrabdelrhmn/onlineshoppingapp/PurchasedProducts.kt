package com.yrabdelrhmn.onlineshoppingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class PurchasedProducts : AppCompatActivity() {
    private var storage: FirebaseStorage? = null
    private var reference: StorageReference? = null
    private var db: FirebaseFirestore? = null
    private var rv : Recycler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchased_products)
         getPurchasedProducts()
    }

    fun getPurchasedProducts(){

    }
}