package lab1.progmob.whowantstobeamillionaire

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import lab1.progmob.whowantstobeamillionaire.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // We use View Binding
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startGame.setOnClickListener {
            onStartGamePressed()
        }

        binding.gameSettings.setOnClickListener {
            onGameSettingsPressed()
        }
    }

    private fun onStartGamePressed() {
        // TODO: Show questions
        binding.startGame.setBackgroundColor(Color.CYAN)
        binding.startGame.setText("New question")
        binding.startGame.setTextColor(Color.BLACK)
    }

    private fun onGameSettingsPressed() {
        // TODO: Go to settings
    }


}