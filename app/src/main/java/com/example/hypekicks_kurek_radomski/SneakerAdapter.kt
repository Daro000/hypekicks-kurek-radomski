package com.example.hypekicks_kurek_radomski

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import com.example.hypekicks_kurek_radomski.databinding.ItemSneakerBinding

class SneakerAdapter(private val context: Context, private var sneakerList: List<Sneaker>) : BaseAdapter() {

    override fun getCount(): Int = sneakerList.size

    override fun getItem(position: Int): Any = sneakerList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: ItemSneakerBinding
        val view: View

        if (convertView == null) {
            binding = ItemSneakerBinding.inflate(LayoutInflater.from(context), parent, false)
            view = binding.root
            view.tag = binding
        } else {
            binding = convertView.tag as ItemSneakerBinding
            view = convertView
        }

        val sneaker = sneakerList[position]

        binding.txtBrand.text = sneaker.brand
        binding.txtModel.text = sneaker.modelName

        Glide.with(context)
            .load(sneaker.imageUrl)
            .into(binding.imgSneaker)

        return view
    }

    fun updateList(newList: List<Sneaker>) {
        sneakerList = newList
        notifyDataSetChanged()
    }
}