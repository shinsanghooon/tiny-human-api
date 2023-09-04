package com.tinyhuman.tinyhumanapi.integration.domain;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class ExifInfo implements Comparable<ExifInfo> {

    private String babyId;

    private String originalCreatedAt;

    private String gpsInfo;

    private String gpsLatitude;

    private String gpsLongitude;

    private String keyName;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("baby_id")
    public String getBabyId() {
        return babyId;
    }

    public void setBabyId(String babyId) {
        this.babyId = babyId;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("original_created_at")
    public String getOriginalCreatedAt() {
        return originalCreatedAt;
    }

    public void setOriginalCreatedAt(String originalCreatedAt) {
        this.originalCreatedAt = originalCreatedAt;
    }

    @DynamoDbAttribute("gps_info")
    public String getGpsInfo() {
        return gpsInfo;
    }

    public void setGpsInfo(String gpsInfo) {
        this.gpsInfo = gpsInfo;
    }

    @DynamoDbAttribute("gps_latitude")
    public String getGpsLatitude() {
        return gpsLatitude;
    }

    public void setGpsLatitude(String gpsLatitude) {
        this.gpsLatitude = gpsLatitude;
    }

    @DynamoDbAttribute("gps_longitude")
    public String getGpsLongitude() {
        return gpsLongitude;
    }

    public void setGpsLongitude(String gpsLongitude) {
        this.gpsLongitude = gpsLongitude;
    }


    @DynamoDbAttribute("key_name")
    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    @Override
    public int compareTo(ExifInfo o) {
        if (this.keyName.compareTo(o.keyName) != 0){
            return this.keyName.compareTo(o.keyName);
        } else {
            return this.keyName.compareTo(o.keyName);
        }
    }
}