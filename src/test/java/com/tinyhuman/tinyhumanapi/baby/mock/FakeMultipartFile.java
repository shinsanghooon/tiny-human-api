package com.tinyhuman.tinyhumanapi.baby.mock;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FakeMultipartFile {

    public static MultipartFile createMultipartFile() {
        // 샘플 이미지 파일을 읽어서 멀티 파트 파일로 변경 후 넘겨준다.
        String filePath = "src/test/resources/assets/test.png";

        // Load the file as a byte array
        File file = new File(filePath);
        byte[] fileBytes = new byte[(int) file.length()];
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            fileInputStream.read(fileBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Create a MockMultipartFile
        MultipartFile multipartFile = new MockMultipartFile(
                "text.jpeg",   // Original file name
                "text.jpeg",   // Original file name
                "image/jpeg",  // Content type
                fileBytes      // File content as byte array
        );
        return multipartFile;
    }
}
