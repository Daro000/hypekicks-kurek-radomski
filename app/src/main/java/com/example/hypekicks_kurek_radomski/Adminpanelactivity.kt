package com.example.hypekicks_kurek_radomski

import android.R
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hypekicks_kurek_radomski.databinding.ActivityAdminPanelBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class AdminPanelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminPanelBinding

    private val db = Firebase.firestore

    private val sneakerDisplayList = mutableListOf<String>()

    private val sneakerIdList = mutableListOf<String>()

    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminPanelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ArrayAdapter(
            this,
            R.layout.simple_list_item_1,
            sneakerDisplayList
        )
        binding.listViewSneakers.adapter = adapter

        setupClickListeners()

        fetchSneakersRealtime()
    }


    // pobieranie danych irt

    private fun fetchSneakersRealtime() {
        db.collection("sneakers")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w("HypeKicks", "Błąd nasłuchiwania Firestore", error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    sneakerDisplayList.clear()
                    sneakerIdList.clear()

                    for (doc in snapshot) {
                        val brand = doc.getString("brand") ?: "?"
                        val model = doc.getString("modelName") ?: "?"
                        val year = doc.getLong("releaseYear") ?: 0
                        val price = doc.getDouble("resellPrice") ?: 0.0

                        sneakerDisplayList.add("👟 $brand $model ($year) — ${price.toInt()} PLN")
                        sneakerIdList.add(doc.id)
                    }

                    adapter.notifyDataSetChanged()
                }
            }
    }


    private fun setupClickListeners() {

        // nowy but
        binding.btnAddSneaker.setOnClickListener {
            val brand = binding.editBrand.text.toString().trim()
            val modelName = binding.editModelName.text.toString().trim()
            val releaseYearStr = binding.editReleaseYear.text.toString().trim()
            val resellPriceStr = binding.editResellPrice.text.toString().trim()
            val imageUrl = binding.editImageUrl.text.toString().trim()

            // walidacja
            if (brand.isEmpty() || modelName.isEmpty() || releaseYearStr.isEmpty()
                || resellPriceStr.isEmpty() || imageUrl.isEmpty()
            ) {
                Toast.makeText(this, "⚠️ Wypełnij wszystkie pola!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val releaseYear = releaseYearStr.toLongOrNull()
            val resellPrice = resellPriceStr.toDoubleOrNull()

            if (releaseYear == null || resellPrice == null) {
                Toast.makeText(this, "⚠️ Rok i cena muszą być liczbami!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            addSneakerToFirestore(brand, modelName, releaseYear, resellPrice, imageUrl)
        }

        // edycja
        binding.listViewSneakers.setOnItemClickListener { _, _, position, _ ->
            val selectedId = sneakerIdList[position]

            val input = android.widget.EditText(this).apply {
                hint = "Nowa cena (PLN)"
                inputType = android.text.InputType.TYPE_CLASS_NUMBER or
                        android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
                setPadding(48, 24, 48, 24)
            }

            android.app.AlertDialog.Builder(this)
                .setTitle("Edytuj cenę resell")
                .setView(input)
                .setPositiveButton("ZAKTUALIZUJ") { _, _ ->
                    val newPrice = input.text.toString().trim().toDoubleOrNull()
                    if (newPrice != null) {
                        db.collection("sneakers").document(selectedId)
                            .update("resellPrice", newPrice)
                            .addOnSuccessListener {
                                android.widget.Toast.makeText(this, "Cena zaktualizowana!", android.widget.Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .setNegativeButton("ANULUJ", null)
                .show()
        }

        // usuniecie
        binding.listViewSneakers.setOnItemLongClickListener { _, _, position, _ ->
            val selectedId = sneakerIdList[position]
            db.collection("sneakers").document(selectedId)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Dokument usunięty!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Log.e("HypeKicks", "Błąd usuwania", e)
                }
            return@setOnItemLongClickListener true
        }
    }


    // dodanie nowego dokumentu do kolekcji

    private fun addSneakerToFirestore(
        brand: String,
        modelName: String,
        releaseYear: Long,
        resellPrice: Double,
        imageUrl: String
    ) {
        // klucz wartosc
        val sneakerMap = hashMapOf(
            "brand" to brand,
            "modelName" to modelName,
            "releaseYear" to releaseYear,
            "resellPrice" to resellPrice,
            "imageUrl" to imageUrl
        )

        db.collection("sneakers")
            .add(sneakerMap)
            .addOnSuccessListener {
                Toast.makeText(this, "✅ But dodany do bazy!", Toast.LENGTH_SHORT).show()
                clearForm()
            }
            .addOnFailureListener { e ->
                Log.e("HypeKicks", "Błąd dodawania buta", e)
                Toast.makeText(this, "❌ Błąd: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }


    // czyszczenie formularza po dodaniu buta

    private fun clearForm() {
        binding.editBrand.text?.clear()
        binding.editModelName.text?.clear()
        binding.editReleaseYear.text?.clear()
        binding.editResellPrice.text?.clear()
        binding.editImageUrl.text?.clear()
    }
}