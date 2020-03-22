package nerd.tuxmobil.fahrplan.congress.repositories

abstract class UpdateProvider<T> {
    private val listeners = mutableSetOf<UpdateListener<T>>()

    fun add(listener: UpdateListener<T>) {
        if (listeners.add(listener)) {
            initialUpdate(listener)
        }
    }

    fun remove(listener: UpdateListener<T>) {
        listeners.remove(listener)
    }

    fun updateAll() {
        listeners.forEach { update(it) }
    }

    abstract fun initialUpdate(listener: UpdateListener<T>)
    abstract fun update(listener: UpdateListener<T>)
}