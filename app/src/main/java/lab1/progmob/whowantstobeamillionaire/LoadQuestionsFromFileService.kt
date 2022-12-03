package lab1.progmob.whowantstobeamillionaire

import android.app.Service
import android.content.Intent
import android.os.IBinder
import lab1.progmob.whowantstobeamillionaire.model.Question

// TODO: Use ExecutorService
class LoadQuestionsFromFileService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val action = intent!!.action

        when (action) {
            ACTION_LOAD_QUESTIONS -> {
                // The service runs in a separate thread
                Thread {
                    loadQuestions()
                }.start()
            }
        }

        return START_NOT_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun loadQuestions() {
        val app:App = application as App

        // Load questions in this variable
        val questionsList: MutableList<Question> = mutableListOf()

        try {
            // Simulate long run
            Thread.sleep(1000)

            val questionsAndAnswersArray: Array<String> = resources.getStringArray(R.array.questions_and_answers)

            var index = 0
            while (index < questionsAndAnswersArray.size) {
                val q = Question(questionsAndAnswersArray[index++],
                    questionsAndAnswersArray[index++],
                    questionsAndAnswersArray[index++],
                    questionsAndAnswersArray[index++],
                    questionsAndAnswersArray[index++])

                questionsList.add(q)
            }

            app.publishCompleted(questionsList)
        }
        catch (e: Exception) {
            e.printStackTrace()
        }

        stopSelf()
    }

    companion object {
        const val ACTION_LOAD_QUESTIONS = BuildConfig.APPLICATION_ID + ".ACTION_LOAD_QUESTIONS"
    }
}
