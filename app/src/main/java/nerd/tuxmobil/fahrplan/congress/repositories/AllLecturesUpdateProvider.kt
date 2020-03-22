package nerd.tuxmobil.fahrplan.congress.repositories

import nerd.tuxmobil.fahrplan.congress.models.Lecture

class AllLecturesUpdateProvider(private val repo: AppRepository) : UpdateProvider<List<Lecture>>() {

    override fun initialUpdate(listener: UpdateListener<List<Lecture>>) {
        val l: AllLecturesUpdateListener = listener as AllLecturesUpdateListener
        listener.onUpdate(repo.loadUncanceledLecturesForDayIndex(l.getDayIndex()))
    }

    override fun update(listener: UpdateListener<List<Lecture>>) {
        val l: AllLecturesUpdateListener = listener as AllLecturesUpdateListener
        listener.onUpdate(repo.loadUncanceledLecturesForDayIndex(l.getDayIndex()))
    }
}