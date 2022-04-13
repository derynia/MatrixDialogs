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

    fun saveCurrentSelected(langSelected: LanguageSelected) {
        with(sharedPreferences.edit()) {
            putString(SELECTED_KEY, Gson().toJson(langSelected))
            this.apply()
        }
    }

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
    }
}