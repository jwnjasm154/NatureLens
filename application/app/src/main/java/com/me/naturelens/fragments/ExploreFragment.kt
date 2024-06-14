package com.me.naturelens.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.camera.core.Camera
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.me.naturelens.InitApp
import com.me.naturelens.R
import com.me.naturelens.models.ModelPlant
import com.me.naturelens.models.RawPlantsData
import com.me.naturelens.utils.BestMyAdapter
import com.me.naturelens.utils.ReadJSONFromAssets

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ExploreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExploreFragment : Fragment() {
    private lateinit var plantsRV: RecyclerView
    private lateinit var plantlist: ArrayList<ModelPlant>
    private lateinit var adapter: BestMyAdapter
    private lateinit var texte: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        val v = inflater.inflate(R.layout.fragment_explore, container, false)

        plantsRV= v.findViewById(R.id.reyc_viwe)
        texte=v.findViewById(R.id.notGetSearch)
        context?.let { buildRecyclerView(it) }
        adapter= BestMyAdapter(plantlist)
        plantsRV.layoutManager= LinearLayoutManager(context,
            RecyclerView.VERTICAL,false)
        plantsRV.adapter=adapter
        adapter.registerAdapterDataObserver(object :RecyclerView.AdapterDataObserver()
        {
            override fun onChanged() {
                super.onChanged()
                if(adapter.itemCount == 0) {
                    texte.visibility=View.VISIBLE
                    plantsRV.visibility=View.GONE
                }else{
                    texte.visibility=View.GONE
                    plantsRV.visibility=View.VISIBLE
                }
            }
        }
        )

        arguments?.let {
            // not null do something
            if (!it.isEmpty) {
                val message = it.getString("mText")
                filterData(message.toString().trimEnd())
            }
        }

        return v
    }

    private fun buildRecyclerView(context: Context) {

        plantlist =  (context.applicationContext as InitApp).plantlist

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)

        val searchView = menu.findItem(R.id.actionSearch).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                filterData(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
        })
    }
    private fun filterData(query: String) {

        adapter.filterList(searchPlants(query.toLowerCase()))

    }

    fun searchPlants(input: String): List<ModelPlant> {
        val (searchKey, searchValue) = input.split(":", limit = 2).let {
            if (it.size == 2) {
                Pair(it[0].trim(), it[1].trim())
            } else {
                Pair("name", input.trim())
            }
        }
        if (this.resources.configuration.locale.language.equals("en")) {
            return plantlist.filter { plant ->
                when (searchKey) {
                    "kind" -> plant.kind.en.toLowerCase().contains(searchValue)
                    "tag" -> plant.tags.en.any { it.toLowerCase().contains(searchValue) }
                    else -> plant.name.en.toLowerCase().contains(searchValue)
                }
        }
        }
        else{
            return plantlist.filter { plant ->
                when (searchKey) {
                    "kind" -> plant.kind.ar.toLowerCase().contains(searchValue)
                    "tag" -> plant.tags.ar.any { it.toLowerCase().contains(searchValue) }
                    else -> plant.name.ar.toLowerCase().contains(searchValue)
                }
            }
        }

    }

}