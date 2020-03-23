package com.cramsan.awslib.platform

expect class KotlinPriorityQueue<T>(comparator: Comparator<T>) {
    fun add(e: T): Boolean
    fun offer(e: T): Boolean
    fun remove(): T
    fun remove(e: T): Boolean
    fun poll(): T
    fun element(): T
    fun peek(): T
    fun size(): Int
    operator fun contains(e: T): Boolean
}
