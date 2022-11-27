package lab1.progmob.whowantstobeamillionaire

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import lab1.progmob.whowantstobeamillionaire.contract.navigator
import lab1.progmob.whowantstobeamillionaire.databinding.FragmentMainBinding
import lab1.progmob.whowantstobeamillionaire.model.Settings

class MainFragment : Fragment() {

    private lateinit var settings : Settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = savedInstanceState?.getParcelable(KEY_SETTINGS) ?: Settings.DEFAULT
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater, container, false)

        navigator().listenResult(Settings::class.java, viewLifecycleOwner) {
            this.settings = it
        }

        binding.startGameButton.setOnClickListener { onStartGamePressed() }
        binding.gameSettingsButton.setOnClickListener { onGameSettingsPressed() }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_SETTINGS, settings)
    }

    private fun onStartGamePressed() {
        navigator().showGameScreen(settings)
    }

    private fun onGameSettingsPressed() {
        navigator().showSettingsScreen(settings)
    }

    companion object {
        @JvmStatic private val KEY_SETTINGS = "SETTINGS"
    }
}