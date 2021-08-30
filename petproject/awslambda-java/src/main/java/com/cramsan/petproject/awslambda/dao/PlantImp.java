package com.cramsan.petproject.awslambda.dao;

import com.cramsan.petproject.appcore.storage.Plant;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class PlantImp implements Plant {

    private long id;
    private String scientificName;
    private String imageUrl;

    public PlantImp() { }

    public PlantImp(long id, String scientificName, String imageUrl) {
        this.id = id;
        this.scientificName = scientificName;
        this.imageUrl = imageUrl;
    }

    @DynamoDbPartitionKey
    @Override
    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
