package com.cramsan.petproject.awslambda.dao;

import com.cramsan.petproject.appcore.storage.PlantFamily;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class PlantFamilyImpl implements PlantFamily {
    private long id;
    private String family;
    private long plantId;
    private String locale;

    public PlantFamilyImpl() { }

    public PlantFamilyImpl(long id, String family, long plantId, String locale) {
        this.id = id;
        this.family = family;
        this.plantId = plantId;
        this.locale = locale;
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
    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    @Override
    public long getPlantId() {
        return plantId;
    }

    public void setPlantId(long plantId) {
        this.plantId = plantId;
    }

    @Override
    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}