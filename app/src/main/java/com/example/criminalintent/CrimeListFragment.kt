package com.example.criminalintent

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CrimeListFragment: Fragment() {

    private lateinit var mCrimeRecyclerView: RecyclerView
    private lateinit var mAdapter: RecyclerView.Adapter<CrimeHolder>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view)
        mCrimeRecyclerView.layoutManager = LinearLayoutManager(activity)

        mAdapter = CrimeAdapter(CrimeLab.mCrimes)
        mCrimeRecyclerView.adapter = mAdapter

        updateUI()

        return view
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI(){
        mAdapter.notifyDataSetChanged()
    }

    private class CrimeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(crime: Crime){
            val crimeTitleView = itemView.findViewById<TextView>(R.id.crime_title)
            val crimeDateView = itemView.findViewById<TextView>(R.id.crime_date)
            val crimeSolvedView = itemView.findViewById<ImageView>(R.id.crime_solved)
            crimeTitleView.text = crime.title
            crimeDateView.text = DateFormat.format("EEEE, MMM dd, yyyy", crime.date)
            crimeSolvedView.visibility = if (crime.solved) View.VISIBLE else View.GONE

            itemView.setOnClickListener {
                val intent = CrimePagerActivity.newIntent(itemView.context, crime.id)
                itemView.context.startActivity(intent)
            }
        }
    }

    private class CrimeAdapter(val mCrimes: List<Crime>) : RecyclerView.Adapter<CrimeHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
            return CrimeHolder(view)
        }

        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            holder.bindItems(mCrimes[position])
        }

        override fun getItemCount(): Int {
            return mCrimes.size
        }
    }

}