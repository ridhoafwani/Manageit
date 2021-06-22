package com.manageitid

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.manageitid.databinding.FragmentEditTransactionBinding
import com.manageitid.databinding.FragmentTransactionDetailsBinding


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

            // Buat validation disini juga

            updateTransaction()
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
                goToDashboard()
            }
            .addOnFailureListener {  }
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }


}