package com.yrabdelrhmn.onlineshoppingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.yrabdelrhmn.onlineshoppingapp.model.CategoryModel
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.category_item.view.*
import kotlinx.android.synthetic.main.products_item.view.*
import java.util.stream.DoubleStream.builder

class CategoryActivity : AppCompatActivity() {

    var storage: FirebaseStorage? = null
    var reference: StorageReference? = null
    var db: FirebaseFirestore? = null
    var adapter : FirestoreRecyclerAdapter<CategoryModel,CategoryViewHolder>?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        db = FirebaseFirestore.getInstance()
        storage = Firebase.storage
        reference = storage!!.reference

        getAllCategories()


        addBtnCate.setOnClickListener{
            val intent = Intent(this,AddCategory::class.java)
            startActivity(intent)
        }
    }

    private fun getAllCategories() {
        val query = db!!.collection("category")
        val options = FirestoreRecyclerOptions.Builder<CategoryModel>().setQuery(query,CategoryModel::class.java).build()
        adapter = object : FirestoreRecyclerAdapter<CategoryModel,CategoryViewHolder>(options)
        {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
           val view = LayoutInflater.from(this).inflate(R.layout.category_item,parent,false)
                return CategoryViewHolder(view)
            }

            override fun onBindViewHolder(
                holder: CategoryViewHolder,
                position: Int,
                model: CategoryModel
            ) {
                holder.name.text = model.name
                Glide.with(this@CategoryActivity).load(model.image).into(holder.image)
            }

        }
        rvCategory.layoutManager = GridLayoutManager(this,2)
        rvCategory.adapter = adapter
    }


    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name = view.categoryName
        var image = view.category_image
    }

    fun addCategory(){
        val category = listOf(
            hashMapOf("name" to "skin care", "image" to R.drawable.skin),
            hashMapOf("name" to "hair care", "image" to R.drawable.hair),
            hashMapOf("name" to "nail" , "image" to R.drawable.nails),
            hashMapOf("name" to "lipstick","image" to R.drawable.p8),
            hashMapOf("name" to "Mascara", "image" to R.drawable.p2),
        )
        db!!.collection("products")
            .add(category)
            .addOnSuccessListener {
                    dr ->
                Log.e("Category Add", dr.id)
            }.addOnFailureListener {
                    e ->
                e.message?.let { Log.e("Failed to add", it) }
            }
    }
        super.onStart()
        adapter!!.startListening()
    }
    
    override fun onStop() {
        super.onStop()
        adapter!!.stopListening()
    }
}