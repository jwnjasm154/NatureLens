package com.me.naturelens.models

import android.content.Context
import java.util.Locale

data class RawPlantsData(
    val data: List<RawPlantInfo>,
) {
    fun toModelPlants(context: Context): ArrayList<ModelPlant> {
        return ArrayList(data.map { rawPlantInfo ->
            ModelPlant(
                id = rawPlantInfo.id,
                name = Name(en = rawPlantInfo.name.en, ar = rawPlantInfo.name.ar),
                kind = Kind(en = rawPlantInfo.kind.en, ar = rawPlantInfo.kind.ar),
                desc = Desc(en = rawPlantInfo.desc.en, ar = rawPlantInfo.desc.ar),
                tags = Tags(en = rawPlantInfo.tags.en, ar = rawPlantInfo.tags.ar),
                mainImage = getImageResource(rawPlantInfo.mainImage, context),
                otherImage = rawPlantInfo.otherImage.map { getImageResource(it, context) }
            )
        })
    }


    private fun getImageResource(imageName: String, context: Context): Int {

        return context.resources.getIdentifier(imageName.lowercase(), "drawable", context.packageName)
    }
}

data class RawPlantInfo(
    val id: Int,
    val name: Name,
    val kind: Kind,
    val desc: Desc,
    val tags: Tags,
    val mainImage: String,
    val otherImage: List<String>,
)

data class Name(
    val en: String,
    val ar: String,
)

data class Kind(
    val en: String,
    val ar: String,
)

data class Desc(
    val en: String,
    val ar: String,
)

data class Tags(
    val en: List<String>,
    val ar: List<String>,
)
