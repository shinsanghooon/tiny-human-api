package com.tinyhuman.tinyhumanapi.integration.aws;

public class S3Util {

    public static String addUserIdToImagePath(String path, Long userId, String fileName) {
        StringBuilder s3FullPathBuilder = new StringBuilder();
        String id = String.valueOf(userId);
        return s3FullPathBuilder.append(path.replace("userId", id)).append(fileName).toString();
    }

    public static String addBabyIdToImagePath(String path, Long babyId, String fileName) {
        StringBuilder s3FullPathBuilder = new StringBuilder();
        String id = String.valueOf(babyId);
        return s3FullPathBuilder.append(path.replace("babyId", id)).append(fileName).toString();
    }

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
