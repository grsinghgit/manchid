package com.gr.manchid.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gr.manchid.R
import android.view.*
import com.gr.manchid.data.CarouselItem

class CarouselAdapter(private val list: List<CarouselItem>) :
    RecyclerView.Adapter<CarouselAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvTitle)

        val f1: TextView = view.findViewById(R.id.tvF1)
        val f2: TextView = view.findViewById(R.id.tvF2)
        val f3: TextView = view.findViewById(R.id.tvF3)
        val f4: TextView = view.findViewById(R.id.tvF4)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_carousel, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = list[position % list.size]

        holder.title.text = item.title

        val context = holder.itemView.context

        holder.f1.text = context.getString(R.string.feature_format, item.f1)
        holder.f2.text = context.getString(R.string.feature_format, item.f2)
        holder.f3.text = context.getString(R.string.feature_format, item.f3)
        holder.f4.text = context.getString(R.string.feature_format, item.f4)
    }

}
