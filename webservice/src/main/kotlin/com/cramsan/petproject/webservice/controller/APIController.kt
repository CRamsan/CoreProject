package com.cramsan.petproject.webservice.controller

import com.cramsan.petproject.appcore.storage.Description
import com.cramsan.petproject.appcore.storage.ModelStorageInterface
import com.cramsan.petproject.appcore.storage.Plant
import com.cramsan.petproject.appcore.storage.PlantCommonName
import com.cramsan.petproject.appcore.storage.PlantFamily
import com.cramsan.petproject.appcore.storage.PlantMainName
import com.cramsan.petproject.appcore.storage.Toxicity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class APIController {

    @Autowired
    private lateinit var modelStorage: ModelStorageInterface

    @GetMapping("/plant")
    fun plants(): List<Plant> {
        return modelStorage.getPlants()
    }

    @GetMapping("/name/main")
    fun mainNames(): List<PlantMainName> {
        return modelStorage.getPlantsMainName()
    }

    @GetMapping("/name/common")
    fun commonNames(): List<PlantCommonName> {
        return modelStorage.getPlantsCommonNames()
    }

    @GetMapping("/family")
    fun families(): List<PlantFamily> {
        return modelStorage.getPlantsFamily()
    }

    @GetMapping("/description")
    fun descriptions(): List<Description> {
        return modelStorage.getDescription()
    }

    @GetMapping("/toxicity")
    fun toxicities(): List<Toxicity> {
        return modelStorage.getToxicity()
    }
}
