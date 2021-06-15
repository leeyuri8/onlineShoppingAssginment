package com.yrabdelrhmn.onlineshoppingapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.yrabdelrhmn.onlineshoppingapp.model.ProductModel
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.category_item.*
import kotlinx.android.synthetic.main.category_item.product_name
import kotlinx.android.synthetic.main.products_item.*
import kotlinx.android.synthetic.main.products_item.view.*
import java.util.*
import kotlin.collections.HashMap

class ProductActivity : AppCompatActivity() {
    private var storage: FirebaseStorage? = null
    private var reference: StorageReference? = null
    private var db: FirebaseFirestore? = null
    private var adapter: FirestoreRecyclerAdapter<ProductModel, ProductViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        db = FirebaseFirestore.getInstance()
        storage = Firebase.storage
        reference = storage!!.reference

        getAllProducts()

            addBtn.setOnClickListener{
            var intent = Intent(this,AddProduct::class.java)
            startActivity(intent)
        }

 if(spinner2.selectedItem.equals("Delete")){
     deleteProduct()

 } else if(spinner2.selectedItem.equals("Update")) {
     updateProduct("id", product_name.toString(), product_price.toString(),categoryName.toString())
 }
buy.setOnClickListener {
    startActivity(Intent(this,PurchasedProducts::class.java))
}

    }

    private fun getAllProducts() {
        val query = db!!.collection("products")
        val options =
            FirestoreRecyclerOptions.Builder<ProductModel>()
                .setQuery(query, ProductModel::class.java).build()
        adapter = object : FirestoreRecyclerAdapter<ProductModel, ProductViewHolder>(options) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
                var view = LayoutInflater.from(this@ProductActivity)
                    .inflate(R.layout.products_item, parent, false)
                return ProductViewHolder(view)
            }

            override fun onBindViewHolder(
                holder: ProductViewHolder,
                position: Int,
                model: ProductModel
            ) {
                holder.name.text = model.name
                holder.price.text = model.price.toString()
                Glide.with(this@ProductActivity).load(model.image).into(holder.image)

            }

        }
        rvProduct.layoutManager = GridLayoutManager(this, 2)
        rvProduct.adapter = adapter
    }



    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name = view.product_name
        var image = view.product_image
        var price = view.product_price

    }

    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter!!.stopListening()
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
    private fun deleteProduct(){
        db!!.collection("products").document()
            .delete()
            .addOnSuccessListener {
                Log.e("tag","Product deleted")
            }
            .addOnFailureListener{
                execution -> Log.e("tag",execution.message!!)
            }

    }
    private fun updateProduct(
        oldId: String,
        name: String,
        price: String,
        category:String){
        val product = HashMap<String,Any>()
        product["name"] = name
        product["price"] = price
        product["category"] = category

        db!!.collection("products").document(oldId)
            .update(product)
            .addOnSuccessListener {
                Log.e("tag","Product Updated")
            }.addOnFailureListener{e->
                Log.e("tag",e.message!!)
            }
    }



}






