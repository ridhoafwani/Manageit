package com.manageitid

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.manageitid.databinding.FragmentTransactionDetailsBinding
import java.io.Serializable
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.drawToBitmap
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore


class TransactionDetailsFragment : Fragment() {

    private var _binding : FragmentTransactionDetailsBinding? = null
    private val binding : FragmentTransactionDetailsBinding get() = _binding!!
    private lateinit var transaction : Transaction
    var db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_share, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> {
                deleteTransaction(transaction.id)
            }
            R.id.action_share_text -> shareText()
            R.id.action_share_image -> shareImage()
        }
        return super.onOptionsItemSelected(item)
    }

    // handle permission dialog
    private val requestLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) shareImage()
//            else showErrorDialog()
        }

//    private fun showErrorDialog() =(
//        error dialog sini
//        )

    private fun shareImage() {
        if (!isStoragePermissionGranted()) {
            requestLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            return
        }

        val imageURI = binding.transactionDetails.detailView.drawToBitmap().let { bitmap ->
            saveBitmap(requireActivity(), bitmap)
        } ?: run {
            Toast.makeText(activity, "error",
                Toast.LENGTH_SHORT).show()
            return
        }

        val intent = ShareCompat.IntentBuilder(requireActivity())
            .setType("image/jpeg")
            .setStream(imageURI)
            .intent

        startActivity(Intent.createChooser(intent, null))
    }


    private fun isStoragePermissionGranted(): Boolean = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("StringFormatMatches")
    private fun shareText() = with(binding) {
        val shareMsg = getString(
            R.string.share_message,
            transactionDetails.title.text.toString(),
            transactionDetails.amount.text.toString(),
            transactionDetails.type.text.toString(),
            transactionDetails.tag.text.toString(),
            transactionDetails.date.text.toString(),
            transactionDetails.note.text.toString(),
        )

        val intent = ShareCompat.IntentBuilder(requireActivity())
            .setType("text/plain")
            .setText(shareMsg)
            .intent

        startActivity(Intent.createChooser(intent, null))
    }

    private fun deleteTransaction(path : String){
        db.collection("transaction").document(path)
            .delete()
            .addOnSuccessListener {
                moveToMain()
            }
            .addOnFailureListener {}
    }

    fun moveToMain(){
        startActivity(Intent(activity, MainActivity::class.java))
    }

}