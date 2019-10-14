package com.example.criminalintent

import android.os.Bundle
import android.text.format.DateFormat
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CrimeListFragment: Fragment() {

    private lateinit var mEmptyCrimesTextView: TextView
    private lateinit var mCrimeRecyclerView: RecyclerView
    private lateinit var mAdapter: RecyclerView.Adapter<CrimeHolder>
    private var mSubtitleVisible: Boolean = true
    private val SAVED_SUBTITLE_VISIBLE = "subtitle"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        mEmptyCrimesTextView = view.findViewById(R.id.empty_text_view)

        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view)
        mCrimeRecyclerView.layoutManager = LinearLayoutManager(activity)

        mAdapter = CrimeAdapter(CrimeLab.mCrimes)
        mCrimeRecyclerView.adapter = mAdapter

        if(savedInstanceState != null){
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE)
        }

        updateUI()

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_crime_list, menu)

        val subtitleItem = menu.findItem(R.id.show_subtitle)
        subtitleItem.title = resources.getString(if (mSubtitleVisible) R.string.hide_subtitle else R.string.show_subtitle)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.new_crime -> {
                val crime = Crime()
                crime.title = ""
                CrimeLab.addCrime(crime)
                val intent = CrimePagerActivity.newIntent(context!!, crime.id)
                startActivity(intent)
                true
            }
            R.id.show_subtitle  -> {
                mSubtitleVisible = !mSubtitleVisible
                activity!!.invalidateOptionsMenu()
                updateSubtitle()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI(){
        mAdapter.notifyDataSetChanged()
        updateSubtitle()
        if(CrimeLab.mCrimes.any()) {
            mEmptyCrimesTextView.visibility = View.GONE
            mCrimeRecyclerView.visibility = View.VISIBLE
        } else {
            mEmptyCrimesTextView.visibility = View.VISIBLE
            mCrimeRecyclerView.visibility = View.GONE
        }
    }

    private fun updateSubtitle(){
        val subtitle = if (mSubtitleVisible) resources.getQuantityString(R.plurals.subtitle_plural, CrimeLab.mCrimes.size, CrimeLab.mCrimes.size) else null
        val activity = activity as CrimeListActivity
        activity.supportActionBar!!.subtitle = subtitle
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