package com.manageitid

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Query
import com.manageitid.databinding.FragmentDashboardBinding
import kotlin.reflect.typeOf


class DashboardFragment : Fragment() {

    private var _binding : FragmentDashboardBinding? = null
    private val binding : FragmentDashboardBinding get() = _binding!!
    var db = FirebaseFirestore.getInstance()
    var data = ArrayList<Transaction>()
    var dataIncome = ArrayList<Transaction>()
    var dataExpense = ArrayList<Transaction>()
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: Adapter
    private val auth : FirebaseAuth get() = Login.user
    private lateinit var uid : String
    private var income : Double = 0.0
    private var expense : Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private companion object {
        private const val TAG = "___TEST___"

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        binding.floatingAddTransaction.setOnClickListener {
            moveToAdd()
        }
        this.getDataFromFirestore()
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun getDataFromFirestore() {
        uid = auth.currentUser?.uid.toString()
        db.collection("transaction")
                .whereEqualTo("uid", auth.currentUser?.uid)
                .orderBy("time", Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    data.clear()
                    for (document in task.result!!) {
                        val title = document.data.get("title") as String
                        val amount = document.data.get("amount") as String
                        val type = document.data.get("type") as String
                        val tag = document.data.get("tag") as String
                        val date = document.data.get("date") as String
                        val note = document.data.get("note") as String
                        data.add(Transaction(title,amount,type,tag,date,note))
                        when(type){
                            "Income" ->{
                                dataIncome.add(Transaction(title,amount,type,tag,date,note))
                            }
                            "Expense" ->{
                                dataExpense.add(Transaction(title,amount,type,tag,date,note))
                            }
                        }
                    }
                    Toast.makeText(activity, "berhasil datanya",
                        Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "ini datanya" + data)
                    if(data.isEmpty()){
                        showEmptyData()
                    }
                    else{
                        onFilter("All")
                        onTotalTransactionLoaded(data)
                    }


                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                    Toast.makeText(activity, "Gagal bos $task.exception",
                        Toast.LENGTH_SHORT).show()

                }
            })
    }

    private fun moveToAdd(){
        val AddTransactionFragment = AddTransactionFragment()
        val fragmentmanager = fragmentManager

        fragmentmanager?.beginTransaction()?.apply {
            replace(R.id.fragment_container, AddTransactionFragment, AddTransactionFragment::class.java.simpleName)
            addToBackStack(null)
            commit()
        }
    }

    private fun moveToDetails(bundle : Bundle){
        val TransactionDetailsFragment = TransactionDetailsFragment()
        val fragmentmanager = fragmentManager
        TransactionDetailsFragment.arguments = bundle

        fragmentmanager?.beginTransaction()?.apply {
            replace(R.id.fragment_container, TransactionDetailsFragment, TransactionDetailsFragment::class.java.simpleName)
            addToBackStack(null)
            commit()
        }
    }

    private fun onTotalTransactionLoaded(transaction: List<Transaction> ) {
        val (totalIncome, totalExpense) = transaction.partition { it.transactionType == "Income" }
        income = totalIncome.sumByDouble { it.amount.toDouble() }
        expense = totalExpense.sumByDouble { it.amount.toDouble() }
        binding.incomeCardView.total.text= "+ ".plus(indonesianRupiah(income))
        binding.expenseCardView.total.text = "- ".plus(indonesianRupiah(expense))
        binding.totalBalanceView.totalBalance.text = (indonesianRupiah(income - expense))
    }

    public fun filter (filter:String){
        onFilter(filter)
        when (filter) {
            "All" -> {
                binding.totalBalanceView.totalBalanceTitle.text =
                        getString(R.string.text_total_balance)
                binding.totalIncomeExpenseView.visibility = View.VISIBLE
                binding.incomeCardView.totalTitle.text = getString(R.string.text_total_income)
                binding.expenseCardView.totalTitle.text = getString(R.string.text_total_expense)
                binding.expenseCardView.totalIcon.setImageResource(R.drawable.ic_expense)
                binding.totalBalanceView.totalBalance.text = (indonesianRupiah(income - expense))
            }
            "Income" -> {
                binding.totalBalanceView.totalBalanceTitle.text =
                        getString(R.string.text_total_income)
                binding.totalIncomeExpenseView.visibility = View.GONE
                binding.totalBalanceView.totalBalance.text = "+ ".plus(indonesianRupiah(income))
            }
            "Expense" -> {
                binding.totalBalanceView.totalBalanceTitle.text =
                        getString(R.string.text_total_expense)
                binding.totalIncomeExpenseView.visibility = View.GONE
                binding.totalBalanceView.totalBalance.text = "- ".plus(indonesianRupiah(expense))
            }
        }

    }

    private fun showEmptyData(){
        binding.emptyStateLayout.visibility = View.VISIBLE
    }

    public fun onFilter(filter: String){

        when (filter) {
            "All" -> {
                adapter = Adapter(data)
            }
            "Income" -> {
                adapter = Adapter(dataIncome)
            }
            "Expense" -> {
                adapter = Adapter(dataExpense)
            }
        }

        recyclerView = binding.transactionList
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("transaction", it)
            }

            moveToDetails(bundle)

        }
    }

}