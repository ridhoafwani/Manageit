package com.manageitid

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.manageitid.databinding.FragmentAddTransactionBinding
import com.manageitid.databinding.FragmentDashboardBinding


class DashboardFragment : Fragment() {

    private var _binding : FragmentDashboardBinding? = null
    private val binding : FragmentDashboardBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        binding.floatingAddTransaction.setOnClickListener {
            val AddTransactionFragment = AddTransactionFragment()
            val fragmentmanager = fragmentManager

            fragmentmanager?.beginTransaction()?.apply {
                replace(R.id.fragment_container, AddTransactionFragment, AddTransactionFragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
        return binding.root

    }

}