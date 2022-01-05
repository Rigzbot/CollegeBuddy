package com.example.collegebuddy.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

private const val ENROLMENT = "enrolment"
private const val IMAGE = "image"
private const val ID_CARD = "idCard"

object SavedPreference {
    private fun getSharedPreference(ctx: Context?): SharedPreferences? {
        return PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    private fun editor(context: Context, const: String, string: String) {
        getSharedPreference(
            context
        )?.edit()?.putString(const, string)?.apply()
    }

    fun setEnrolment(context: Context, enrolment: String) {
        editor(
            context,
            ENROLMENT,
            enrolment
        )
    }

    fun getEnrolment(context: Context) = getSharedPreference(context)?.getString(ENROLMENT, "")

    fun setImage(context: Context, image: String) {
        editor(
            context,
            IMAGE,
            image
        )
    }

    fun getImage(context: Context) = getSharedPreference(context)?.getString(IMAGE, "")

    fun setIdCard(context: Context, idCard: String) {
        editor(
            context,
            ID_CARD,
            idCard
        )
    }

    fun getIdCard(context: Context) = getSharedPreference(context)?.getString(ID_CARD, "")
}