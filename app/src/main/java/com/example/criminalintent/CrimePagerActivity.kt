package com.example.criminalintent

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import java.util.*

class CrimePagerActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_CRIME_ID = "com.bignerdranch.android.criminalIntent.crime_id"
        fun newIntent(packageContent: Context, crimeId: UUID): Intent {
            val intent = Intent(packageContent, CrimePagerActivity::class.java)
            intent.putExtra(EXTRA_CRIME_ID, crimeId)
            return intent
        }
        private var crimes: List<Crime> = emptyList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crime_pager)

        val viewPager = findViewById<ViewPager>(R.id.crime_view_pager)
        val pagerAdapter = CrimePagerAdapter(supportFragmentManager)
        pagerAdapter.setCrimes(crimes)
        viewPager.adapter = pagerAdapter

        val crimeId = intent.getSerializableExtra(EXTRA_CRIME_ID)
        for (i in 1..crimes.size){
            if (crimes[i-1].id == crimeId){
                viewPager.currentItem = i-1
                break
            }
        }

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
            return CrimeFragment.newInstance(UUID.fromString(crime.id))
        }

        fun setCrimes(crimes: List<Crime>){
            this.crimes = crimes
        }
    }

}