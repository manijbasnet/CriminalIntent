package com.example.criminalintent

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.*
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.core.app.ShareCompat
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
    private lateinit var mSuspectButton: Button
    private lateinit var mReportButton: Button
    private lateinit var mSuspectCallButton: Button
    private lateinit var suspectNumber: Uri

    private lateinit var crimeViewModel: CrimeViewModel

    companion object {
        const val ARG_CRIME_ID = "crime_id"
        const val DIALOG_DATE = "DialogDate"
        const val DIALOG_TIME = "DialogTime"
        const val REQUEST_DATE = 0
        const val REQUEST_TIME = 1
        const val REQUEST_CONTACT = 2
        fun newInstance(crimeId: String): CrimeFragment {
            val args = Bundle()
            args.putString(ARG_CRIME_ID, crimeId)
            val fragment = CrimeFragment()
            fragment.arguments = args
            return fragment
        }
        val pickContact = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
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
        if(mCrime.suspect != null){
            mSuspectButton.text = mCrime.suspect
            mSuspectCallButton.text = getString(R.string.crime_suspect_call, mCrime.suspect)
            mSuspectCallButton.isEnabled = true
        } else {
            mSuspectCallButton.isEnabled = false
            mSuspectCallButton.text = getString(R.string.crime_suspect_call, "Suspect")
        }
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

        mReportButton = v.findViewById(R.id.crime_report)
        mReportButton.setOnClickListener {
            val intent = ShareCompat.IntentBuilder
                .from(activity)
                .setType("text/plain")
                .setSubject(getString(R.string.crime_report_subject))
                .setText(getCrimeReport())
                .setChooserTitle(getString(R.string.send_report))
                .createChooserIntent()
            startActivity(intent)
        }

        mSuspectButton = v.findViewById(R.id.crime_suspect)
        mSuspectButton.setOnClickListener {
            startActivityForResult(pickContact, REQUEST_CONTACT)
        }

        val packageManager = activity?.packageManager
        if(packageManager?.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null){
            mSuspectButton.isEnabled = false
        }

        mSuspectCallButton = v.findViewById(R.id.crime_suspect_call)
        mSuspectCallButton.isEnabled = false
        mSuspectCallButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:${suspectNumber}"))
            val chooser= Intent.createChooser(intent,"title")
            startActivity(chooser)
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

        if(requestCode == REQUEST_CONTACT && data != null){
            val contactUri = data.data!!
            val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID)
            val cursor = activity!!.contentResolver.query(contactUri, queryFields, null, null, null)
            cursor?.use {
                if(cursor.count == 0){ return }
                cursor.moveToFirst()
                mCrime.suspect = cursor.getString(0)
                val id = cursor.getString(1)
                mSuspectButton.text = mCrime.suspect

                val phones = activity!!.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null)
                while (phones!!.moveToNext()){
                    suspectNumber = Uri.parse(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)))
                }

                mSuspectCallButton.text = getString(R.string.crime_suspect_call, mCrime.suspect)
                mSuspectCallButton.isEnabled = true
            }
        }
    }

    private fun getCrimeReport(): String{
        val solvedString = getString(if (mCrime.solved) R.string.crime_report_solved else R.string.crime_report_unsolved)
        val dateFormat = "EEE, MMM dd"
        val dateString = DateFormat.format(dateFormat, mCrime.date)
        val suspect = if (mCrime.suspect == null) getString(R.string.crime_report_no_suspect) else  getString(R.string.crime_report_suspect, mCrime.suspect)
        return getString(R.string.crime_report, mCrime.title, dateString, solvedString, suspect)
    }
}