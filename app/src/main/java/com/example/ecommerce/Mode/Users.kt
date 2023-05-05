package com.example.ecommerce.Mode

data class UsersModel(
    val name:String="a",
    val Userphonenumber:String="a",
    val StreetName:String="a",
    val city:String="a",
    val pincode:String="a",
    val state:String="a",
    val county:String="a",
    val FMCtoken:String="",
    val order: List<Any> = listOf()

)
