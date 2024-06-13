package com.me.naturelens.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.me.naturelens.CameraActivity
import com.me.naturelens.InfoPlantActivity
import com.me.naturelens.InitApp
import com.me.naturelens.MainActivity
import com.me.naturelens.R
import com.me.naturelens.models.ModelPlant

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        val v =inflater.inflate(R.layout.fragment_home, container, false)

        val randomPlant =  (v.context.applicationContext as InitApp).plantlist.take(39).shuffled().take(4)
        v.findViewById<ImageView>(R.id.popular_image1).setImageResource(randomPlant[0].mainImage)
        v.findViewById<ImageView>(R.id.popular_image2).setImageResource(randomPlant[1].mainImage)
        v.findViewById<TextView>(R.id.popular_text1).text= if (this.resources.configuration.locale.language.equals("en")) randomPlant[0].name.en else randomPlant[0].name.ar
        v.findViewById<TextView>(R.id.popular_text2).text= if (this.resources.configuration.locale.language.equals("en")) randomPlant[1].name.en else randomPlant[1].name.ar

        v.findViewById<ImageView>(R.id.about_image1).setImageResource(randomPlant[2].mainImage)
        v.findViewById<ImageView>(R.id.about_image2).setImageResource(randomPlant[3].mainImage)
        v.findViewById<TextView>(R.id.about_titel1).text= if (this.resources.configuration.locale.language.equals("en")) randomPlant[2].name.en else randomPlant[2].name.ar
        v.findViewById<TextView>(R.id.about_titel2).text= if (this.resources.configuration.locale.language.equals("en")) randomPlant[3].name.en else randomPlant[3].name.ar
        v.findViewById<TextView>(R.id.about_dec1).text= if (this.resources.configuration.locale.language.equals("en")) randomPlant[2].desc.en.split(' ').take(15).joinToString(" ") + "..." else randomPlant[2].desc.ar.split(' ').take(15).joinToString(" ") + "..."
        v.findViewById<TextView>(R.id.about_dec2).text= if (this.resources.configuration.locale.language.equals("en")) randomPlant[3].desc.en.split(' ').take(15).joinToString(" ") + "..." else randomPlant[3].desc.ar.split(' ').take(15).joinToString(" ") + "..."
        v.findViewById<CardView>(R.id.first_popularPlant).setOnClickListener {openPlantInfo(randomPlant[0],v)}
        v.findViewById<CardView>(R.id.second_popularPlant).setOnClickListener {openPlantInfo(randomPlant[1],v) }
        v.findViewById<CardView>(R.id.first_learnAbout).setOnClickListener {openPlantInfo(randomPlant[2],v)}
        v.findViewById<CardView>(R.id.second_learnAbout).setOnClickListener {openPlantInfo(randomPlant[3],v)}

        v.findViewById<CardView>(R.id.goLiveMode).setOnClickListener {
            val intent = Intent(v.context, CameraActivity::class.java)
            startActivity(intent)
        }
        if (this.resources.configuration.locale.language.equals("en")){
        v.findViewById<CardView>(R.id.cardKind1).setOnClickListener {
            activity?.let { (it as MainActivity).setFragmentPage(ExploreFragment(), nameplant = "tag:poison", exploreFSearch = true) }
        }
        v.findViewById<CardView>(R.id.cardKind2).setOnClickListener {
            activity?.let { (it as MainActivity).setFragmentPage(ExploreFragment(), nameplant = "tag:medicine", exploreFSearch = true) }
        }
        v.findViewById<CardView>(R.id.cardKind3).setOnClickListener {
            activity?.let { (it as MainActivity).setFragmentPage(ExploreFragment(), nameplant = "tag:rare", exploreFSearch = true) }
        }
        v.findViewById<CardView>(R.id.cardKind4).setOnClickListener {
            activity?.let { (it as MainActivity).setFragmentPage(ExploreFragment(), nameplant = "tag:ornamental", exploreFSearch = true) }
        }
}else{
            v.findViewById<CardView>(R.id.cardKind1).setOnClickListener {
                activity?.let { (it as MainActivity).setFragmentPage(ExploreFragment(), nameplant = "tag:سام", exploreFSearch = true) }
            }
            v.findViewById<CardView>(R.id.cardKind2).setOnClickListener {
                activity?.let { (it as MainActivity).setFragmentPage(ExploreFragment(), nameplant = "tag:طبي", exploreFSearch = true) }
            }
            v.findViewById<CardView>(R.id.cardKind3).setOnClickListener {
                activity?.let { (it as MainActivity).setFragmentPage(ExploreFragment(), nameplant = "tag:نادر", exploreFSearch = true) }
            }
            v.findViewById<CardView>(R.id.cardKind4).setOnClickListener {
                activity?.let { (it as MainActivity).setFragmentPage(ExploreFragment(), nameplant = "tag:زينة", exploreFSearch = true) }
            }
}
        return v
        }

    private fun openPlantInfo(modelPlant: ModelPlant ,view: View) {
        if (view.resources.configuration.locale.language.equals("en")) {
            val intent = Intent(view.context, InfoPlantActivity::class.java)
            intent.putExtra("id",modelPlant.id)
            intent.putExtra("name", modelPlant.name.en)
            intent.putExtra("kind", modelPlant.kind.en)
            intent.putExtra("desc", modelPlant.desc.en)
            intent.putExtra("tags", modelPlant.tags.en.joinToString(", "))
            intent.putExtra("image", modelPlant.mainImage)
            intent.putExtra("other_images", modelPlant.otherImage.toIntArray())

            view.context?.startActivity(intent)
        } else {
            val intent = Intent(view.context, InfoPlantActivity::class.java)
            intent.putExtra("id",modelPlant.id)
            intent.putExtra("name", modelPlant.name.ar)
            intent.putExtra("kind", modelPlant.kind.ar)
            intent.putExtra("desc", modelPlant.desc.ar)
            intent.putExtra("tags", modelPlant.tags.ar.joinToString(", "))
            intent.putExtra("image", modelPlant.mainImage)
            intent.putExtra("other_images", modelPlant.otherImage.toIntArray())

            view.context?.startActivity(intent)
        }
    }

}