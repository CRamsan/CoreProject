package com.cramsan.petproject.awslambda.dao;

import com.cramsan.petproject.appcore.storage.PlantMainName;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class PlantMainNameImpl implements PlantMainName {
    private long id;
    private String MainName;
    private long plantId;
    private String locale;

    public PlantMainNameImpl() { }

    public PlantMainNameImpl(long id, String MainName, long plantId, String locale) {
        this.id = id;
        this.MainName = MainName;
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
    public String getMainName() {
        return MainName;
    }

    public void setMainName(String MainName) {
        this.MainName = MainName;
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