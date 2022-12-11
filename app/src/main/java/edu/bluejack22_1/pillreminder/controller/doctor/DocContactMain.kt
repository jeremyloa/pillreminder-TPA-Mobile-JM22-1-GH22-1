package edu.bluejack22_1.pillreminder.controller.doctor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import edu.bluejack22_1.pillreminder.adapter.DocAdapter
import edu.bluejack22_1.pillreminder.databinding.ActivityDocContactMainBinding
import edu.bluejack22_1.pillreminder.model.DoctorContact

class DocContactMain : AppCompatActivity(), DocAdapter.DocClickListener {
    private lateinit var binding: ActivityDocContactMainBinding
    private lateinit var rvDoc: RecyclerView
    private lateinit var docAdapter: DocAdapter

    private lateinit var searchDocText: TextInputEditText
    private lateinit var searchDocBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDocContactMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        searchDocText = binding.searchDocText
        searchDocBtn = binding.searchDocBtn
        searchDocBtn.setOnClickListener {
            if (!searchDocText.toString().isNullOrEmpty()) {
                var intent = Intent(this, DocContactSearch::class.java)
                Log.i("SEARCH_DOC", searchDocText.text.toString())
                intent.putExtra("query", searchDocText.text.toString())
                startActivity(intent)
            }
        }
        buildRecylcerView()
        setContentView(binding.root)
    }

    fun buildRecylcerView(){
        rvDoc = binding.rvDoc
        rvDoc.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        docAdapter = DocAdapter(DoctorContact.allDoctorCon, this)
        rvDoc.adapter = docAdapter
    }

    override fun onDocClicked(pos: Int) {

    }


}