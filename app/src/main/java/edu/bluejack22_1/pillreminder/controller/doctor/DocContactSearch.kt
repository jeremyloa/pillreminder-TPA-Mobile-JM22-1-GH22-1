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
import com.google.android.material.textview.MaterialTextView
import edu.bluejack22_1.pillreminder.adapter.DocAdapter
import edu.bluejack22_1.pillreminder.databinding.ActivityDocContactMainBinding
import edu.bluejack22_1.pillreminder.databinding.ActivityDocContactSearchBinding
import edu.bluejack22_1.pillreminder.model.DoctorContact

class DocContactSearch : AppCompatActivity(), DocAdapter.DocClickListener {
    private lateinit var binding: ActivityDocContactSearchBinding
    private lateinit var rvDocSearch: RecyclerView
    private lateinit var docAdapter: DocAdapter
    private lateinit var searchDocSearchText: TextInputEditText
    private lateinit var searchDocSearchBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDocContactSearchBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        searchDocSearchText = binding.searchDocSearchText
        searchDocSearchText.setText(intent.getStringExtra("query").toString())
        searchDocSearchBtn = binding.searchDocSearchBtn
        searchDocSearchBtn.setOnClickListener {
            if (!searchDocSearchText.toString().isNullOrEmpty()) {
                var intent = Intent(this, DocContactSearch::class.java)
                intent.putExtra("query", searchDocSearchText.text.toString())
                startActivity(intent)
            }
        }
        buildRecylcerView()
        setContentView(binding.root)
    }

    fun buildRecylcerView(){
        rvDocSearch = binding.rvDocSearch
        rvDocSearch.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        docAdapter = DocAdapter(searchDoc(intent.getStringExtra("query").toString()), this)
        rvDocSearch.adapter = docAdapter
    }

    override fun onDocClicked(pos: Int) {

    }

    fun searchDoc(query: String): MutableList<DoctorContact>{
        Log.i("SEARCH_DOC_QUERY", query)
        var docs: MutableList<DoctorContact> = mutableListOf()
        for (doc in DoctorContact.allDoctorCon){
            if (doc.email!!.contains(query) || doc.name!!.contains(query) || doc.phone!!.contains(query))
                docs.add(doc)
        }
        return docs
    }

}