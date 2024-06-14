package com.me.naturelens.fragments

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.me.naturelens.InfoPlantActivity
import com.me.naturelens.InitApp
import com.me.naturelens.R
import com.me.naturelens.models.ModelPlant

//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_image = "image"
private const val ARG_name = "mText"

class SuccessFragment : Fragment() {
    private lateinit var byteArray: ByteArray
    private lateinit var plantName: String
    private lateinit var plantssanImage: ImageView
    private lateinit var plantssanName: TextView
    private lateinit var btnnavgate: CardView
    private var listener: SuccessInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (!it.isEmpty) {
                byteArray = it.getByteArray(ARG_image)!!
                plantName = it.getString(ARG_name).toString()
            }
        }
    }

    // make lisner
    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as SuccessInterface
    }

    //setFragment
    fun setFragmentFormFragment(context: Context) {
        val plantlist =  (context.applicationContext as InitApp).plantlist
        val filteredList:List<ModelPlant> =
            plantlist.filter { item ->
                item.name.en.equals(plantName.trimEnd('\r'),ignoreCase = true)
            }
        if(filteredList.isNotEmpty() && filteredList.size < 2){
            Log.i("testScann",filteredList.toString())
            val plant = filteredList.first()
            if (context.resources.configuration.locale.language.equals("en")) {
                val intent = Intent(context, InfoPlantActivity::class.java)
                intent.putExtra("id", plant.id)
                intent.putExtra("name", plant.name.en)
                intent.putExtra("kind", plant.kind.en)
                intent.putExtra("desc", plant.desc.en)
                intent.putExtra("tags", plant.tags.en.joinToString(", "))
                intent.putExtra("image", plant.mainImage)
                intent.putExtra("other_images", plant.otherImage.toIntArray())

                context.startActivity(intent)
            }
            else {
                val intent = Intent(context, InfoPlantActivity::class.java)
                intent.putExtra("id", plant.id)
                intent.putExtra("name", plant.name.ar)
                intent.putExtra("kind", plant.kind.ar)
                intent.putExtra("desc", plant.desc.ar)
                intent.putExtra("tags", plant.tags.ar.joinToString(", "))
                intent.putExtra("image", plant.mainImage)
                intent.putExtra("other_images", plant.otherImage.toIntArray())

                context.startActivity(intent)
            }
        }else {
            Log.i("testScann",filteredList.toString())
            listener?.callSetFragmentPage(plantName.toLowerCase().trimEnd('\r'))
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_success, container, false)
        //setImage
        val bitmap1 = BitmapFactory.decodeByteArray(byteArray, 0, byteArray?.size ?: 0)
        plantssanImage = v.findViewById(R.id.imageVscan)
        plantssanImage.setImageBitmap(bitmap1)
        //setText
        plantssanName = v.findViewById(R.id.tVName)
        plantssanName.text = plantName
        //go to plant
        btnnavgate = v.findViewById(R.id.btnForMore)
        btnnavgate.setOnClickListener {
            context?.let { it1 -> setFragmentFormFragment(it1) }
        }
        return v
    }

}

interface SuccessInterface {
    fun callSetFragmentPage(myString: String)
}