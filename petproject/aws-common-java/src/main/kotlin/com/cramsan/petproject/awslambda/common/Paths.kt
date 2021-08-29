package com.cramsan.petproject.awslambda.common

enum class Paths(val value: String) {
    PLANT_NAME("/$PLANTS_PATH"),
    MAIN_NAMES("/$NAME_PATH/$MAIN_PATH"),
    COMMON_NAMES("/$NAME_PATH/$COMMON_PATH/{$PLANT_ID_PARAM}"),
    FAMILIES("/$FAMILY_PATH/{$PLANT_ID_PARAM}"),
    DESCRIPTIONS("/$DESCRIPTIONS_PATH/{$PLANT_ID_PARAM}/{$ANIMAL_TYPE_PARAM}"),
    TOXICITIES("/$TOXICITIES_PATH"),
    ;
    companion object {
        private val mapping: Map<String, Paths> by lazy {
            values().map { it.value to it }.toMap()
        }

        fun fromString(value: String) = mapping[value]
    }
}

const val PLANT_ID_PARAM = "plantId"
const val ANIMAL_TYPE_PARAM = "animalType"
const val PLANTS_PATH = "plants"
const val NAME_PATH = "name"
const val MAIN_PATH = "main"
const val COMMON_PATH = "common"
const val FAMILY_PATH = "family"
const val DESCRIPTIONS_PATH = "description"
const val TOXICITIES_PATH = "toxicity"
