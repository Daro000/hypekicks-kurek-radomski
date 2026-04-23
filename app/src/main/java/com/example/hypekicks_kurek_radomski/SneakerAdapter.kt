package com.example.hypekicks_kurek_radomski

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class SneakerAdapter(private val context: Context, private var list: List<Sneaker>) : BaseAdapter() {
    override fun getCount() = list.size
    override fun getItem(position: Int) = list[position]
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_sneaker, parent, false)
        } else {
            view = convertView
        }
        
        val sneaker = list[position]
        
        val img = view.findViewById<ImageView>(R.id.imgSneaker)
        val brand = view.findViewById<TextView>(R.id.txtBrand)
        val model = view.findViewById<TextView>(R.id.txtModel)

        brand.text = sneaker.brand
        model.text = sneaker.modelName

        Glide.with(context)
            .load(sneaker.imageUrl)
            .placeholder(android.R.drawable.ic_menu_report_image) // Ikona podczas ładowania
            .error(android.R.drawable.ic_delete)               // Czerwony X jeśli link jest zły
            .into(img)

        return view
    }

    fun updateList(newList: List<Sneaker>) {
        this.list = newList
        notifyDataSetChanged()
    }
}