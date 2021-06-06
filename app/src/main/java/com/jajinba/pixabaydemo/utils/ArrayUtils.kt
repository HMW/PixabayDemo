package com.jajinba.pixabaydemo.utils

object ArrayUtils {
    fun getLengthSafe(collection: MutableCollection<*>?): Int {
        return collection?.size ?: 0
    }

    fun isEmpty(collection: MutableCollection<*>?): Boolean {
        return getLengthSafe(collection) == 0
    }

    fun isNotEmpty(collection: MutableCollection<*>?): Boolean {
        return getLengthSafe(collection) > 0
    }
}