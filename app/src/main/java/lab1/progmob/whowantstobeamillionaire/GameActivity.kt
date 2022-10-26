package lab1.progmob.whowantstobeamillionaire

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.content.ContextCompat
import lab1.progmob.whowantstobeamillionaire.databinding.ActivityGameBinding
import lab1.progmob.whowantstobeamillionaire.model.Question
import lab1.progmob.whowantstobeamillionaire.model.Settings
import kotlin.random.Random

class GameActivity : BaseActivity() {

    private lateinit var binding: ActivityGameBinding

    private lateinit var settings: Settings

    private var questionsList: MutableList<Question> = mutableListOf()

    private var winsum: Int = 60000
    private var incrementor: Int = 10000
    private var step: Int = 10000
    private var bigwin:Int = 1000000

    private lateinit var takenQuestions: MutableList<Question>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater).also { setContentView(it.root) }

        settings = intent.getParcelableExtra(EXTRA_SETTINGS) ?:
            throw IllegalArgumentException("Can't launch GameActivity without options")

        if (!settings.is50Enabled) {
            binding.fifFifButton.isEnabled = false
            binding.fifFifButton.alpha = .5f
            binding.fifFifButton.isClickable = false
        }

        fillQuestionAnswers()
        showQuestions()

        binding.answerButtonA.setOnClickListener { view -> onAnswerSelected(view) }
        binding.answerButtonB.setOnClickListener { view -> onAnswerSelected(view) }
        binding.answerButtonC.setOnClickListener { view -> onAnswerSelected(view) }
        binding.answerButtonD.setOnClickListener { view -> onAnswerSelected(view) }

        binding.fifFifButton.setOnClickListener { onFifFifPressed() }
        binding.takeMoneyButton.setOnClickListener { onTakeMoneyPressed() }
    }

    // TODO: Implement screen rotation and save current question

    // getIdentifier -- is really slow, because it uses heavy reflection
    // Link: https://stackoverflow.com/q/5904554
    private fun fillQuestionAnswers() {
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

        takenQuestions = questionsList.shuffled().take(settings.questionsCount).toMutableList()
    }

    private fun showQuestions() {
        binding.questionTv.text = takenQuestions[0].question

        binding.answerButtonA.isEnabled = true
        binding.answerButtonA.alpha = 1f
        binding.answerButtonA.isClickable = true
        binding.answerButtonB.isEnabled = true
        binding.answerButtonB.alpha = 1f
        binding.answerButtonB.isClickable = true
        binding.answerButtonC.isEnabled = true
        binding.answerButtonC.alpha = 1f
        binding.answerButtonC.isClickable = true
        binding.answerButtonD.isEnabled = true
        binding.answerButtonD.alpha = 1f
        binding.answerButtonD.isClickable = true

        binding.answerButtonA.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500))
        binding.answerButtonB.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500))
        binding.answerButtonC.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500))
        binding.answerButtonD.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500))

        when (Random.nextInt(4)) {
            0 -> {
                binding.answerButtonA.text = takenQuestions[0].answer
                binding.answerButtonA.tag = 1

                binding.answerButtonB.text = takenQuestions[0].option_1
                binding.answerButtonC.text = takenQuestions[0].option_2
                binding.answerButtonD.text = takenQuestions[0].option_3

                binding.answerButtonB.tag = 0
                binding.answerButtonC.tag = 0
                binding.answerButtonD.tag = 0
            }
            1 -> {
                binding.answerButtonB.text = takenQuestions[0].answer
                binding.answerButtonB.tag = 1

                binding.answerButtonA.text = takenQuestions[0].option_2
                binding.answerButtonC.text = takenQuestions[0].option_1
                binding.answerButtonD.text = takenQuestions[0].option_3

                binding.answerButtonA.tag = 0
                binding.answerButtonC.tag = 0
                binding.answerButtonD.tag = 0
            }
            2 -> {
                binding.answerButtonC.text = takenQuestions[0].answer
                binding.answerButtonC.tag = 1

                binding.answerButtonA.text = takenQuestions[0].option_3
                binding.answerButtonB.text = takenQuestions[0].option_2
                binding.answerButtonD.text = takenQuestions[0].option_1

                binding.answerButtonA.tag = 0
                binding.answerButtonB.tag = 0
                binding.answerButtonD.tag = 0
            }
            3 -> {
                binding.answerButtonD.text = takenQuestions[0].answer
                binding.answerButtonD.tag = 1

                binding.answerButtonA.text = takenQuestions[0].option_3
                binding.answerButtonB.text = takenQuestions[0].option_1
                binding.answerButtonC.text = takenQuestions[0].option_2

                binding.answerButtonA.tag = 0
                binding.answerButtonB.tag = 0
                binding.answerButtonC.tag = 0
            }
        }

        takenQuestions.removeAt(0)
    }

    // Button delay: https://stackoverflow.com/q/61023968
    private fun onAnswerSelected(view: View) {
        if (view.tag as Int == 1) {
            view.setBackgroundColor(Color.GREEN)
            winsum += incrementor
            incrementor += step

            binding.prizeSumTv.text = getString(R.string.prize, winsum)

            // TODO: Maybe disable button because I can click several times and increase prize
            // A little delay to show the correct answer green
            if (takenQuestions.isEmpty()) {
                winsum += bigwin - winsum
                binding.prizeSumTv.text = getString(R.string.prize, winsum)
                onTakeMoneyPressed()
            } else {
                val dialog = AlertDialog.Builder(this)
                    .setTitle(getString(R.string.next_title))
                    .setMessage(getString(R.string.next_or_take))
                    .setCancelable(false)
                    .setPositiveButton(R.string.next_question) {_, _ -> showQuestions() }
                    .setNegativeButton(R.string.take_money) {_, _ -> onTakeMoneyPressed()}
                    .create()
                dialog.show()
            }

        } else {
            view.setBackgroundColor(Color.RED)
            val dialog = AlertDialog.Builder(this)
                //.setTitle("ВИ ПРОГРАЛИ")
                .setMessage(getString(R.string.fail))
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok) {_, _ -> finish() }
                .create()
            dialog.show()
        }
    }

    private fun onFifFifPressed() {
        if (binding.fifFifButton.isEnabled) {
            binding.fifFifButton.isEnabled = false
            binding.fifFifButton.alpha = .5f
            binding.fifFifButton.isClickable = false

            if (binding.answerButtonA.tag == 1 || binding.answerButtonD.tag == 1) {
                binding.answerButtonB.isEnabled = false
                binding.answerButtonB.alpha = 0.5f
                binding.answerButtonB.isClickable = false

                binding.answerButtonC.isEnabled = false
                binding.answerButtonC.alpha = 0.5f
                binding.answerButtonC.isClickable = false
            } else if (binding.answerButtonB.tag == 1 || binding.answerButtonC.tag == 1) {
                binding.answerButtonA.isEnabled = false
                binding.answerButtonA.alpha = 0.5f
                binding.answerButtonA.isClickable = false

                binding.answerButtonD.isEnabled = false
                binding.answerButtonD.alpha = 0.5f
                binding.answerButtonD.isClickable = false
            }
        }
    }

    private fun onTakeMoneyPressed() {
        val dialog = AlertDialog.Builder(this)
                .setTitle(getString(R.string.victory_title))
                .setMessage(binding.prizeSumTv.text)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok) {_, _ -> finish() }
                .create()
        dialog.show()
    }

    companion object {
        @JvmStatic val EXTRA_SETTINGS = "EXTRA_SETTINGS"
    }
}
