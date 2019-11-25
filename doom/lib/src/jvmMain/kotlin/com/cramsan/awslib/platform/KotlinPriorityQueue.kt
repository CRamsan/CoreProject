package com.cramsan.awslib.platform

import java.util.PriorityQueue

actual class KotlinPriorityQueue<T> actual constructor(comparator: Comparator<T>) {

    private var queue: PriorityQueue<T>

    init {
        val jvmComparator = java.util.Comparator<T> { o1, o2 -> comparator.compare(o1, o2) }
        queue = PriorityQueue(1, jvmComparator)
    }

    actual fun add(e: T): Boolean {
        return queue.add(e)
    }

    actual fun offer(e: T): Boolean {
        return queue.offer(e)
    }

    actual fun remove(): T {
        return queue.remove()
    }

    actual fun poll(): T {
        return queue.poll()
    }

    actual fun element(): T {
        return queue.element()
    }

    actual fun peek(): T {
        return queue.peek()
    }

    actual fun size(): Int {
        return queue.size
    }

    actual fun remove(e: T): Boolean {
        return queue.remove(e)
    }

    actual operator fun contains(e: T): Boolean {
        return queue.contains(e)
    }
}