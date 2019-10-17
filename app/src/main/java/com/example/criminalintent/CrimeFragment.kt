package com.example.criminalintent

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.*
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import java.util.*

class CrimeFragment : Fragment() {

    private var mCrime: Crime = Crime()
    private lateinit var mTitleField: EditText
    private lateinit var mDateButton: Button
    private lateinit var mTimeButton: Button
    private lateinit var mSolvedCheckBox: CheckBox

    private lateinit var crimeViewModel: CrimeViewModel

    companion object {
        const val ARG_CRIME_ID = "crime_id"
        const val DIALOG_DATE = "DialogDate"
        const val DIALOG_TIME = "DialogTime"
        const val REQUEST_DATE = 0
        const val REQUEST_TIME = 1
        fun newInstance(crimeId: String): CrimeFragment {
            val args = Bundle()
            args.putString(ARG_CRIME_ID, crimeId)
            val fragment = CrimeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crimeViewModel = ViewModelProvider(this).get(CrimeViewModel::class.java)

        val crimeId = arguments?.getString(ARG_CRIME_ID)

        crimeViewModel.get(crimeId!!).observe(this, Observer { crime ->
            crime?.let {
                mCrime = crime
                updateUI()
            }
        })
        setHasOptionsMenu(true)
    }

    private fun updateUI(){
        mTitleField.setText(mCrime.title)
        mSolvedCheckBox.isChecked = mCrime.solved
        updateDate()
        updateTime()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_crime, container, false)
        mTitleField = v.findViewById(R.id.crime_title)
        mTitleField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mCrime.title = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        mDateButton = v.findViewById(R.id.crime_date)
        mDateButton.setOnClickListener {
            val dialogFragment = DatePickerFragment.newInstance(mCrime.date)
            dialogFragment.setTargetFragment(this, REQUEST_DATE)
            dialogFragment.show(parentFragmentManager, DIALOG_DATE)
        }

        mSolvedCheckBox = v.findViewById(R.id.crime_solved)
        mSolvedCheckBox.setOnCheckedChangeListener { _, isChecked ->
            mCrime.solved = isChecked
        }


        mTimeButton = v.findViewById(R.id.crime_time)
        mTimeButton.setOnClickListener {
            val timeFragment = TimePickerFragment.newInstance(mCrime.date)
            timeFragment.setTargetFragment(this, REQUEST_TIME)
            timeFragment.show(parentFragmentManager,  DIALOG_TIME)
        }

        return v
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_crime, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
          R.id.delete_crime -> {
              //CrimeLab.mCrimes.remove(mCrime)
              activity!!.finish()
              true
          }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateDate(){
        mDateButton.text = DateFormat.format("EEEE, MMM dd, yyyy", mCrime.date)
    }

    private fun updateTime(){
        mTimeButton.text = DateFormat.format("hh:mm:ss a", mCrime.date)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_DATE) {
            val date = data!!.getSerializableExtra(DatePickerFragment.EXTRA_DATE) as Date
            mCrime.date = date
            updateDate()
        }

        if(requestCode == REQUEST_TIME) {
            val date = data!!.getSerializableExtra(TimePickerFragment.EXTRA_TIME) as Date
            mCrime.date = date
            updateTime()
        }
    }

}