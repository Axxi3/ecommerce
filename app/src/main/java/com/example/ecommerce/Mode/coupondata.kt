package com.example.ecommerce.Mode

data class coupondata(
    var code: String, // Add a setter function for the "code" property
    var discount: String
) {
    constructor() : this("", "")
}

