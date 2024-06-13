package com.me.naturelens.utils


import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.me.naturelens.InfoPlantActivity
import com.me.naturelens.R
import com.me.naturelens.models.ModelPlant



class BestMyAdapter(plantlist: ArrayList<ModelPlant>) :
    RecyclerView.Adapter<BestMyAdapter.ViewHolder>() {

    private var plantlist: ArrayList<ModelPlant>

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filterList: List<ModelPlant>) {

        plantlist = filterList as ArrayList<ModelPlant>

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {

        return plantlist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsViewModel = plantlist[position]

        holder.imageView.setImageResource(itemsViewModel.mainImage)

        val name_plant_view = itemsViewModel.name.let {
            if (holder.itemView.resources.configuration.locale.language.equals("en")) {
                it.en
            } else {
                it.ar
            }
        }

        val kind_plant_view = itemsViewModel.kind.let {
            if (holder.itemView.resources.configuration.locale.language.equals("en")) {
                it.en
            } else {
                it.ar
            }
        }

        val tags_plant_view = itemsViewModel.tags.let {
            if (holder.itemView.resources.configuration.locale.language.equals("en")) {
                it.en
            } else {
                it.ar
            }
        }

        holder.namePlantView.text = name_plant_view
        holder.nameKindView.text = kind_plant_view
        holder.nameTagsView.text = tags_plant_view.joinToString(", ")
        holder.objectPlant = itemsViewModel

    }

    class ViewHolder(itemviwe: View, var objectPlant: ModelPlant? = null) :
        RecyclerView.ViewHolder(itemviwe) {
        init {
            itemviwe.setOnClickListener {
                if (itemviwe.resources.configuration.locale.language.equals("en")) {
                    val intent = Intent(itemviwe.context, InfoPlantActivity::class.java)
                    intent.putExtra("id",objectPlant?.id)
                    intent.putExtra("name", objectPlant?.name?.en)
                    intent.putExtra("kind", objectPlant?.kind?.en)
                    intent.putExtra("desc", objectPlant?.desc?.en)
                    intent.putExtra("tags", objectPlant?.tags?.en?.joinToString(", "))
                    intent.putExtra("image", objectPlant?.mainImage)
                    intent.putExtra("other_images", objectPlant?.otherImage?.toIntArray())

                    itemviwe.context?.startActivity(intent)
                } else {
                    val intent = Intent(itemviwe.context, InfoPlantActivity::class.java)
                    intent.putExtra("id",objectPlant?.id)
                    intent.putExtra("name", objectPlant?.name?.ar)
                    intent.putExtra("kind", objectPlant?.kind?.ar)
                    intent.putExtra("desc", objectPlant?.desc?.ar)
                    intent.putExtra("tags", objectPlant?.tags?.ar?.joinToString(", "))
                    intent.putExtra("image", objectPlant?.mainImage)
                    intent.putExtra("other_images", objectPlant?.otherImage?.toIntArray())

                    itemviwe.context?.startActivity(intent)
                }
            }
        }

        val imageView: ImageView = itemView.findViewById(R.id.image_plant)
        val namePlantView: TextView = itemView.findViewById(R.id.tvNamePlant)
        val nameKindView: TextView = itemView.findViewById(R.id.tvKindPlant)
        val nameTagsView: TextView = itemView.findViewById(R.id.tvTagsPlant)

    }

    init {
        this.plantlist = plantlist
    }

}
