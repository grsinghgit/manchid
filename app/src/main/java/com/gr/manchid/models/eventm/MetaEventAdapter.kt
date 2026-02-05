package com.gr.manchid.models.eventm



import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gr.manchid.data.repository.event.MetaEvent
import com.gr.manchid.databinding.ItemMetaBinding


class MetaEventAdapter(
    private val onDeleteClick: (MetaEvent) -> Unit,
    private val onEditClick: (MetaEvent) -> Unit
) : RecyclerView.Adapter<MetaEventAdapter.MetaViewHolder>() {

    private val list = ArrayList<MetaEvent>()

    // Role Control
    var showDelete = true
    var showEdit = true

    // ⭐ ViewHolder
    inner class MetaViewHolder(val binding: ItemMetaBinding) :
        RecyclerView.ViewHolder(binding.root)

    // ⭐ Create ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MetaViewHolder {

        val binding = ItemMetaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MetaViewHolder(binding)
    }

    // ⭐ Bind Data
    override fun onBindViewHolder(holder: MetaViewHolder, position: Int) {

        val item = list[position]

        holder.binding.dateText.text = item.date
        holder.binding.programText.text = item.program

        // Delete Visibility
        holder.binding.deleteBtn.visibility =
            if (showDelete) android.view.View.VISIBLE
            else android.view.View.GONE

        // Edit Visibility
        holder.binding.editBtn.visibility =
            if (showEdit) android.view.View.VISIBLE
            else android.view.View.GONE

        // Delete Click
        holder.binding.deleteBtn.setOnClickListener {
            onDeleteClick(item)
        }

        // Edit Click
        holder.binding.editBtn.setOnClickListener {
            onEditClick(item)
        }
    }

    override fun getItemCount(): Int = list.size

    // ⭐ Submit List
    fun submitList(newList: List<MetaEvent>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}
