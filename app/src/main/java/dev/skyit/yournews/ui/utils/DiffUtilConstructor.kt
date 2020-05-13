package dev.skyit.yournews.ui.utils

import androidx.recyclerview.widget.DiffUtil


fun <T>buildDiffUttil(idKey: T.() -> Any): DiffUtil.ItemCallback<T> {
    return object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem.idKey() == newItem.idKey()
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }

    }
}