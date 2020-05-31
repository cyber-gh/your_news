package dev.skyit.yournews.utils

fun <T>List<T>.toArrayList() : ArrayList<T> {
    return ArrayList(this)
}