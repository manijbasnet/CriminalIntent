package com.example.criminalintent

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment

class CrimeFragment: Fragment() {

    lateinit var mCrime: Crime
    lateinit var mTitleField: EditText
    lateinit var mDateButton: Button
    lateinit var mSolvedCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCrime = Crime()
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
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mCrime.title = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

        mDateButton = v.findViewById(R.id.crime_date)
        mDateButton.text = mCrime.date.toString()
        mDateButton.isEnabled = false

        mSolvedCheckBox = v.findViewById(R.id.crime_solved)
        mSolvedCheckBox.setOnCheckedChangeListener { _, isChecked ->
            mCrime.solved = isChecked
        }

        return v
    }

}