package com.manageitid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.manageitid.databinding.FragmentTransactionDetailsBinding
import java.io.Serializable


class TransactionDetailsFragment : Fragment() {

    private var _binding : FragmentTransactionDetailsBinding? = null
    private val binding : FragmentTransactionDetailsBinding get() = _binding!!
    private lateinit var transaction : Transaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentTransactionDetailsBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        binding.editTransaction.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("transaction", transaction as Serializable)
            }
            moveToEdit(bundle)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        transaction = arguments?.getSerializable("transaction") as Transaction
        initData()

    }

    private fun initData(){
        binding.transactionDetails.title.text = capitalEachWord(transaction.title)
        binding.transactionDetails.amount.text = indonesianRupiah(transaction.amount.toDouble())
        binding.transactionDetails.type.text = transaction.transactionType
        binding.transactionDetails.date.text = transaction.date
        binding.transactionDetails.tag.text = transaction.tag
//        binding.transactionDetails.createdAt.text = transaction.createdAtDateFormat
        binding.transactionDetails.note.text = transaction.note
    }

    fun capitalEachWord (str: String) : String {

        val words = str.split(" ")

        var newStr = ""

        words.forEach {
            newStr += it.capitalize() + " "
        }

        return newStr
    }

    private fun moveToEdit(bundle : Bundle){
        val EditTransactionFragment = EditTransactionFragment()
        val fragmentmanager = fragmentManager
        EditTransactionFragment.arguments = bundle

        fragmentmanager?.beginTransaction()?.apply {
            replace(R.id.fragment_container, EditTransactionFragment, EditTransactionFragment::class.java.simpleName)
            addToBackStack(null)
            commit()
        }
    }


}