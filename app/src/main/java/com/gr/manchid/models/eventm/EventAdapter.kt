package com.gr.manchid.models.eventm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gr.manchid.data.repository.event.EventData
import com.gr.manchid.databinding.ItemEventBinding


class EventAdapter : RecyclerView.Adapter<EventAdapter.EventVH>() {

    private val list = ArrayList<EventData>()

    fun submitList(newList: List<EventData>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    class EventVH(val binding: ItemEventBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventVH {
        val binding = ItemEventBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return EventVH(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: EventVH, position: Int) {
        val item = list[position]

        holder.binding.txtTitle.text = item.title
        holder.binding.txtDate.text = item.date
        holder.binding.txtLocation.text = item.location
    }
}
