package com.manageitid

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.manageitid.databinding.ActivityLoginBinding
import com.manageitid.databinding.FragmentAddTransactionBinding


class AddTransactionFragment : Fragment() {


    private var _binding : FragmentAddTransactionBinding ? = null
    private val binding : FragmentAddTransactionBinding get() = _binding!!

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
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }


}