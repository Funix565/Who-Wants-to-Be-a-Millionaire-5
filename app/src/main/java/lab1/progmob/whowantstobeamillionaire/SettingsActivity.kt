package lab1.progmob.whowantstobeamillionaire

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import lab1.progmob.whowantstobeamillionaire.databinding.ActivitySettingsBinding
import lab1.progmob.whowantstobeamillionaire.model.Settings

class SettingsActivity : BaseActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private lateinit var settings: Settings

    private lateinit var questionsNumList: List<Int>

    private lateinit var adapter: ArrayAdapter<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater).also { setContentView(it.root) }

        // 1. Get saved settings from state when we rotate screen
        // or
        // 2. Get settings from caller activity intent
        settings = savedInstanceState?.getParcelable(KEY_SETTINGS) ?:
                    intent.getParcelableExtra(EXTRA_SETTINGS) ?:
                    throw IllegalArgumentException("You need to specify EXTRA_SETTINGS argument to launch this activity")

        setupSpinner()
        setupCheckbox()
        updateUi()

        binding.saveButton.setOnClickListener { onSavePressed() }
    }

    private fun setupCheckbox() {
        // Set listener to update settings
        binding.checkbox50.setOnClickListener {
            settings = settings.copy(is50Enabled = binding.checkbox50.isChecked)
        }
    }

    private fun setupSpinner() {
        questionsNumList = IntArray(6) {it + 5}.toList()
        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            questionsNumList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)

        binding.numqSpinner.adapter = adapter

        // Set listener to update settings
        binding.numqSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) { }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val count = questionsNumList[p2]
                settings = settings.copy(questionsCount = count)
            }
        }
    }

    private fun updateUi() {
        binding.checkbox50.isChecked = settings.is50Enabled
        binding.numqSpinner.setSelection(questionsNumList.indexOfFirst { it == settings.questionsCount })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_SETTINGS, settings)
    }

    // We don't collect user data when this button pressed.
    // We rather update the settings every time a user changes the settings parameters themselves via Listeners
    private fun onSavePressed() {
        val intent = Intent()
        intent.putExtra(EXTRA_SETTINGS, settings)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    // It is responsible for saving and passing settings
    companion object {
        @JvmStatic val EXTRA_SETTINGS = "EXTRA_SETTINGS"
        @JvmStatic private val KEY_SETTINGS = "KEY_SETTINGS"
    }
}
