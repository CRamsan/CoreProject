package com.cramsan.petproject.awslambda.dao;

import com.cramsan.petproject.appcore.model.AnimalType;
import com.cramsan.petproject.appcore.model.ToxicityValue;
import com.cramsan.petproject.appcore.storage.Toxicity;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class ToxicityImpl implements Toxicity {
    private long id;
    private long plantId;
    private AnimalType animalId;
    private ToxicityValue toxic;
    private String source;

    public ToxicityImpl() { }

    public ToxicityImpl(long id, long plantId, AnimalType animalId, ToxicityValue toxic, String source) {
        this.id = id;
        this.plantId = plantId;
        this.animalId = animalId;
        this.toxic = toxic;
        this.source = source;
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
    public ToxicityValue getToxic() {
        return toxic;
    }

    public void setToxic(ToxicityValue toxic) {
        this.toxic = toxic;
    }

    @Override
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
