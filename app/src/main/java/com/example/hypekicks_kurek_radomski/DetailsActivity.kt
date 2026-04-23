package com.example.hypekicks_kurek_radomski

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.hypekicks_kurek_radomski.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Odbieranie danych
        val sneaker = intent.getSerializableExtra("sneaker") as? Sneaker

        if (sneaker != null) {
            binding.txtDetailsBrand.text = sneaker.brand
            binding.txtDetailsModel.text = sneaker.modelName
            // Sformatowana cena
            binding.txtDetailsPrice.text = String.format("%.2f PLN", sneaker.resellPrice)

            // Wyświetlenie zdjęcia
            Glide.with(this)
                .load(sneaker.imageUrl)
                .into(binding.imgDetails)
        }


        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}