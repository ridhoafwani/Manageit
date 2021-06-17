package com.manageitid

import java.io.Serializable
import java.text.DateFormat

data class Transaction(
    val id : String,
    val title : String,
    val amount: String,
    val transactionType: String,
    val tag: String,
    val date: String,
    val note: String,
    val createdAt: String
) : Serializable{
    val createdAtDateFormat: String
        get() = DateFormat.getDateTimeInstance()
            .format(createdAt) // Date Format: Jan 11, 2021, 11:30 AM
}
