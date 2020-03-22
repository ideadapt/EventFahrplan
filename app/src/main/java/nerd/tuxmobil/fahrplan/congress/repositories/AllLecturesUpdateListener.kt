package nerd.tuxmobil.fahrplan.congress.repositories

import nerd.tuxmobil.fahrplan.congress.models.Lecture


/**
 * Listens for any updates to the content of lectures.
 */
interface AllLecturesUpdateListener : UpdateListener<List<Lecture>> {
    fun getDayIndex(): Int
}
