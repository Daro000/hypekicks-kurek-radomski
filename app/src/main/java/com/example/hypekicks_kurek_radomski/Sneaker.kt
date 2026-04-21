package com.example.hypekicks_kurek_radomski

import java.io.Serializable

data class Sneaker(
    val id: String = "",
    val brand: String = "",
    val modelName: String = "",
    val releaseYear: Long = 0,
    val resellPrice: Double = 0.0,
    val imageUrl: String = ""
) : Serializable