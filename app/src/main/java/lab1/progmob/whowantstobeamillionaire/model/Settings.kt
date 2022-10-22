package lab1.progmob.whowantstobeamillionaire.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Settings(
    val questionsCount: Int,
    val is50Enabled: Boolean
) : Parcelable {

    // We do not have static fields in Kotlin
    // Members of the companion object can be called simply by using the class name as the qualifier
    companion object {
        @JvmStatic val DEFAULT = Settings(questionsCount = 5, is50Enabled = false)
    }
}
