package com.nataraj.paging3.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * @author natarajkr007@gmail.com
 * @since 06/10/25
 * */

@Serializable
data class DummyProductResponse(
    val products: List<DummyProduct>,
    val total: Int,
    val skip: Int,
    val limit: Int
)

@Entity
@Serializable
data class DummyProduct(
    @PrimaryKey val id: Int,
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val price: Double = 0.0,
    val discountPercentage: Double = 0.0,
    val rating: Double = 0.0,
    val stock: Int = 0,
//    val tags: List<String>,
    val brand: String = "",
    val sku: String = "",
    val weight: Int = 0,
//    val dimensions: Dimensions,
    val warrantyInformation: String = "",
    val shippingInformation: String = "",
    val availabilityStatus: String = "",
//    val reviews: List<Review>,
    val returnPolicy: String = "",
    val minimumOrderQuantity: Int = 0,
//    val meta: Meta,
//    val images: List<String>,
    val thumbnail: String = ""
)

@Serializable
data class Dimensions(
    val width: Double,
    val height: Double,
    val depth: Double
)

@Serializable
data class Review(
    val rating: Int,
    val comment: String,
    val date: String,
    val reviewerName: String,
    val reviewerEmail: String
)

@Serializable
data class Meta(
    val createdAt: String,
    val updatedAt: String,
    val barcode: String,
    val qrCode: String
)
