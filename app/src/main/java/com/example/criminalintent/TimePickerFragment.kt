package com.example.criminalintent

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePickerFragment: DialogFragment() {

    companion object {
        const val ARG_TIME = "date"
        const val EXTRA_TIME = "com.bignerdranch.android.criminalIntent.time"
        fun newInstance(date: Date): TimePickerFragment {
            val fragment = TimePickerFragment()
            val bundle = Bundle()
            bundle.putSerializable(ARG_TIME, date)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val v = LayoutInflater.from(activity).inflate(R.layout.dialog_time, null)
        val date = arguments?.getSerializable(TimePickerFragment.ARG_TIME) as Date

        val calendar = Calendar.getInstance()
        calendar.time = date

        val timePicker = v.findViewById<TimePicker>(R.id.dialog_time_picker)
        timePicker.hour = calendar.get(Calendar.HOUR)
        timePicker.minute = calendar.get(Calendar.MINUTE)

        return AlertDialog
            .Builder(activity)
            .setView(v)
            .setPositiveButton(android.R.string.ok){_, _ ->
                val time =
                    GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                        timePicker.hour, timePicker.minute, calendar.get(Calendar.SECOND)).time
                sendResult(Activity.RESULT_OK, time)
            }
            .create()
    }

    private fun sendResult(resultCode: Int, date: Date) {
        if (targetFragment == null) {
            return
        }
        val intent = Intent()
        intent.putExtra(EXTRA_TIME, date)
        targetFragment!!.onActivityResult(targetRequestCode, resultCode, intent)
    }

}