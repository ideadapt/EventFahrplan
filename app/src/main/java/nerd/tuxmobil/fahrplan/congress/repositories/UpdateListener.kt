package nerd.tuxmobil.fahrplan.congress.repositories

interface UpdateListener<T> {
    fun onUpdate(data: T)
}
