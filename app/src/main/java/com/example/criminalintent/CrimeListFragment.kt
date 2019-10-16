package com.example.criminalintent

import android.os.Bundle
import android.text.format.DateFormat
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CrimeListFragment: Fragment() {

    private lateinit var crimeViewModel: CrimeViewModel

    private lateinit var mEmptyCrimesTextView: TextView
    private lateinit var mCrimeRecyclerView: RecyclerView
    private lateinit var mAdapter: CrimeAdapter
    private var mSubtitleVisible: Boolean = true
    private val SAVED_SUBTITLE_VISIBLE = "subtitle"
    private var crimes: List<Crime> = emptyList()

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

        mAdapter = CrimeAdapter()
        mCrimeRecyclerView.adapter = mAdapter

        if(savedInstanceState != null){
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE)
        }

        crimeViewModel = ViewModelProvider(this).get(CrimeViewModel::class.java)
        crimeViewModel.allWords.observe(this, Observer { words ->
            words?.let {
                this.crimes = it
                mAdapter.setWords(this.crimes)
                updateUI()
            }
        })

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
                crime.title = "Crime ${this.crimes.size+1}"
                crimeViewModel.insert(crime)
                //val intent = CrimePagerActivity.newIntent(context!!, UUID.fromString(crime.id))
                //startActivity(intent)
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
        if(crimes.any()) {
            mEmptyCrimesTextView.visibility = View.GONE
            mCrimeRecyclerView.visibility = View.VISIBLE
        } else {
            mEmptyCrimesTextView.visibility = View.VISIBLE
            mCrimeRecyclerView.visibility = View.GONE
        }
        updateSubtitle()
    }

    private fun updateSubtitle(){
        val subtitle = if (mSubtitleVisible) resources.getQuantityString(R.plurals.subtitle_plural, this.crimes.size, this.crimes.size) else null
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

            /*itemView.setOnClickListener {
                val intent = CrimePagerActivity.newIntent(itemView.context, UUID.fromString(crime.id))
                itemView.context.startActivity(intent)
            }*/
        }
    }

    private class CrimeAdapter : RecyclerView.Adapter<CrimeHolder>(){

        private var mCrimes = emptyList<Crime>()

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

        internal fun setWords(words: List<Crime>) {
            this.mCrimes = words
            notifyDataSetChanged()
        }

    }

}