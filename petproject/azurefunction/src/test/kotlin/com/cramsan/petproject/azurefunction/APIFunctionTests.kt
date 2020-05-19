package com.cramsan.petproject.azurefunction

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.ToxicityValue
import com.cramsan.petproject.appcore.storage.implementation.DescriptionImpl
import com.cramsan.petproject.appcore.storage.implementation.PlantCommonNameImpl
import com.cramsan.petproject.appcore.storage.implementation.PlantFamilyImpl
import com.cramsan.petproject.appcore.storage.implementation.PlantImp
import com.cramsan.petproject.appcore.storage.implementation.PlantMainNameImpl
import com.cramsan.petproject.appcore.storage.implementation.ToxicityImpl
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.microsoft.azure.functions.ExecutionContext
import com.microsoft.azure.functions.HttpRequestMessage
import com.microsoft.azure.functions.HttpResponseMessage
import com.microsoft.azure.functions.HttpStatus
import io.mockk.every
import io.mockk.mockk
import java.lang.reflect.Type
import java.util.logging.Logger
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

class APIFunctionTests {

    lateinit var gson: Gson

    @Before
    fun setUp() {
        gson = Gson()
    }

    @Test
    fun testHttpTriggerJava() {
        // Setup
        val req: HttpRequestMessage<String?> = mockk()
        val queryParams: MutableMap<String, String> = HashMap()
        queryParams["name"] = "Azure"

        every { req.queryParameters } returns queryParams
        every { req.body } returns ""

        every { req.createResponseBuilder(any()) } answers {
            val status: HttpStatus = call.invocation.args[0] as HttpStatus
            HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(status)
        }

        val context: ExecutionContext = mockk()
        every { context.logger } returns Logger.getGlobal()

        // Invoke
        val ret: HttpResponseMessage = APIFunction().plants(req, context)

        // Verify
        assertEquals(ret.status, HttpStatus.OK)
    }

    @Test
    fun testPlants() {
        // Setup
        val req: HttpRequestMessage<String?> = mockk()
        every { req.createResponseBuilder(any()) } answers {
            val status: HttpStatus = call.invocation.args[0] as HttpStatus
            HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(status)
        }
        val context: ExecutionContext = mockk()
        // Invoke
        val ret: HttpResponseMessage = APIFunction().plants(req, context)
        val bodyString: String = ret.body as String
        val listType: Type = object : TypeToken<ArrayList<PlantImp>>() {}.type
        val result: ArrayList<PlantImp> = gson.fromJson(bodyString, listType)

        // Verify
        assertFalse(result.isEmpty())
        val plant = result[0]
        assertEquals(plant.id, 1)
        assertEquals(plant.imageUrl, "https://upload.wikimedia.org/wikipedia/commons/thumb/9/99/Howea_forsteriana_Lord_Howe_Island.jpg/1200px-Howea_forsteriana_Lord_Howe_Island.jpg")
        assertEquals(plant.scientificName, "Howea forsteriana")
    }

    @Test
    fun testMainNames() {
        // Setup
        val req: HttpRequestMessage<String?> = mockk()
        every { req.createResponseBuilder(any()) } answers {
            val status: HttpStatus = call.invocation.args[0] as HttpStatus
            HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(status)
        }
        val context: ExecutionContext = mockk()
        // Invoke
        val ret: HttpResponseMessage = APIFunction().mainNames(req, context)
        val bodyString: String = ret.body as String
        val listType: Type = object : TypeToken<ArrayList<PlantMainNameImpl>>() {}.type
        val result: ArrayList<PlantMainNameImpl> = gson.fromJson(bodyString, listType)

        // Verify
        assertFalse(result.isEmpty())
        val mainName = result[0]
        assertEquals(mainName.id, 1)
        assertEquals(mainName.plantId, 1)
        assertEquals(mainName.mainName, "Forster Sentry Palm")
        assertEquals(mainName.locale, "en")
    }

    @Test
    fun testCommonNames() {
        // Setup
        val req: HttpRequestMessage<String?> = mockk()
        every { req.createResponseBuilder(any()) } answers {
            val status: HttpStatus = call.invocation.args[0] as HttpStatus
            HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(status)
        }
        val context: ExecutionContext = mockk()
        // Invoke
        val ret: HttpResponseMessage = APIFunction().commonNames(req, 90L, context)
        val bodyString: String = ret.body as String
        val listType: Type = object : TypeToken<ArrayList<PlantCommonNameImpl>>() {}.type
        val result: ArrayList<PlantCommonNameImpl> = gson.fromJson(bodyString, listType)

        // Verify
        assertFalse(result.isEmpty())
        val commonName = result[5]
        assertEquals(commonName.id, 619)
        assertEquals(commonName.plantId, 90)
        assertEquals(commonName.commonName, "Devil's Vine")
        assertEquals(commonName.locale, "en")
    }

    @Test
    fun testDescription() {
        // Setup
        val req: HttpRequestMessage<String?> = mockk()
        every { req.createResponseBuilder(any()) } answers {
            val status: HttpStatus = call.invocation.args[0] as HttpStatus
            HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(status)
        }
        val context: ExecutionContext = mockk()
        // Invoke
        val ret: HttpResponseMessage = APIFunction().description(req, 100L, 1, context)
        val bodyString: String = ret.body as String
        val result: DescriptionImpl = gson.fromJson(bodyString, DescriptionImpl::class.java)

        // Verify
        assertEquals(result.id, 664)
        assertEquals(result.plantId, 100)
        assertEquals(result.animalId, AnimalType.DOG)
        assertEquals(result.description, "")
        assertEquals(result.locale, "en")
    }

    @Test
    fun testFamily() {
        // Setup
        val req: HttpRequestMessage<String?> = mockk()
        every { req.createResponseBuilder(any()) } answers {
            val status: HttpStatus = call.invocation.args[0] as HttpStatus
            HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(status)
        }
        val context: ExecutionContext = mockk()
        // Invoke
        val ret: HttpResponseMessage = APIFunction().familiy(req, 100L, context)
        val bodyString: String = ret.body as String
        val result: PlantFamilyImpl = gson.fromJson(bodyString, PlantFamilyImpl::class.java)

        // Verify
        assertEquals(result.id, 100)
        assertEquals(result.plantId, 100)
        assertEquals(result.family, "Begoniaceae")
        assertEquals(result.locale, "en")
    }

    @Test
    fun testToxicities() {
        // Setup
        val req: HttpRequestMessage<String?> = mockk()
        every { req.createResponseBuilder(any()) } answers {
            val status: HttpStatus = call.invocation.args[0] as HttpStatus
            HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(status)
        }
        val context: ExecutionContext = mockk()
        // Invoke
        val ret: HttpResponseMessage = APIFunction().toxicities(req, context)
        val bodyString: String = ret.body as String
        val listType: Type = object : TypeToken<ArrayList<ToxicityImpl>>() {}.type
        val result: ArrayList<ToxicityImpl> = gson.fromJson(bodyString, listType)

        // Verify
        assertFalse(result.isEmpty())
        val toxicity = result[0]
        assertEquals(toxicity.id, 1)
        assertEquals(toxicity.plantId, 1)
        assertEquals(toxicity.animalId, AnimalType.DOG)
        assertEquals(toxicity.toxic, ToxicityValue.NON_TOXIC)
    }
}
