package com.popit.popitproject.common.s3;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Object;

@Service
@Data
public class S3Service {

  @Value("${cloud.aws.credentials.access-key}")
  private String accessKey;

  @Value("${cloud.aws.credentials.secret-key}")
  private String secretKey;

  @Value("${cloud.aws.region.static}")
  private String region;

  @Value("${cloud.aws.s3.bucket}")
  private String bucketName;

  @Bean
  public S3Client s3Client() {  // S3Client는 AWS S3(Simple Storage Service)와 상호 작용하기 위한 클라이언트
    return S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
        .build();
  }

  // 파일을 Amazon S3 버킷에 업로드 ,  MultipartFile file 클라이언트에서 업로드한 파일 : 업로드하는 방식은 HTML 폼, AJAX 요청, HTTP POST
  public String uploadFile(MultipartFile file) throws IOException {
    AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);

    // S3 클라이언트 생성
    S3Client s3 = S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
        .build();

    // UUID를 사용하여 고유한 파일 이름 생성
    String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

    // PutObjectRequest를 빌드합니다.
    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(fileName)
        .build();

    // 파일을 Amazon S3에 업로드합니다.
    PutObjectResponse response = s3.putObject(putObjectRequest,
        RequestBody.fromByteBuffer(ByteBuffer.wrap(file.getBytes())));

    // 업로드된 파일의 URL을 반환합니다.
    if (response.sdkHttpResponse().isSuccessful()) {
      return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileName;
    } else {
      return "Error occurred while uploading " + fileName;
    }
  }


  public List<String> listFiles() { // Amazon S3 버킷에 있는 모든 파일을 나열합니다.
    AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);

    // S3Client 생성
    S3Client s3 = S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
        .build();

    // ListObjectsV2Request를 빌드
    ListObjectsV2Request listObjectsReqManual = ListObjectsV2Request.builder()
        .bucket(bucketName)
        .build();

    ListObjectsV2Response listObjResponse = s3.listObjectsV2(listObjectsReqManual);
    List<S3Object> objects = listObjResponse.contents();

    return objects.stream()
        .map(S3Object::key)
        .collect(Collectors.toList());
  }

  public void deleteFile(String fileName) {
    AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);

    S3Client s3 = S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
        .build();

    DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
        .bucket(bucketName)
        .key(fileName)
        .build();

    s3.deleteObject(deleteObjectRequest);
  }

  public void deleteDuplicateFiles() {
    List<String> allFiles = listFiles();

    // Create a set to identify duplicates
    Set<String> uniqueFiles = new HashSet<>();

    for (String file : allFiles) {
      // For each file, check if it's already in the set
      if (uniqueFiles.contains(file)) {
        // If it is, delete the file
        deleteFile(file);
      } else {
        // If it isn't, add it to the set
        uniqueFiles.add(file);
      }
    }
  }
  public void deleteAllFiles() {
    List<String> allFiles = listFiles();

    for (String file : allFiles) {
      deleteFile(file);
    }
  }
}
