package com.buyani.buyaniserver.util;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StorageUtil {
  private static final String BUCKET_NAME = "buyani-cdn-cleizeery";
  private static final String STORE_FOLDER = "buyani";
  private static final String SIGNING_REGION = "us-west-004";

  AmazonS3 space;

  private static String getConfig(String key) {
    String value = System.getProperty(key);
    if (value == null || value.isEmpty()) {
      value = System.getenv(key);
    }
    return value;
  }

  public StorageUtil() {
    String accessKey = getConfig("STORAGE_ACCESS_KEY");
    String secretKey = getConfig("STORAGE_SECRET_KEY");
    String storageUrl = getConfig("STORAGE_URL");

    log.info("ACCESS KEY LOADED: {}", accessKey);
    log.info("URL LOADED: {}", storageUrl);

    AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
    AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(credentials);

    AwsClientBuilder.EndpointConfiguration endpointConfiguration =
      new AwsClientBuilder.EndpointConfiguration(
        "https://" + storageUrl,
        SIGNING_REGION
      );

    this.space = AmazonS3ClientBuilder
      .standard()
      .withCredentials(awsCredentialsProvider)
      .withEndpointConfiguration(endpointConfiguration)
      .withPathStyleAccessEnabled(true)
      .build();
  }

  public List<String> getImageList() {
    ListObjectsV2Result result = space.listObjectsV2(BUCKET_NAME);
    List<S3ObjectSummary> objectSummaries = result.getObjectSummaries();
    return objectSummaries.stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
  }

  public byte[] getImageBytes(String key) {
    try {
      log.info("Fetching image from bucket {} with key {}", BUCKET_NAME, key);
      S3Object object = space.getObject(BUCKET_NAME, key);
      return object.getObjectContent().readAllBytes();
    } catch (Exception e) {
      log.error("Failed to get image bytes: {}", e.getMessage());
      return null;
    }
  }

  public String uploadImage(MultipartFile file) {
    log.info("Uploading image to bucket {}", BUCKET_NAME);
    String imagePath = "";

    try {
      String fileName = file.getOriginalFilename();
      String contentType = file.getContentType();

      ObjectMetadata objectMetadata = new ObjectMetadata();
      objectMetadata.setContentType(contentType);
      objectMetadata.setContentLength(file.getSize());

      PutObjectRequest putObjectRequest = new PutObjectRequest(
        BUCKET_NAME,
        STORE_FOLDER + "/" + fileName,
        file.getInputStream(),
        objectMetadata
      );

      space.putObject(putObjectRequest);
      imagePath = getImagePathName(file);
      log.info("Upload image success! Path: {}", imagePath);
    } catch (IOException exception) {
      log.error("Failed to upload image to the storage: {}", exception.getMessage());
    }

    return imagePath;
  }

  public String uploadPersonnelImage(MultipartFile file) {
    log.info("Uploading personnel image to bucket {}", BUCKET_NAME);
    String imagePath = "";

    try {
      String fileName = file.getOriginalFilename();
      String contentType = file.getContentType();

      ObjectMetadata objectMetadata = new ObjectMetadata();
      objectMetadata.setContentType(contentType);
      objectMetadata.setContentLength(file.getSize());

      PutObjectRequest putObjectRequest = new PutObjectRequest(
        BUCKET_NAME,
        "personnel/" + fileName,
        file.getInputStream(),
        objectMetadata
      );

      space.putObject(putObjectRequest);
      imagePath = getConfig("STORAGE_PATH_URL") + "/personnel/" + fileName;
      log.info("Upload personnel image success! Path: {}", imagePath);
    } catch (IOException exception) {
      log.error("Failed to upload personnel image to the storage: {}", exception.getMessage());
    }

    return imagePath;
  }

  public String getImagePathName(MultipartFile file) {
    String fileName = file.getOriginalFilename();
    return STORE_FOLDER + "/" + fileName;
  }
}