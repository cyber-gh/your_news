package dev.skyit.yournews.ui.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter<T, VB : ViewDataBinding>(
    private val binderCreator: (LayoutInflater) -> VB,
    private val injectData: VB.(data: T) -> Unit,
    private var itemsList: ArrayList<T> = arrayListOf(),
    private val onItemClick: ((T) -> Unit)? = null
) : RecyclerView.Adapter<RecyclerAdapter<T, VB>.RecyclerViewHolder>() {
    inner class RecyclerViewHolder(private val binding: VB) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: T) {
            binding.injectData(data)
            binding.root.setOnClickListener {
                onItemClick?.invoke(data)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = binderCreator(inflater)
        return RecyclerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemsList.count()
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val data = itemsList[position]
        holder.bind(data)
    }

    fun updateData(newData: ArrayList<T>) {
        this.itemsList = newData
        notifyDataSetChanged()
    }
}
