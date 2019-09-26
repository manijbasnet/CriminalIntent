package com.example.criminalintent

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment : DialogFragment() {

    companion object {
        const val ARG_DATE = "date"
        const val EXTRA_DATE = "com.bignerdranch.android.criminalIntent.date"
        fun newInstance(date: Date): DatePickerFragment {
            val fragment = DatePickerFragment()
            val bundle = Bundle()
            bundle.putSerializable(ARG_DATE, date)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val v = LayoutInflater.from(activity).inflate(R.layout.dialog_date, null)
        val date = arguments?.getSerializable(ARG_DATE) as Date

        val calendar = Calendar.getInstance()
        calendar.time = date

        val datePicker = v.findViewById<DatePicker>(R.id.dialog_date_picker)
        datePicker.init(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
            null
        )

        return AlertDialog.Builder(activity)
            .setView(v)
            .setTitle(R.string.date_picker_title)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                val date =
                    GregorianCalendar(datePicker.year, datePicker.month, datePicker.dayOfMonth).time
                sendResult(Activity.RESULT_OK, date)
            }
            .create()
    }

    private fun sendResult(resultCode: Int, date: Date) {
        if (targetFragment == null) {
            return
        }
        val intent = Intent()
        intent.putExtra(EXTRA_DATE, date)
        targetFragment!!.onActivityResult(targetRequestCode, resultCode, intent)
    }
}