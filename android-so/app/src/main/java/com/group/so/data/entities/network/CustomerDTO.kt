package com.group.so.data.entities.network

import com.group.so.core.data.NullToEmptyString
import com.group.so.data.entities.db.CustomerDb
import com.group.so.data.entities.model.Customer
import com.squareup.moshi.Json

data class CustomerDTO(
    @Json(name = "id") @field:Json(name = "id")
    val id: Int,

    @NullToEmptyString
    val name: String,

    @NullToEmptyString
    @Json(name = "document_number") @field:Json(name = "document_number")
    val documentNumber: String,

    @NullToEmptyString
    val phone: String,

    @NullToEmptyString
    @Json(name = "state_inscription") @field:Json(name = "state_inscription")
    val stateInscription: String?,

    @NullToEmptyString
    @Json(name = "customer_type") @field:Json(name = "customer_type")
    val customeType: String

) {
    fun toModel(): Customer = Customer(
        id = id,
        name = name,
        documentNumber = documentNumber,
        phone = phone,
        stateInscription = stateInscription ?: "",
        customerType = customeType
    )

    fun toDb(): CustomerDb = CustomerDb(
        id = id,
        name = name,
        documentNumber = documentNumber,
        phone = phone,
        stateInscription = stateInscription ?: "",
        customerType = customeType
    )
}

fun List<CustomerDTO>.toModel(): List<Customer> =
    this.map {
        it.toModel()
    }

fun List<CustomerDTO>.toDb(): List<CustomerDb> =
    this.map {
        it.toDb()
    }
