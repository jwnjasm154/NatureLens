package com.me.naturelens.models

data class ModelPlant(
    val id: Int,
    val name: Name,
    val kind: Kind,
    val desc: Desc,
    val tags: Tags,
    val mainImage: Int,
    val otherImage: List<Int>,
)
