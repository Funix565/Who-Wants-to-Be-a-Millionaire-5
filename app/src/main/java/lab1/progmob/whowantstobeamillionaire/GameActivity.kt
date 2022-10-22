package lab1.progmob.whowantstobeamillionaire

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import lab1.progmob.whowantstobeamillionaire.databinding.ActivityGameBinding
import lab1.progmob.whowantstobeamillionaire.model.Settings
import kotlin.random.Random

class GameActivity : BaseActivity() {

    private lateinit var binding: ActivityGameBinding

    private lateinit var settings: Settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater).also { setContentView(it.root) }

        settings = intent.getParcelableExtra(EXTRA_SETTINGS) ?:
            throw IllegalArgumentException("Can't launch GameActivity without options")

        // TODO: Prepare questions

        if (!settings.is50Enabled) {
            binding.fifFifButton.isEnabled = false
            binding.fifFifButton.alpha = .5f
            binding.fifFifButton.isClickable = false
        }

        fillQuestionAnswers()

        binding.fifFifButton.setOnClickListener { onFifFifPressed() }
        binding.takeMoneyButton.setOnClickListener { onTakeMoneyPressed() }
    }

    // TODO: Implement screen rotation

    private fun fillQuestionAnswers() {
        // Use view.tag for correct answer
        // For every button one listener
    }

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
                .setMessage("${binding.priceSumTv.text}")
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
