package com.me.naturelens

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.me.naturelens.utils.BestMyAdapter2
import com.me.naturelens.utils.SharedPreferenceHelper

class InfoPlantActivity : BaseActivity() {
    private lateinit var plantsRV: RecyclerView
    private lateinit var adapter: BestMyAdapter2
    private lateinit var savedList: MutableList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        SharedPreferenceHelper.initialize(this)
        savedList = SharedPreferenceHelper.getIntegerList("saved_list_key") ?: mutableListOf()
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_plant)

        val id = intent.getIntExtra("id", -1)
        val name = intent.getStringExtra("name")
        val tags = intent.getStringExtra("tags")
        val desc = intent.getStringExtra("desc")
        val image = intent.getIntExtra("image", -1)
        val otherImages = intent.getIntArrayExtra("other_images")?.toList()?.let { ArrayList(it) }

        setSavedState(id)
        setCheckBoxListeners(id)
        if (tags != null) {
            setImageViewTags(tags)
        }

        findViewById<TextView>(R.id.tVTitelPlant).text = name
        findViewById<TextView>(R.id.tVSubTitelPlant).text = tags
        findViewById<TextView>(R.id.tVDescPlant).text = desc
        findViewById<ImageView>(R.id.pI_bigImage).setImageResource(image)
        findViewById<ImageView>(R.id.pI_smallImage).setImageResource(image)

        plantsRV = findViewById(R.id.reec_image)
        otherImages?.let { buildRecyclerView(this, it) }
    }

    private fun setSavedState(id: Int) {
        if (savedList.isNotEmpty() && savedList.contains(id)) {
            findViewById<CheckBox>(R.id.saved_checkBox).isChecked = true
        }
    }

    private fun setCheckBoxListeners(id: Int) {
        findViewById<CheckBox>(R.id.saved_checkBox).setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                if (savedList.isNotEmpty() && savedList.contains(id)) {
                    savedList.remove(id)
                    SharedPreferenceHelper.saveIntegerList("saved_list_key", savedList)
                }
            } else {
                if (!savedList.contains(id) || savedList.isEmpty()) {
                    savedList.add(id)
                    SharedPreferenceHelper.saveIntegerList("saved_list_key", savedList)
                }
            }
        }
    }

    private fun setImageViewTags(tags: String) {
        tags.split(", ", limit = 4).let {
            if (it.contains("poison") || it.contains("نبات سام")) {
                findViewById<ImageView>(R.id.poison_icon_tag).visibility = View.VISIBLE
            }
            if (it.contains("medicine") || it.contains("نبات طبي")) {
                findViewById<ImageView>(R.id.medicine_icon_tag).visibility = View.VISIBLE
            }
            if (it.contains("rare") || it.contains("نبات نادر")) {
                findViewById<ImageView>(R.id.rare_icon_tag).visibility = View.VISIBLE
            }
            if (it.contains("ornamental") || it.contains("نبات زينة")) {
                findViewById<ImageView>(R.id.ornamental_icon_tag).visibility = View.VISIBLE
            }
        }
    }

    private fun buildRecyclerView(context: Context, plantlist: ArrayList<Int>) {
        adapter = BestMyAdapter2(plantlist)
        plantsRV.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        plantsRV.adapter = adapter
    }
}