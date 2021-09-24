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
            values().associateBy { it.value }
        }

        fun fromString(value: String) = mapping[value]
    }
}

enum class ModifyPaths(val value: String) {
    PLANT_NAME("/$PLANTS_PATH/{$PLANT_ID_PARAM}"),
    MAIN_NAMES("/$NAME_PATH/$MAIN_PATH/{$MAIN_NAME_ID}"),
    COMMON_NAMES("/$NAME_PATH/$COMMON_PATH/{$COMMON_NAME_ID}"),
    FAMILIES("/$FAMILY_PATH/{$FAMILY_ID}"),
    DESCRIPTIONS("/$DESCRIPTIONS_PATH/{$DESCRIPTION_ID}"),
    TOXICITIES("/$TOXICITIES_PATH/{$TOXICITY_ID}"),
    ;
    companion object {
        private val mapping: Map<String, ModifyPaths> by lazy {
            values().associateBy { it.value }
        }

        fun fromString(value: String) = mapping[value]
    }
}

const val PLANT_ID_PARAM = "plantId"
const val ANIMAL_TYPE_PARAM = "animalType"
const val MAIN_NAME_ID = "mainNameId"
const val COMMON_NAME_ID = "commonNameId"
const val FAMILY_ID = "familyId"
const val DESCRIPTION_ID = "descriptionId"
const val TOXICITY_ID = "toxicityId"

const val PLANTS_PATH = "plants"
const val NAME_PATH = "name"
const val MAIN_PATH = "main"
const val COMMON_PATH = "common"
const val FAMILY_PATH = "family"
const val DESCRIPTIONS_PATH = "description"
const val TOXICITIES_PATH = "toxicity"
