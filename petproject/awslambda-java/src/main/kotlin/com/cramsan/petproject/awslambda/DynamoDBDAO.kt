package com.cramsan.petproject.awslambda

import com.cramsan.petproject.appcore.model.AnimalType
import com.cramsan.petproject.appcore.model.ToxicityValue
import com.cramsan.petproject.appcore.storage.Description
import com.cramsan.petproject.appcore.storage.GetAllPlantsWithAnimalId
import com.cramsan.petproject.appcore.storage.GetPlantWithPlantIdAndAnimalId
import com.cramsan.petproject.appcore.storage.ModelStorageDAO
import com.cramsan.petproject.appcore.storage.Plant
import com.cramsan.petproject.appcore.storage.PlantCommonName
import com.cramsan.petproject.appcore.storage.PlantFamily
import com.cramsan.petproject.appcore.storage.PlantMainName
import com.cramsan.petproject.appcore.storage.Toxicity
import com.cramsan.petproject.awslambda.dao.DescriptionImpl
import com.cramsan.petproject.awslambda.dao.PlantCommonNameImpl
import com.cramsan.petproject.awslambda.dao.PlantFamilyImpl
import com.cramsan.petproject.awslambda.dao.PlantImp
import com.cramsan.petproject.awslambda.dao.PlantMainNameImpl
import com.cramsan.petproject.awslambda.dao.ToxicityImpl
import kotlinx.coroutines.flow.Flow
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

