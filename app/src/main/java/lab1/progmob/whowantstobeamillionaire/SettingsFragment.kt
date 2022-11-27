package lab1.progmob.whowantstobeamillionaire

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import lab1.progmob.whowantstobeamillionaire.contract.CustomAction
import lab1.progmob.whowantstobeamillionaire.contract.HasCustomAction
import lab1.progmob.whowantstobeamillionaire.contract.HasCustomTitle
import lab1.progmob.whowantstobeamillionaire.contract.navigator
import lab1.progmob.whowantstobeamillionaire.databinding.FragmentSettingsBinding
import lab1.progmob.whowantstobeamillionaire.model.Settings

class SettingsFragment : Fragment(), HasCustomTitle, HasCustomAction {

    private lateinit var binding: FragmentSettingsBinding

    private lateinit var settings: Settings

    private lateinit var questionsNumList: List<Int>

    private lateinit var adapter: ArrayAdapter<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = savedInstanceState?.getParcelable<Settings>(KEY_SETTINGS) ?:
            arguments?.getParcelable(ARG_SETTINGS) ?:
                throw IllegalArgumentException("You need to specify SETTINGS to launch this fragment")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        setupSpinner()
        setupCheckbox()
        updateUi()

        binding.saveButton.setOnClickListener { onSavePressed() }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_SETTINGS, settings)
    }

    override fun getTitleRes(): Int = R.string.settings

    override fun getCustomAction(): CustomAction {
        return CustomAction(
            iconRes = R.drawable.ic_done,
            textRes = R.string.submit_settings,
            onCustomAction = Runnable {
                onSavePressed()
            }
        )
    }

    private fun setupSpinner() {
        questionsNumList = IntArray(6) {it + 5}.toList()
        adapter = ArrayAdapter(
            requireActivity(),
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

    private fun setupCheckbox() {
        // Set listener to update settings
        binding.checkbox50.setOnClickListener {
            settings = settings.copy(is50Enabled = binding.checkbox50.isChecked)
        }
    }

    private fun updateUi() {
        binding.checkbox50.isChecked = settings.is50Enabled
        binding.numqSpinner.setSelection(questionsNumList.indexOfFirst { it == settings.questionsCount })
    }

    private fun onSavePressed() {
        navigator().publishResult(settings)
        navigator().goBack()
    }

    companion object {
        @JvmStatic private val ARG_SETTINGS = "ARG_SETTINGS"
        @JvmStatic private val KEY_SETTINGS = "KEY_SETTINGS"

        @JvmStatic
        fun newInstance(settings: Settings): SettingsFragment {
            val args = Bundle()
            args.putParcelable(ARG_SETTINGS, settings)
            val fragment = SettingsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}