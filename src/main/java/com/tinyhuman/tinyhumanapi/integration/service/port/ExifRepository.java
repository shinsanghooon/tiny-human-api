package com.tinyhuman.tinyhumanapi.integration.service.port;

import com.tinyhuman.tinyhumanapi.integration.controller.dto.LastEvaluatedKey;
import com.tinyhuman.tinyhumanapi.integration.domain.ExifInfo;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;

import java.util.Iterator;

public interface ExifRepository {

    Iterator<Page<ExifInfo>> getExif(Long babyId, LastEvaluatedKey lastEvaluatedKey);

}

