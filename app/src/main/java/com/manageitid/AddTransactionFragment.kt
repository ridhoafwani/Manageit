package com.manageitid

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.manageitid.databinding.FragmentAddTransactionBinding
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log


class AddTransactionFragment : Fragment() {


    private var _binding : FragmentAddTransactionBinding ? = null
    private val binding : FragmentAddTransactionBinding get() = _binding!!
    var db = FirebaseFirestore.getInstance()

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
        binding.btnSaveTransaction.setOnClickListener {
            when{
                binding.addTransactionLayout.etTitle.text!!.isEmpty() -> {
                    binding.addTransactionLayout.etTitle.error = "Title cannot be blank"
                }

                // Teruskan untuk field wajib lainnya

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
        val transactionData: MutableMap<String, Any> = HashMap()
        transactionData["title"] = binding.addTransactionLayout.etTitle.text.toString()
        transactionData["amount"] = binding.addTransactionLayout.etAmount.text.toString()
        transactionData["type"] = binding.addTransactionLayout.etTransactionType.text.toString()
        transactionData["tag"] = binding.addTransactionLayout.etTag.text.toString()
        transactionData["date"] = binding.addTransactionLayout.etWhen.text.toString()
        transactionData["note"] = binding.addTransactionLayout.etNote.text.toString()

        db.collection("transaction")
            .add(transactionData)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG,
                    "DocumentSnapshot added with ID: " + documentReference.id
                )
                goToDashboard()
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e)
            }
    }

    private fun goToDashboard(){
        val DashboardFragment = DashboardFragment()
        val fragmentmanager = fragmentManager

        fragmentmanager?.beginTransaction()?.apply {
            replace(R.id.fragment_container, DashboardFragment, DashboardFragment::class.java.simpleName)
            addToBackStack(null)
            commit()
        }
    }



}