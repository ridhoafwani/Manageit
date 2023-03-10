package com.manageitid.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.manageitid.R
import com.manageitid.databinding.ItemTransactionLayoutBinding
import com.manageitid.extra.Transaction
import com.manageitid.extra.indonesianRupiah

class Adapter( val data : ArrayList<Transaction>) : RecyclerView.Adapter<Adapter.TransactionVH>() {

    inner class TransactionVH(val binding: ItemTransactionLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionVH {
        val binding =
            ItemTransactionLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionVH(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: TransactionVH, position: Int) {

        val item = data[position]
        holder.binding.apply {

            transactionName.text = item.title
            transactionCategory.text = item.tag

            when (item.transactionType) {
                "Income" -> {
                    transactionAmount.setTextColor(
                        ContextCompat.getColor(
                            transactionAmount.context,
                            R.color.income
                        )
                    )

                    transactionAmount.text = "+ ".plus(indonesianRupiah(item.amount.toDouble()))
                }
                "Expense" -> {
                    transactionAmount.setTextColor(
                        ContextCompat.getColor(
                            transactionAmount.context,
                            R.color.expense
                        )
                    )
                    transactionAmount.text = "- ".plus(indonesianRupiah(item.amount.toDouble()))
                }
            }

            when (item.tag) {
                "Income" -> {
                    transactionIconView.setImageResource(R.drawable.ic_income_list)
                }
                "Gift" -> {
                    transactionIconView.setImageResource(R.drawable.ic_gift)
                }
                "Property" -> {
                    transactionIconView.setImageResource(R.drawable.ic_housing)
                }
                "Transportation" -> {
                    transactionIconView.setImageResource(R.drawable.ic_transport)
                }
                "Food" -> {
                    transactionIconView.setImageResource(R.drawable.ic_food)
                }
                "Insurance" -> {
                    transactionIconView.setImageResource(R.drawable.ic_insurance)
                }
                "Healthcare" -> {
                    transactionIconView.setImageResource(R.drawable.ic_medical)
                }
                "Saving & Debts" -> {
                    transactionIconView.setImageResource(R.drawable.ic_savings)
                }
                "Personal Spending" -> {
                    transactionIconView.setImageResource(R.drawable.ic_personal_spending)
                }
                "Entertainment" -> {
                    transactionIconView.setImageResource(R.drawable.ic_entertainment)
                }
                "Fashion" -> {
                    transactionIconView.setImageResource(R.drawable.ic_store)
                }
                "School & College" -> {
                    transactionIconView.setImageResource(R.drawable.ic_school)
                }
                "Child" -> {
                    transactionIconView.setImageResource(R.drawable.ic_child)
                }
                "Internet" -> {
                    transactionIconView.setImageResource(R.drawable.ic_internet)
                }
                "Pets" -> {
                    transactionIconView.setImageResource(R.drawable.ic_pets)
                }
                else -> {
                    transactionIconView.setImageResource(R.drawable.ic_others)
                }
            }

            // on item click
            holder.itemView.setOnClickListener {
                onItemClickListener?.let { it(item) }
            }
        }
    }

    // on item click listener
    private var onItemClickListener: ((Transaction) -> Unit)? = null

    fun setOnItemClickListener(listener: (Transaction) -> Unit) {
        onItemClickListener = listener
    }
}
