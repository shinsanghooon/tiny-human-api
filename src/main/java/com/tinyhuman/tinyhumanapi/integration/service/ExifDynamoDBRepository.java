package com.tinyhuman.tinyhumanapi.integration.service;

import com.tinyhuman.tinyhumanapi.integration.controller.dto.LastEvaluatedKey;
import com.tinyhuman.tinyhumanapi.integration.domain.ExifInfo;
import com.tinyhuman.tinyhumanapi.integration.service.port.ExifRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class ExifDynamoDBRepository implements ExifRepository {

    @Value("${aws.ddb.table.exif}")
    private String EXIF_TABLE_NAME;

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @Override
    public Iterator<Page<ExifInfo>> getExif(Long babyId, LastEvaluatedKey lastEvaluatedKey) {

        // create exclusiveStartKey for next data
        Map<String, AttributeValue> exclusiveStartKey = new HashMap<>();
        if (lastEvaluatedKey == null) {
            exclusiveStartKey = null;
        } else {
            exclusiveStartKey.put("baby_id", AttributeValue.fromS(lastEvaluatedKey.babyId()));
            exclusiveStartKey.put("original_created_at", AttributeValue.fromS(lastEvaluatedKey.originalCreatedAt()));
        }

        DynamoDbTable<ExifInfo> table = dynamoDbEnhancedClient.table(EXIF_TABLE_NAME, TableSchema.fromBean(ExifInfo.class));

        QueryConditional keyEqual = QueryConditional.keyEqualTo(b -> b.partitionValue(String.valueOf(babyId)));

        QueryEnhancedRequest tableQuery = QueryEnhancedRequest.builder()
                .attributesToProject("baby_id", "original_created_at", "gps_latitude", "gps_longitude")
                .queryConditional(keyEqual)
                .scanIndexForward(false) // 최신순
                .exclusiveStartKey(exclusiveStartKey)
                .limit(5)
                .build();

        return table.query(tableQuery).iterator();
    }

}
