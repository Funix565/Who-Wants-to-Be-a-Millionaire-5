package lab1.progmob.whowantstobeamillionaire

import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    // We need this for the correct work of the Back-Arrow in children activities
    override fun onSupportNavigateUp(): Boolean {
        // Here, for example, we can implement additional logic.
        // Show a dialog to confirm back
        finish()
        return true
    }
}
