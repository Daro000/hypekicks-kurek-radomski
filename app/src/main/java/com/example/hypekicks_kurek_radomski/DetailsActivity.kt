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


        val sneaker = intent.getSerializableExtra("sneaker") as? Sneaker

        if (sneaker != null) {

            binding.txtDetailsBrand.text = sneaker.brand
            binding.txtDetailsModel.text = sneaker.modelName
            binding.txtDetailsPrice.text = String.format("%.2f PLN", sneaker.resellPrice)


            Glide.with(this)
                .load(sneaker.imageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_delete)
                .into(binding.imgDetails)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}