package lab1.progmob.whowantstobeamillionaire.contract

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import lab1.progmob.whowantstobeamillionaire.model.Settings

typealias ResultListener<T> = (T) -> Unit

// Extension method. Every fragment has access to navigator
fun Fragment.navigator() : Navigator {
    return requireActivity() as Navigator
}

interface Navigator {

    fun showGameScreen(settings: Settings)

    fun showSettingsScreen(settings: Settings)

    fun goBack()

    fun goToMenu()

    fun <T: Parcelable> publishResult(result: T)

    fun <T: Parcelable> listenResult(clazz: Class<T>, owner: LifecycleOwner, listener: ResultListener<T>)
}