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

        // TODO: Prepare questions
        fillQuestionAnswers()
        showQuestions()

        binding.answerButtonA.setOnClickListener { view -> onAnswerSelected(view) }
        binding.answerButtonB.setOnClickListener { view -> onAnswerSelected(view) }
        binding.answerButtonC.setOnClickListener { view -> onAnswerSelected(view) }
        binding.answerButtonD.setOnClickListener { view -> onAnswerSelected(view) }

        binding.fifFifButton.setOnClickListener { onFifFifPressed() }
        binding.takeMoneyButton.setOnClickListener { onTakeMoneyPressed() }
    }

    // TODO: Implement screen rotation

    // No simple and efficient approach to iterate through a list of string resources with appending to similar name
    // string-array -- complicated and ambiguous structure
    // getIdentifier -- is really slow, because it uses heavy reflection
    // Link: https://stackoverflow.com/q/5904554

    // TODO: Anyway, try with string-array and index-padding
    // For example, question = 1, options = question + 1, question + 2, question + 3, question + 4
    private fun fillQuestionAnswers() {
        // Use view.tag for correct answer
        // For every button one listener
        val q1 = Question("Що допомагає дорослій черепасі захищатися від своїх ворогів?",
                "Панцир",
                "Шкаралупа",
                "Броня",
                "Дах")
        questionsList.add(q1)

        val q2 = Question("Хто проживав за адресою: Лондон, Бейкер стріт 221-б?",
                            "Шерлок Холмс",
                            "Еркюль Пуаро",
                            "Полковник Ісаєв",
                            "Комісар Мегре")
        questionsList.add(q2)

        val q3 = Question("Якими словами закінчується афоризм Хемінгуея «Щастя – це міцне здоров’я і ..»?",
                            "Слабка пам’ять",
                            "Хороші друзі",
                            "Великі гроші",
                            "Сильна воля")
        questionsList.add(q3)

        val q4 = Question("Ложечка з якого сплаву розплавиться під час помішування нею гарячого чаю?",
                            "Сплав Вуда",
                            "Олов’яниста бронза",
                            "Інвар",
                            "Алюмель")
        questionsList.add(q4)

        val q5 = Question("Як інакше називається алігаторова груша?",
                            "Авокадо",
                            "Папайя",
                            "Касава",
                            "Маракуйя")
        questionsList.add(q5)

        takenQuestions = questionsList.shuffled().take(settings.questionsCount).toMutableList()
    }

    private fun showQuestions() {
        binding.questionTv.text = takenQuestions[0].question
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
            Handler(Looper.getMainLooper()).postDelayed({
                if (takenQuestions.isEmpty()) {
                    onTakeMoneyPressed()
                } else {
                    showQuestions()
                }
            }, 500)
        } else {
            view.setBackgroundColor(Color.RED)
            val dialog = AlertDialog.Builder(this)
                //.setTitle("ВИ ПРОГРАЛИ")
                .setMessage("ВИ ПРОГРАЛИ")
                .setCancelable(false)
                .setPositiveButton("МЕНЮ") {_, _ -> finish() }
                .create()
            dialog.show()
        }
    }


    // TODO: Do not disable correct answer
    private fun onFifFifPressed() {
        if (binding.fifFifButton.isEnabled) {
            binding.fifFifButton.isEnabled = false
            binding.fifFifButton.alpha = .5f
            binding.fifFifButton.isClickable = false

            val v: Int = Random.nextInt(2)
            if (v == 0) {
                binding.answerButtonA.isEnabled = false
                binding.answerButtonA.alpha = 0.5f
                binding.answerButtonA.isClickable = false

                binding.answerButtonD.isEnabled = false
                binding.answerButtonD.alpha = 0.5f
                binding.answerButtonD.isClickable = false
            } else {
                binding.answerButtonB.isEnabled = false
                binding.answerButtonB.alpha = 0.5f
                binding.answerButtonB.isClickable = false

                binding.answerButtonC.isEnabled = false
                binding.answerButtonC.alpha = 0.5f
                binding.answerButtonC.isClickable = false
            }
        }
    }

    private fun onTakeMoneyPressed() {
        val dialog = AlertDialog.Builder(this)
                .setTitle("ВІТАЄМО")
                .setMessage(binding.priceSumTv.text)
                .setCancelable(false)
                .setPositiveButton("OK") {_, _ -> finish() }
                .create()
        dialog.show()
    }

//    private fun onAnswerSelected(view: View) {
//        if (view.tag as Int == correctIndex) {
//            // Fill next question
//                // if it was the last question -- show Congratulation activity or pop up or dialog
//        } else {
//            // Fail activity or pop up or dialog
//        }
//    }

    companion object {
        @JvmStatic val EXTRA_SETTINGS = "EXTRA_SETTINGS"
    }
}
