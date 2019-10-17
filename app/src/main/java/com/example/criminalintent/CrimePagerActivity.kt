package com.example.criminalintent

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager

class CrimePagerActivity : AppCompatActivity() {

    private lateinit var crimeViewModel: CrimeViewModel
    private lateinit var crimeId: String
    private var crimePosition: Int = 0

    companion object {
        private const val EXTRA_CRIME_ID = "com.bignerdranch.android.criminalIntent.crime_id"
        private const val EXTRA_CRIME_POSITION = "com.bignerdranch.android.criminalIntent.crime_position"
        fun newIntent(packageContent: Context, crimeId: String, position: Int): Intent {
            val intent = Intent(packageContent, CrimePagerActivity::class.java)
            intent.putExtra(EXTRA_CRIME_ID, crimeId)
            intent.putExtra(EXTRA_CRIME_POSITION, position)
            return intent
        }
        private var crimes: List<Crime> = emptyList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crime_pager)

        val viewPager = findViewById<ViewPager>(R.id.crime_view_pager)
        val pagerAdapter = CrimePagerAdapter(supportFragmentManager)

        crimeViewModel = ViewModelProvider(this).get(CrimeViewModel::class.java)
        crimeViewModel.allWords.observe(this, Observer { crimes ->
            crimes?.let {
                pagerAdapter.setCrimes(crimes)
                viewPager.currentItem = crimePosition
            }
        })

        viewPager.adapter = pagerAdapter

        crimeId = intent.getStringExtra(EXTRA_CRIME_ID)!!
        crimePosition = intent.getIntExtra(EXTRA_CRIME_POSITION, 0)

        val firstItemButton = findViewById<Button>(R.id.first_item)
        val lastItemButton = findViewById<Button>(R.id.last_item)

        firstItemButton.setOnClickListener {
            viewPager.currentItem = 0
        }

        lastItemButton.setOnClickListener {
            viewPager.currentItem = crimes.size
        }
    }

    private class CrimePagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private var crimes = emptyList<Crime>()

        override fun getCount(): Int {
            return crimes.size
        }

        override fun getItem(position: Int): Fragment {
            val crime = crimes[position]
            return CrimeFragment.newInstance(crime.id)
        }

        fun setCrimes(crimes: List<Crime>){
            this.crimes = crimes
            notifyDataSetChanged()
        }
    }

}