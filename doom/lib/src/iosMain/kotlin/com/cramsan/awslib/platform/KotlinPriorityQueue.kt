package com.cramsan.awslib.platform

// TODO: Implement solution for iOS Priority Queue
actual class KotlinPriorityQueue<T> actual constructor(comparator: Comparator<T>) {

    actual fun add(e: T) = false

    actual fun offer(e: T) = false

    actual fun remove(): T = TODO("Not Implemented")

    actual fun poll(): T? = TODO("Not Implemented")

    actual fun element(): T = TODO("Not Implemented")

    actual fun peek(): T? = TODO("Not Implemented")

    actual fun size() = 0

    actual fun remove(e: T) = false

    actual operator fun contains(e: T) = false
}
