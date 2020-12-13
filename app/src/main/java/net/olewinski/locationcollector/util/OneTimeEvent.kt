package net.olewinski.locationcollector.util

open class OneTimeEvent<out T>(private val content: T) {
    private var hasBeenHandled = false

    fun getContent(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
}
