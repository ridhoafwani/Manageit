package com.manageitid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import com.manageitid.databinding.FragmentEditTransactionBinding
import com.manageitid.databinding.FragmentTransactionDetailsBinding


class EditTransactionFragment : Fragment() {

    private var _binding : FragmentEditTransactionBinding? = null
    private val binding : FragmentEditTransactionBinding get() = _binding!!
    private lateinit var transaction : Transaction
    var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentEditTransactionBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        binding.btnSaveTransaction.setOnClickListener {

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        transaction = arguments?.getSerializable("transaction") as Transaction
        initData()
    }

    private fun initData(){
        binding.addTransactionLayout.etTitle.setText(transaction.title)
        binding.addTransactionLayout.etAmount.setText(transaction.amount.toString())
        binding.addTransactionLayout.etTransactionType.setText(transaction.transactionType, false)
        binding.addTransactionLayout.etTag.setText(transaction.tag, false)
        binding.addTransactionLayout.etWhen.setText(transaction.date)
        binding.addTransactionLayout.etNote.setText(transaction.note)
    }

    private fun updateTransaction(){
        val newTran = hashMapOf(
            "title" to binding.addTransactionLayout.etTitle,
            "amount" to binding.addTransactionLayout.etAmount,
            "type" to binding.addTransactionLayout.etTransactionType,
            "tag" to binding.addTransactionLayout.etTag,
            "date" to binding.addTransactionLayout.etWhen,
            "note" to binding.addTransactionLayout.etNote
        )

        db.collection("transaction").document(transaction.id)
            .set(newTran)
            .addOnSuccessListener {  }
            .addOnFailureListener {  }
    }


}