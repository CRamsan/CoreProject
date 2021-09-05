package com.cramsan.petproject.awslambda.dao;

import com.cramsan.petproject.appcore.storage.PlantCommonName;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class PlantCommonNameImpl implements PlantCommonName {
    private long id;
    private String commonName;
    private long plantId;
    private String locale;

    public PlantCommonNameImpl() { }

    public PlantCommonNameImpl(long id, String commonName, long plantId, String locale) {
        this.id = id;
        this.commonName = commonName;
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
    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
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