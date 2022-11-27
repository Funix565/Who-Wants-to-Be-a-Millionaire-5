package lab1.progmob.whowantstobeamillionaire

import android.app.Application
import android.os.Handler
import android.os.Looper
import lab1.progmob.whowantstobeamillionaire.model.Question

class App : Application() {

    private val listeners: MutableSet<TaskListener> = mutableSetOf()

    // In order to have access to the main thread and UI update
    private val handler = Handler(Looper.getMainLooper())

    // Call from Fragment
    fun addListener(listener: TaskListener) {
        this.listeners.add(listener)
    }

    // Call from Fragment
    fun removeListener(listener: TaskListener) {
        this.listeners.remove(listener)
    }

    // Call from Service in a separate thread
    fun publishCompleted(questions: MutableList<Question>) {
        handler.post {
            for (listener: TaskListener in listeners) {
                listener.onCompleted(questions)
            }
        }
    }
}
