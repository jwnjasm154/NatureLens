package com.me.naturelens.utils




import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.ceylonlabs.imageviewpopup.ImagePopup
import com.me.naturelens.R


class BestMyAdapter2(plantlist: ArrayList<Int>) :
    RecyclerView.Adapter<BestMyAdapter2.ViewHolder>() {

    private var plantlist: ArrayList<Int>



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.other_images, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {

        return plantlist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = plantlist[position]


        holder.imageView.setImageResource(ItemsViewModel)
        holder.objectPlant = ItemsViewModel

    }

    class ViewHolder(itemviwe: View, var objectPlant: Int? = null) :
        RecyclerView.ViewHolder(itemviwe) {
        init {
            itemviwe.setOnClickListener {
                val imagePopup = ImagePopup(itemviwe.context)

                imagePopup.backgroundColor = Color.parseColor("#00FFFFFF")
                imagePopup.initiatePopup(imageView.drawable) // Load Image from Drawable
                imageView.setOnClickListener {
                    imagePopup.viewPopup()
                }
            }
        }
        val imageView: ImageView = itemView.findViewById(R.id.onther_imageView_recey)
    }

    init {
        this.plantlist = plantlist
    }

}
