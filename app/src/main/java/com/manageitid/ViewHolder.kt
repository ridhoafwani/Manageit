package com.manageitid

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_transaction_layout, parent, false)){

    private var image : ImageView? = null
    private var name : TextView? = null
    private var amount : TextView? = null
    private var tag : TextView? = null

    init {
        image = itemView.findViewById(R.id.transactionIconView)
        name = itemView.findViewById(R.id.transactionName)
        amount = itemView.findViewById(R.id.transactionAmount)
        tag = itemView.findViewById(R.id.transactionCategory)
    }

    fun bind (data: Transaction){
        image?.setImageResource(R.drawable.ic_food)
        name?.text = data.title
        amount?.text = data.amount
        tag?.text = data.tag
    }

}