class DynamoDBDAO(
    private val plantsTableName: String,
    private val commonNamesTableName: String,
    private val mainNamesTableName: String,
    private val familiesTableName: String,
    private val toxicitiesTableName: String,
    private val descriptionsTableName: String,
) : ModelStorageDAO {

    private lateinit var ddb: DynamoDbClient
    private lateinit var enhancedClient: DynamoDbEnhancedClient
    private lateinit var plantsTable: DynamoDbTable<PlantImp>
    private lateinit var commonNamesTable: DynamoDbTable<PlantCommonNameImpl>
    private lateinit var mainNamesTable: DynamoDbTable<PlantMainNameImpl>
    private lateinit var familiesTable: DynamoDbTable<PlantFamilyImpl>
    private lateinit var toxicitiesTable: DynamoDbTable<ToxicityImpl>
    private lateinit var descriptionsTable: DynamoDbTable<DescriptionImpl>

    fun configure() {
        ddb = DynamoDbClient.builder().build()
        enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(ddb)
            .build()
        plantsTable = enhancedClient.table(plantsTableName, TableSchema.fromClass(PlantImp::class.java))
        commonNamesTable = enhancedClient.table(commonNamesTableName, TableSchema.fromBean(PlantCommonNameImpl::class.java))
        mainNamesTable = enhancedClient.table(mainNamesTableName, TableSchema.fromBean(PlantMainNameImpl::class.java))
        // familiesTable = enhancedClient.table(familiesTableName, TableSchema.fromBean(PlantFamilyImpl::class.java))
        // toxicitiesTable = enhancedClient.table(toxicitiesTableName, TableSchema.fromBean(ToxicityImpl::class.java))
        // descriptionsTable = enhancedClient.table(descriptionsTableName, TableSchema.fromBean(DescriptionImpl::class.java))
    }

    override fun insertPlantEntry(plantId: Long?, scientificName: String, imageUrl: String) {
        requireNotNull(plantId)
        val plant = PlantImp(plantId, scientificName, imageUrl)
        plantsTable.putItem(plant)
    }

    override fun insertPlantEntries(list: List<com.cramsan.petproject.appcore.storage.Plant>) {
        val batchWriterBuilder = WriteBatch.builder(PlantImp::class.java)
            .mappedTableResource(plantsTable)

        list.forEach {
            val plant = PlantImp(it.id, it.scientificName, it.imageUrl)
            batchWriterBuilder.addPutItem(plant)
        }

        val batchWriteItemEnhancedRequest: BatchWriteItemEnhancedRequest =
            BatchWriteItemEnhancedRequest.builder().writeBatches(batchWriterBuilder.build()).build()
        enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest)
    }

    override fun insertPlantCommonNameEntry(commonNameId: Long?, commonName: String, plantId: Long, locale: String) {
        requireNotNull(commonNameId)
        val plantCommonName = PlantCommonNameImpl(commonNameId, commonName, plantId, locale)
        commonNamesTable.putItem(plantCommonName)
    }

    override fun insertPlantCommonNameEntries(list: List<com.cramsan.petproject.appcore.storage.PlantCommonName>) {
        val batchWriterBuilder = WriteBatch.builder(PlantCommonNameImpl::class.java)
            .mappedTableResource(commonNamesTable)

        list.forEach {
            val plantCommonName = PlantCommonNameImpl(it.id, it.commonName, it.plantId, it.locale)
            batchWriterBuilder.addPutItem(plantCommonName)
        }

        val batchWriteItemEnhancedRequest: BatchWriteItemEnhancedRequest =
            BatchWriteItemEnhancedRequest.builder().writeBatches(batchWriterBuilder.build()).build()
        enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest)
    }

    override fun insertPlantMainNameEntry(mainNameId: Long?, mainName: String, plantId: Long, locale: String) {
        requireNotNull(mainNameId)
        val plantMainNameImpl = PlantMainNameImpl(mainNameId, mainName, plantId, locale)
        mainNamesTable.putItem(plantMainNameImpl)
    }

    override fun insertPlantMainNameEntries(list: List<com.cramsan.petproject.appcore.storage.PlantMainName>) {
        val batchWriterBuilder = WriteBatch.builder(PlantMainNameImpl::class.java)
            .mappedTableResource(mainNamesTable)

        list.forEach {
            val plantMainName = PlantMainNameImpl(it.id, it.mainName, it.plantId, it.locale)
            batchWriterBuilder.addPutItem(plantMainName)
        }

        val batchWriteItemEnhancedRequest: BatchWriteItemEnhancedRequest =
            BatchWriteItemEnhancedRequest.builder().writeBatches(batchWriterBuilder.build()).build()
        enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest)
    }

    override fun insertPlantFamilyNameEntry(familyId: Long?, family: String, plantId: Long, locale: String) {
        requireNotNull(familyId)
        val plantFamilyImpl = PlantFamilyImpl(familyId, family, plantId, locale)
        familiesTable.putItem(plantFamilyImpl)
    }

    override fun insertPlantFamilyNameEntries(list: List<com.cramsan.petproject.appcore.storage.PlantFamily>) {
        val batchWriterBuilder = WriteBatch.builder(PlantFamilyImpl::class.java)
            .mappedTableResource(familiesTable)

        list.forEach {
            val plantFamily = PlantFamilyImpl(it.id, it.family, it.plantId, it.locale)
            batchWriterBuilder.addPutItem(plantFamily)
        }

        val batchWriteItemEnhancedRequest: BatchWriteItemEnhancedRequest =
            BatchWriteItemEnhancedRequest.builder().writeBatches(batchWriterBuilder.build()).build()
        enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest)
    }

    override fun insertToxicityEntry(toxicityId: Long?, isToxic: ToxicityValue, plantId: Long, animalType: AnimalType, source: String) {
        requireNotNull(toxicityId)
        val toxicityImpl = ToxicityImpl(toxicityId, plantId, animalType, isToxic, source)
        toxicitiesTable.putItem(toxicityImpl)
    }

    override fun insertToxicityEntries(list: List<com.cramsan.petproject.appcore.storage.Toxicity>) {
        val batchWriterBuilder = WriteBatch.builder(ToxicityImpl::class.java)
            .mappedTableResource(toxicitiesTable)

        list.forEach {
            val toxicity = ToxicityImpl(it.id, it.plantId, it.animalId, it.toxic, it.source)
            batchWriterBuilder.addPutItem(toxicity)
        }

        val batchWriteItemEnhancedRequest: BatchWriteItemEnhancedRequest =
            BatchWriteItemEnhancedRequest.builder().writeBatches(batchWriterBuilder.build()).build()
        enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest)
    }

    override fun insertDescriptionEntry(descriptionId: Long?, plantId: Long, animalType: AnimalType, description: String, locale: String) {
        requireNotNull(descriptionId)
        val descriptionImpl = DescriptionImpl(descriptionId, plantId, animalType, description, locale)
        descriptionsTable.putItem(descriptionImpl)
    }

    override fun insertDescriptionEntries(list: List<com.cramsan.petproject.appcore.storage.Description>) {
        val batchWriterBuilder = WriteBatch.builder(DescriptionImpl::class.java)
            .mappedTableResource(descriptionsTable)

        list.forEach {
            val description = DescriptionImpl(it.id, it.plantId, it.animalId, it.description, it.locale)
            batchWriterBuilder.addPutItem(description)
        }

        val batchWriteItemEnhancedRequest: BatchWriteItemEnhancedRequest =
            BatchWriteItemEnhancedRequest.builder().writeBatches(batchWriterBuilder.build()).build()
        enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest)
    }

    override fun getPlantEntry(scientificName: String): Plant? = TODO("Operation not supported in this target.")

    override fun getAllPlantEntries(): List<Plant> {
        val results: Iterator<PlantImp> = plantsTable.scan().items().iterator()

        val plantList = mutableListOf<PlantImp>()
        while (results.hasNext()) {
            val plant = results.next()
            plantList.add(plant)
        }
        return plantList
    }

    override fun getPlantEntryCount(): Long = TODO("Operation not supported in this target.")

    override fun getPlantCommonNameEntries(plantId: Long, locale: String): List<PlantCommonName> = TODO("Operation not supported in this target.")

    override fun getAllPlantCommonNameEntries(): List<PlantCommonName> {
        val results: Iterator<PlantCommonNameImpl> = commonNamesTable.scan().items().iterator()

        val commonNameList = mutableListOf<PlantCommonNameImpl>()
        while (results.hasNext()) {
            val commonName = results.next()
            commonNameList.add(commonName)
        }
        return commonNameList
    }

    override fun getPlantMainNameEntry(plantId: Long, locale: String): PlantMainName? = TODO("Operation not supported in this target.")

    override fun getAllPlantMainNameEntries(): List<PlantMainName> {
        val results: Iterator<PlantMainNameImpl> = mainNamesTable.scan().items().iterator()

        val mainNameList = mutableListOf<PlantMainNameImpl>()
        while (results.hasNext()) {
            val mainName = results.next()
            mainNameList.add(mainName)
        }
        return mainNameList
    }

    override fun getPlantFamilyEntry(plantId: Long, locale: String): PlantFamily? = TODO("Operation not supported in this target.")

    override fun getAllPlantFamilyEntries(): List<PlantFamily> {
        val results: Iterator<PlantFamilyImpl> = familiesTable.scan().items().iterator()

        val familyList = mutableListOf<PlantFamilyImpl>()
        while (results.hasNext()) {
            val family = results.next()
            familyList.add(family)
        }
        return familyList
    }

    override fun getToxicityEntry(plantId: Long, animalType: AnimalType): Toxicity? = TODO("Operation not supported in this target.")

    override fun getAllToxicityEntries(): List<Toxicity> {
        val results: Iterator<ToxicityImpl> = toxicitiesTable.scan().items().iterator()

        val toxicityList = mutableListOf<ToxicityImpl>()
        while (results.hasNext()) {
            val toxicity = results.next()
            toxicityList.add(toxicity)
        }
        return toxicityList
    }

    override fun getDescriptionEntry(plantId: Long, animalType: AnimalType, locale: String): Description? = TODO("Operation not supported in this target.")

    override fun getAllDescriptionEntries(): List<Description> {
        val results: Iterator<DescriptionImpl> = descriptionsTable.scan().items().iterator()

        val descriptionList = mutableListOf<DescriptionImpl>()
        while (results.hasNext()) {
            val description = results.next()
            descriptionList.add(description)
        }
        return descriptionList
    }

    override fun getCustomPlantEntries(animalType: AnimalType, locale: String): List<GetAllPlantsWithAnimalId> =
        TODO("Operation not supported in this target.")

    override fun getCustomPlantEntriesFlow(
        animalType: AnimalType,
        locale: String
    ): Flow<List<GetAllPlantsWithAnimalId>> = TODO("Operation not supported in this target.")

    override fun getCustomPlantEntry(plantId: Long, animalType: AnimalType, locale: String): GetPlantWithPlantIdAndAnimalId? =
        TODO("Operation not supported in this target.")

    override fun getCustomPlantEntriesPaginated(
        animalType: AnimalType,
        locale: String,
        limit: Long,
        offset: Long
    ): List<GetAllPlantsWithAnimalId> = TODO("Operation not supported in this target.")

    override fun deleteAll() = TODO("Operation not supported in this target.")
}
