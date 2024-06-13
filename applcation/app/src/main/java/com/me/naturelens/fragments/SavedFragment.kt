package com.me.naturelens.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.me.naturelens.InitApp
import com.me.naturelens.MainActivity
import com.me.naturelens.R
import com.me.naturelens.models.ModelPlant
import com.me.naturelens.utils.BestMyAdapter
import com.me.naturelens.utils.SharedPreferenceHelper

class SavedFragment : Fragment() {
    private lateinit var savedList: MutableList<Int>
    private lateinit var plantsRV: RecyclerView
    private lateinit var plantlist: ArrayList<ModelPlant>
    private lateinit var adapter: BestMyAdapter
    override fun onResume() {
        super.onResume()

        if (savedList != SharedPreferenceHelper.getIntegerList("saved_list_key") && savedList.isNotEmpty()) {
            activity?.let { (it as MainActivity).setFragmentPage(SavedFragment()) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_saved, container, false)
        SharedPreferenceHelper.initialize(v.context)
        savedList = SharedPreferenceHelper.getIntegerList("saved_list_key") ?: mutableListOf()
        if (savedList.isEmpty()) {
            v.findViewById<LinearLayout>(R.id.empty_saved_icon).visibility = View.VISIBLE
            v.findViewById<RecyclerView>(R.id.reyc_saved_viwe).visibility = View.GONE
        } else {
            v.findViewById<LinearLayout>(R.id.empty_saved_icon).visibility = View.GONE
            v.findViewById<RecyclerView>(R.id.reyc_saved_viwe).visibility = View.VISIBLE

            plantsRV = v.findViewById(R.id.reyc_saved_viwe)
            context?.let { buildRecyclerView(it) }
            adapter = BestMyAdapter(plantlist)
            plantsRV.layoutManager = LinearLayoutManager(
                context,
                RecyclerView.VERTICAL, false
            )
            plantsRV.adapter = adapter
        }

        return v
    }

    private fun buildRecyclerView(context: Context) {
        val data1 = (context.applicationContext as InitApp).plantlist
        val data2: ArrayList<ModelPlant> =
            data1.filter { savedList.contains(it.id) }.toCollection(ArrayList())
        val data3 = data2.sortedBy { savedList.indexOf(it.id) }.reversed()
        plantlist = data3.toCollection(ArrayList())

        // initializing our adapter class.
        adapter = BestMyAdapter(plantlist)

        // adding layout manager to our recycler view.
        val manager = LinearLayoutManager(context)
        plantsRV.setHasFixedSize(true)

        // setting layout manager
        // to our recycler view.
        plantsRV.layoutManager = manager

        // setting adapter to
        // our recycler view.
        plantsRV.adapter = adapter
    }
}