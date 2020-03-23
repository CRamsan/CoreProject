package com.cesarandres.ps2link.fragments

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.makeText
import com.cesarandres.ps2link.R
import com.cesarandres.ps2link.base.BaseFragment

/**
 * Fragment that will provide settings for the user
 */
class FragmentSettings : BaseFragment() {

    private var fromTimeHours: Int = 0
    private var fromTimeMinutes: Int = 0
    private var toTimeHours: Int = 0
    private var toTimeMinutes: Int = 0

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onCreateView(android.view.
     * LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.cesarandres.ps2link.base.BaseFragment#onActivityCreated(android.os
     * .Bundle)
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this.fragmentTitle.text = getString(R.string.title_settings)

        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        fromTimeHours = prefs.getInt(PREF_KEY_START_HOUR, 0)
        fromTimeMinutes = prefs.getInt(PREF_KEY_START_MINUTE, 0)
        toTimeHours = prefs.getInt(PREF_KEY_END_HOUR, 23)
        toTimeMinutes = prefs.getInt(PREF_KEY_END_MINUTE, 59)

        var checkEnabled = prefs.getBoolean(PREF_KEY_NOTIFICATION_ENABLE, false)

        // MODIFY THIS TO REENABLE NOTIFICATIONS
        checkEnabled = false

        val enabledCheckbox =
            view!!.findViewById<View>(R.id.checkBoxSettingsNotificationsEnabled) as CheckBox
        val fromTime = view!!.findViewById<View>(R.id.editTextSettingsFrom) as EditText
        val toTime = view!!.findViewById<View>(R.id.editTextSettingsTo) as EditText
        fromTime.keyListener = null
        toTime.keyListener = null

        enabledCheckbox.isChecked = checkEnabled
        fromTime.isEnabled = checkEnabled
        toTime.isEnabled = checkEnabled

        this.setTimeText(fromTime, fromTimeHours, fromTimeMinutes)
        this.setTimeText(toTime, toTimeHours, toTimeMinutes)

        enabledCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = prefs.edit()

            // MODIFY THIS TO REENABLE NOTIFICATIONS
            editor.putBoolean(PREF_KEY_NOTIFICATION_ENABLE, false)
            makeText(context, R.string.toast_push_notifications_disabled, LENGTH_LONG).show()

            editor.commit()
            enabledCheckbox.isChecked = false
        }
        fromTime.setOnClickListener {
            val tp1 = TimePickerDialog(
                context,
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    if (hourOfDay > toTimeHours || hourOfDay == toTimeHours && minute > toTimeMinutes) {
                        Toast.makeText(activity, R.string.toast_time_error, Toast.LENGTH_LONG)
                            .show()
                        return@OnTimeSetListener
                    }
                    val prefs = PreferenceManager.getDefaultSharedPreferences(context)
                    val editor = prefs.edit()
                    editor.putInt(PREF_KEY_START_HOUR, hourOfDay)
                    editor.putInt(PREF_KEY_START_MINUTE, minute)
                    editor.commit()
                    fromTimeHours = hourOfDay
                    fromTimeMinutes = minute
                    setTimeText(fromTime, hourOfDay, minute)
                },
                fromTimeHours,
                fromTimeMinutes,
                false
            )
            tp1.show()
        }
        toTime.setOnClickListener {
            val tp1 = TimePickerDialog(
                context,
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    if (hourOfDay < fromTimeHours || hourOfDay == fromTimeHours && minute < fromTimeMinutes) {
                        Toast.makeText(activity, R.string.toast_time_error, Toast.LENGTH_LONG)
                            .show()
                        return@OnTimeSetListener
                    }

                    val prefs = PreferenceManager.getDefaultSharedPreferences(context)
                    val editor = prefs.edit()
                    editor.putInt(PREF_KEY_END_HOUR, hourOfDay)
                    editor.putInt(PREF_KEY_END_MINUTE, minute)
                    editor.commit()
                    toTimeHours = hourOfDay
                    toTimeMinutes = minute
                    setTimeText(toTime, hourOfDay, minute)
                },
                toTimeHours,
                toTimeMinutes,
                false
            )
            tp1.show()
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onResume()
     */
    override fun onResume() {
        super.onResume()
    }

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onPause()
     */
    override fun onPause() {
        super.onPause()
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
     */
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
    }

    /*
     * (non-Javadoc)
     *
     * @see com.cesarandres.ps2link.base.BaseFragment#onDestroyView()
     */
    override fun onDestroyView() {
        super.onDestroyView()
    }

    // TODO This needs to be localized
    private fun setTimeText(editView: EditText, hours: Int, minutes: Int) {
        var hourOfDay = hours
        var partOfDay = "AM"
        if (hourOfDay >= 12) {
            partOfDay = "PM"
            if (hourOfDay > 12) {
                hourOfDay -= 12
            }
        }
        editView.setText("$hourOfDay:$minutes $partOfDay")
    }

    companion object {

        val PREF_KEY_NOTIFICATION_ENABLE = "push_enabled"
        val PREF_KEY_START_HOUR = "push_start_hours"
        val PREF_KEY_START_MINUTE = "push_start_minutes"
        val PREF_KEY_END_HOUR = "push_end_hours"
        val PREF_KEY_END_MINUTE = "push_end_minutes"
    }
}
