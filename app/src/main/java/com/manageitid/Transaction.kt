package com.manageitid

data class Transaction(
    val title : String,
    val amount: String,
    val transactionType: String,
    val tag: String,
    val date: String,
    val note: String
)
