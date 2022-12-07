package lab1.progmob.whowantstobeamillionaire

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import lab1.progmob.whowantstobeamillionaire.model.Question
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// TODO: Use ExecutorService
class LoadQuestionsFromFileService : Service() {

    private lateinit var executorService: ExecutorService

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("LAB3", "LoadQuestionsFromFileService in onStartCommand")
        Log.d("LAB3", "Executed on: ${Thread.currentThread().id}")

        val action = intent!!.action

        executorService = Executors.newSingleThreadExecutor()

        when (action) {
            ACTION_LOAD_QUESTIONS -> {
                // The service runs in a separate thread
                executorService.submit {
                    Log.d("LAB3", "LoadQuestionsFromFileService in executorService.submit")
                    Log.d("LAB3", "Executed on: ${Thread.currentThread().id}")
                    loadQuestions()
                }
            }
        }

        return START_NOT_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun loadQuestions() {
        Log.d("LAB3", "LoadQuestionsFromFileService in loadQuestions")
        Log.d("LAB3", "Executed on: ${Thread.currentThread().id}")

        val app:App = application as App

        // Load questions in this variable
        val questionsList: MutableList<Question> = mutableListOf()

        try {
            // Simulate long run
            Thread.sleep(2000)

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

        executorService.shutdown()
        stopSelf()
    }

    companion object {
        const val ACTION_LOAD_QUESTIONS = BuildConfig.APPLICATION_ID + ".ACTION_LOAD_QUESTIONS"
    }
}
