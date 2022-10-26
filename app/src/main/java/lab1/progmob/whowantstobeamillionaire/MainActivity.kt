package lab1.progmob.whowantstobeamillionaire

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import lab1.progmob.whowantstobeamillionaire.databinding.ActivityMainBinding
import lab1.progmob.whowantstobeamillionaire.model.Settings

class MainActivity : BaseActivity() {

    // We use View Binding
    private lateinit var binding: ActivityMainBinding

    private lateinit var settings : Settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also{ setContentView(it.root) }

        binding.startGameButton.setOnClickListener { onStartGamePressed() }
        binding.gameSettingsButton.setOnClickListener { onGameSettingsPressed() }

        settings = savedInstanceState?.getParcelable(KEY_SETTINGS) ?: Settings.DEFAULT
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_SETTINGS, settings)
    }

    private fun onStartGamePressed() {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra(GameActivity.EXTRA_SETTINGS, settings)
        startActivity(intent)
    }

    // Here we get the info after startActivityForResult
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SETTINGS_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            settings = data?.getParcelableExtra(SettingsActivity.EXTRA_SETTINGS) ?:
                    throw IllegalArgumentException("Can't get the updated data from settings activity")
        }
    }

    private fun onGameSettingsPressed() {
        // Create a thing and say Android that we would like to do it
        val intent = Intent(this, SettingsActivity::class.java)

        // Pass additional data to the Activity we want to start
        intent.putExtra(SettingsActivity.EXTRA_SETTINGS, settings)
        // Call startActivityForResult because we want something back after this call
        // SETTINGS_REQUEST_CODE -- We need this because we want to know, who sends the result back. Could be many activities
        startActivityForResult(intent, SETTINGS_REQUEST_CODE)
    }

    companion object {
        @JvmStatic private val KEY_SETTINGS = "SETTINGS"

        // A unique integer.
        @JvmStatic private val SETTINGS_REQUEST_CODE = 1
    }
}
