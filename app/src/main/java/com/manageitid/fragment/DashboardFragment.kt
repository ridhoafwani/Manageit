package com.manageitid.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.manageitid.activity.Login
import com.manageitid.R
import com.manageitid.adapter.Adapter
import com.manageitid.databinding.FragmentDashboardBinding
import com.manageitid.extra.Transaction
import com.manageitid.extra.indonesianRupiah
import java.io.Serializable


class DashboardFragment : Fragment() {
    //new
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

    companion object {
        private const val TAG = "___TEST___"
        lateinit var dashboardBind : FragmentDashboardBinding
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        dashboardBind = binding
        binding.floatingAddTransaction.setOnClickListener {
            moveToAdd()
        }
        this.getDataFromFirestore()
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeToDelete()
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
                    dataIncome.clear()
                    dataExpense.clear()
                    for (document in task.result!!) {
                        val id = document.id
                        val title = document.data.get("title") as String
                        val amount = document.data.get("amount") as String
                        val type = document.data.get("type") as String
                        val tag = document.data.get("tag") as String
                        val date = document.data.get("date") as String
                        val note = document.data.get("note") as String
                        val time = document.data.get("time") as String
                        data.add(Transaction(id,title,amount,type,tag,date,note,time))
                        when(type){
                            "Income" ->{
                                dataIncome.add(Transaction(id,title,amount,type,tag,date,note,time))
                            }
                            "Expense" ->{
                                dataExpense.add(Transaction(id,title,amount,type,tag,date,note,time))
                            }
                        }
                    }
                    Snackbar.make(
                        binding.root,
                        getString(R.string.success_load_data),
                        Snackbar.LENGTH_LONG
                    )
                        .apply {
                            show()
                        }

                    Log.d(TAG, "ini datanya" + data)
                    if(data.isEmpty()){
                        showEmptyData()
                    }
                    else{
                        filter("All")
                        onTotalTransactionLoaded(data)
                    }


                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                    Toast.makeText(activity, "Error getting documents $task.exception",
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
                putSerializable("transaction", it as Serializable)
            }

            moveToDetails(bundle)

        }
    }

    private fun swipeToDelete() {
        // init item touch callback for swipe action
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // get item position & delete notes
                val position = viewHolder.adapterPosition
                val transaction = data[position]
                val transactionItem = Transaction(
                    transaction.id,
                    transaction.title,
                    transaction.amount,
                    transaction.transactionType,
                    transaction.tag,
                    transaction.date,
                    transaction.note,
                    transaction.createdAt
                )
                deleteTransaction(transaction.id)
                Snackbar.make(
                    binding.root,
                    getString(R.string.success_transaction_delete),
                    Snackbar.LENGTH_LONG
                )
                    .apply {
                        setAction("Undo") {
                            undo(transactionItem)
                        }
                        show()
                    }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.transactionList)
        }
    }

    private fun deleteTransaction(path : String){
        db.collection("transaction").document(path)
            .delete()
            .addOnSuccessListener {
                getDataFromFirestore()
                Log.d(TAG, "DocumentSnapshot successfully deleted!")
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }

    private fun undo(transactionItem : Transaction) {
        val uid = auth.currentUser?.uid.toString()
        val transactionData: MutableMap<String, Any> = HashMap()
        transactionData["uid"] = uid
        transactionData["time"] = transactionItem.createdAt
        transactionData["title"] = transactionItem.title
        transactionData["amount"] = transactionItem.amount
        transactionData["type"] = transactionItem.transactionType
        transactionData["tag"] = transactionItem.tag
        transactionData["date"] = transactionItem.date
        transactionData["note"] = transactionItem.note

        db.collection("transaction")
            .add(transactionData)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    TAG,
                    "DocumentSnapshot added with ID: " + documentReference.id
                )
                this.getDataFromFirestore()
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



}