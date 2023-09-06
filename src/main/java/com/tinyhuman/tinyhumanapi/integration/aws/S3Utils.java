package com.tinyhuman.tinyhumanapi.integration.aws;

public class S3Utils {
    public static String getAccessUrl(String bucketName, String s3FullPath) {
        StringBuffer accessUrl = new StringBuffer();
        return accessUrl.append("https://")
                .append(bucketName)
                .append(".s3.ap-northeast-2.amazonaws.com")
                .append("/")
                .append(s3FullPath)
                .toString();
    }
}
