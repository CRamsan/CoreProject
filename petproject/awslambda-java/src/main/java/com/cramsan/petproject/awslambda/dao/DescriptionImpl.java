package com.cramsan.petproject.awslambda.dao;

import com.cramsan.petproject.appcore.model.AnimalType;
import com.cramsan.petproject.appcore.storage.Description;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class DescriptionImpl implements Description {

    private long id;
    private long plantId;
    private AnimalType animalId;
    private String locale;
    private String description;

    public DescriptionImpl() { }

    public DescriptionImpl(long id, long plantId, AnimalType animalId, String locale, String description) {
        this.id = id;
        this.plantId = plantId;
        this.animalId = animalId;
        this.locale = locale;
        this.description = description;
        this.description = description;
    }

    @DynamoDbPartitionKey
    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public long getPlantId() {
        return plantId;
    }

    public void setPlantId(long plantId) {
        this.plantId = plantId;
    }

    @Override
    public AnimalType getAnimalId() {
        return animalId;
    }

    public void setAnimalId(AnimalType animalId) {
        this.animalId = animalId;
    }

    @Override
    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}