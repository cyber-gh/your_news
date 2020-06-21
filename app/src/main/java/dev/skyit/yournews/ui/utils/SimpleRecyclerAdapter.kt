package dev.skyit.yournews.ui.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView




class SimpleRecyclerAdapter<T, VB : ViewDataBinding>(
    private val binderCreator: (LayoutInflater) -> VB, //TODO get rid of this somehow
    private val injectData: VB.(data: T) -> Unit,
    private val itemsList: ArrayList<T> = arrayListOf(),
    private val onItemClick: ((T) -> Unit)? = null,
    private val idKey: (T.() -> Any)? = null
) : RecyclerView.Adapter<SimpleRecyclerAdapter<T, VB>.RecyclerViewHolder>() {
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

    private inner class Diff(private val oldList: List<T>, private val newList: List<T>) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val key = idKey ?: return false
            return oldList[oldItemPosition].key() == newList[newItemPosition].key()

        }

        override fun getOldListSize(): Int  = oldList.count()

        override fun getNewListSize(): Int = newList.count()

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].hashCode() == newList[newItemPosition].hashCode()
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
        if (idKey == null) {
            itemsList.clear()
            itemsList.addAll(newData)
            notifyDataSetChanged()
        } else {
            updateData(newData, Diff(itemsList, newData))
        }
    }

    private fun updateData(newData: ArrayList<T>, diff: Diff) {
        val diffResult = DiffUtil.calculateDiff(diff)
        itemsList.clear()
        itemsList.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }
}
