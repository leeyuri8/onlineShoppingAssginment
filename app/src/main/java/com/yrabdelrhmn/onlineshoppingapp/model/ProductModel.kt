package com.yrabdelrhmn.onlineshoppingapp.model
import com.google.gson.annotations.SerializedName

data class ProductModel(
    var id : Int,
    var name:String,
    var price:String,
    var image:Int,
    var category : String

)
