package com.example.hypekicks_kurek_radomski

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.example.hypekicks_kurek_radomski.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val db = FirebaseFirestore.getInstance()
    private var allSneakers = mutableListOf<Sneaker>()
    private lateinit var adapter: SneakerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        adapter = SneakerAdapter(this, allSneakers)
        binding.gridView.adapter = adapter

        fetchSneakers()


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("HypeKicks", "Szukana fraza: $newText")
                filterList(newText)
                return true
            }
        })

        binding.btnGoToAdmin.setOnClickListener {
            startActivity(Intent(this, AdminPanelActivity::class.java))
        }

        binding.gridView.setOnItemClickListener { _, _, position, _ ->
            val selectedSneaker = adapter.getItem(position) as Sneaker
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("sneaker", selectedSneaker)
            startActivity(intent)
        }
    }

    private fun fetchSneakers() {
        db.collection("sneakers").addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.e("HypeKicks", "Błąd Firestore: ${e.message}")
                return@addSnapshotListener
            }

            if (snapshot != null) {
                allSneakers.clear()
                for (doc in snapshot) {
                    val sneaker = doc.toObject(Sneaker::class.java).copy(id = doc.id)
                    allSneakers.add(sneaker)
                }
                Log.d("HypeKicks", "Pobrano ${allSneakers.size} butów")
                adapter.updateList(allSneakers)
            }
        }
    }

    private fun filterList(query: String?) {
        val filtered = if (query.isNullOrEmpty()) {
            allSneakers.toList()
        } else {
            allSneakers.filter {
                it.modelName.lowercase().contains(query.lowercase()) ||
                it.brand.lowercase().contains(query.lowercase())
            }
        }
        Log.d("HypeKicks", "Wyniki po filtrze: ${filtered.size}")
        adapter.updateList(filtered)
    }

    override fun onResume() {
        super.onResume()
        binding.searchView.setQuery("", false)
        binding.searchView.clearFocus()
    }
}