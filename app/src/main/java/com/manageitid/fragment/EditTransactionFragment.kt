package com.manageitid.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.manageitid.activity.MainActivity
import com.manageitid.R
import com.manageitid.databinding.FragmentEditTransactionBinding
import com.manageitid.extra.Constant
import com.manageitid.extra.Transaction


class EditTransactionFragment : Fragment() {

    private var _binding : FragmentEditTransactionBinding? = null
    private val binding : FragmentEditTransactionBinding get() = _binding!!
    private lateinit var transaction : Transaction
    var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentEditTransactionBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        binding.btnSaveTransaction.setOnClickListener {

            when{
                binding.addTransactionLayout.etTitle.text!!.isEmpty() -> {
                    binding.addTransactionLayout.etTitle.error = "Title cannot be empty"
                }
                binding.addTransactionLayout.etAmount.text!!.isEmpty() ->{
                    binding.addTransactionLayout.etAmount.error = "Amount cannot be empty"
                }
                binding.addTransactionLayout.etTransactionType.text!!.isEmpty() ->{
                    binding.addTransactionLayout.etTransactionType.error = "Transaction Type cannot be empty"
                }
                binding.addTransactionLayout.etTag.text!!.isEmpty() ->{
                    binding.addTransactionLayout.etTag.error = "Transaction Tag cannot be empty"
                }
                binding.addTransactionLayout.etWhen.text!!.isEmpty() ->{
                    binding.addTransactionLayout.etWhen.error = "Transaction Date cannot be empty"
                }
                binding.addTransactionLayout.etNote.text!!.isEmpty() ->{
                    binding.addTransactionLayout.etNote.error = "Transaction cannot be empty"
                }

                else ->{
                    Snackbar.make(
                        binding.root,
                        "Processing, Please Wait",
                        Snackbar.LENGTH_SHORT
                    )
                        .apply {

                            show()
                        }
                    updateTransaction()
                }
            }

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        transaction = arguments?.getSerializable("transaction") as Transaction
        initData()
    }

    private fun initData(){

        val transactionTypeAdapter =
            ArrayAdapter(
                requireContext(),
                R.layout.item_autocomplete_layout,
                Constant.transactionType
            )
        val tagsAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_autocomplete_layout,
            Constant.transactionTags
        )

        binding.addTransactionLayout.etTransactionType.setAdapter(transactionTypeAdapter)
        binding.addTransactionLayout.etTag.setAdapter(tagsAdapter)

        binding.addTransactionLayout.etTitle.setText(transaction.title)
        binding.addTransactionLayout.etAmount.setText(transaction.amount)
        binding.addTransactionLayout.etTransactionType.setText(transaction.transactionType, false)
        binding.addTransactionLayout.etTag.setText(transaction.tag, false)
        binding.addTransactionLayout.etWhen.setText(transaction.date)
        binding.addTransactionLayout.etNote.setText(transaction.note)
    }

    private fun updateTransaction(){
        val newTran = hashMapOf(
            "title" to binding.addTransactionLayout.etTitle.text.toString(),
            "amount" to binding.addTransactionLayout.etAmount.text.toString(),
            "type" to binding.addTransactionLayout.etTransactionType.text.toString(),
            "tag" to binding.addTransactionLayout.etTag.text.toString(),
            "date" to binding.addTransactionLayout.etWhen.text.toString(),
            "note" to binding.addTransactionLayout.etNote.text.toString()
        )

        db.collection("transaction").document(transaction.id)
            .set(newTran, SetOptions.merge())
            .addOnSuccessListener {
                Snackbar.make(
                    binding.root,
                    getString(R.string.success_expense_saved),
                    Snackbar.LENGTH_LONG
                )
                    .apply {

                        show()
                    }

                moveToMain()
            }
            .addOnFailureListener {  }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    fun moveToMain(){
        startActivity(Intent(activity, MainActivity::class.java))
    }


}