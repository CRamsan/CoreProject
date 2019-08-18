package com.cramsan.petproject.webservice.controller

import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.storage.ModelStorageInterface
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class APIController {

    @Autowired
    private lateinit var eventLogger: EventLoggerInterface

    @Autowired
    private lateinit var modelStorage: ModelStorageInterface

    @GetMapping("/plants")
    fun plants(model: Model): String {
        val result = modelStorage.getPlantsWithToxicity(AnimalType.CAT, "en")
        model["title"] = "Blog ${result.size}"
        return "blog ${result.size}"
    }
}
