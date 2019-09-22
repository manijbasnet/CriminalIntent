package com.example.criminalintent

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import java.util.*

class CrimeActivity : SingleFragmentActivity() {

    companion object {
        private const val EXTRA_CRIME_ID = "com.bignerdranch.android.criminalIntent.crime_id"
        fun newIntent(packageContent: Context, crimeId: UUID): Intent {
            val intent = Intent(packageContent, CrimeActivity::class.java)
            intent.putExtra(EXTRA_CRIME_ID, crimeId)
            return intent
        }
    }

    override fun createFragment(): Fragment {
        val crimeId =  intent.getSerializableExtra(EXTRA_CRIME_ID) as UUID
        return CrimeFragment.newInstance(crimeId)
    }
}
