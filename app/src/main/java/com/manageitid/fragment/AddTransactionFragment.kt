package com.manageitid.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import android.widget.ArrayAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.manageitid.activity.Login
import com.manageitid.activity.MainActivity
import com.manageitid.R
import com.manageitid.databinding.FragmentAddTransactionBinding
import com.manageitid.extra.Constant
import com.manageitid.extra.DatePicker
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashMap


class AddTransactionFragment : Fragment() {


    private var _binding : FragmentAddTransactionBinding? = null
    private val binding : FragmentAddTransactionBinding get() = _binding!!
    var db = FirebaseFirestore.getInstance()
    private val auth : FirebaseAuth get() = Login.user
    val currentDateTime = LocalDateTime.now()


    private companion object {
        private const val TAG = "___TEST___"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddTransactionBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment

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

        // Transform TextInputEditText to DatePicker using Ext function
        binding.addTransactionLayout.etWhen.DatePicker(
            requireContext(),
            "dd/MM/yyyy",
            Date()
        )



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
                    addNewDataToFirestore()
                }
            }
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }



    private fun addNewDataToFirestore() {
        val uid = auth.currentUser?.uid.toString()
        val transactionData: MutableMap<String, Any> = HashMap()
        transactionData["uid"] = uid
        transactionData["time"] = currentDateTime.toString()
        transactionData["title"] = binding.addTransactionLayout.etTitle.text.toString()
        transactionData["amount"] = binding.addTransactionLayout.etAmount.text.toString()
        transactionData["type"] = binding.addTransactionLayout.etTransactionType.text.toString()
        transactionData["tag"] = binding.addTransactionLayout.etTag.text.toString()
        transactionData["date"] = binding.addTransactionLayout.etWhen.text.toString()
        transactionData["note"] = binding.addTransactionLayout.etNote.text.toString()

        db.collection("transaction")
            .add(transactionData)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    TAG,
                    "DocumentSnapshot added with ID: " + documentReference.id
                )
                moveToMain()
                Snackbar.make(
                    binding.root,
                    getString(R.string.success_expense_saved),
                    Snackbar.LENGTH_LONG
                )
                    .apply {

                        show()
                    }
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e)
            }
    }

    fun moveToMain(){
        startActivity(Intent(activity, MainActivity::class.java))
    }



}