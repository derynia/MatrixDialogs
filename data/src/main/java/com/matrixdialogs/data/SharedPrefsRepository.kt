package com.matrixdialogs.data

import android.content.Context
import com.google.gson.Gson
import com.matrixdialogs.data.dataclass.LanguageSelected
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPrefsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val sharedPreferences
        get() = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private fun putPrefByKey(key: String, value: String) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            this.apply()
        }
    }

    fun saveCurrentSelected(langSelected: LanguageSelected) {
        putPrefByKey(SELECTED_KEY, Gson().toJson(langSelected))
    }

    fun saveRepeats(repeats: Int) {
        putPrefByKey(REPEATS_KEY, repeats.toString())
    }

    fun getRepeats() : Int = sharedPreferences.getString(
            REPEATS_KEY,
            DEF_REPEATS
        )?.toInt() ?: 0

    fun getCurrentSelected() : LanguageSelected? {
        val json = sharedPreferences.getString(
            SELECTED_KEY,
            null
        )

        return when {
            json.isNullOrBlank() -> null
            else -> Gson().fromJson(json, LanguageSelected::class.java)
        }
    }

    companion object {
        private const val PREFS_NAME = "MatrixDialogs"
        private const val SELECTED_KEY = "language_selected_key"
        private const val REPEATS_KEY = "repeats_key"
        private const val DEF_REPEATS = "20"
    }
}