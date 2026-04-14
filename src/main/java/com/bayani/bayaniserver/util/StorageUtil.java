package com.bayani.bayaniserver.util;

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
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StorageUtil {
  private static final String BUCKET_NAME = "alchives-cdn";
  private static final String STORE_FOLDER = "bayani";
  private static final String STORAGE_ACCESS_KEY = "STORAGE_ACCESS_KEY";
  private static final String STORAGE_SECRET_KEY = "STORAGE_SECRET_KEY";
  private static final String STORAGE_URL = "STORAGE_URL";
  private static final String STORAGE_PATH_URL = "STORAGE_PATH_URL";
  private static final String SIGNING_REGION = "sgp1";

  AmazonS3 space;

  public StorageUtil() {
    AWSCredentials credentials = new BasicAWSCredentials(
      System.getenv(STORAGE_ACCESS_KEY), 
      System.getenv(STORAGE_SECRET_KEY)
    );

    AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(credentials);

    AwsClientBuilder.EndpointConfiguration endpointConfiguration = 
      new AwsClientBuilder.EndpointConfiguration(System.getenv(STORAGE_URL), SIGNING_REGION);

    this.space = AmazonS3ClientBuilder
      .standard()
      .withCredentials(awsCredentialsProvider)
      .withEndpointConfiguration(endpointConfiguration)
      .build();
  }

  public List<String> getImageList() {
    ListObjectsV2Result result = space.listObjectsV2(BUCKET_NAME);
    List<S3ObjectSummary> objectSummaries = result.getObjectSummaries();
    
    return objectSummaries.stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
  }

  public String uploadImage(MultipartFile file) {
    log.info("Uploading image to bucket {}", BUCKET_NAME);
   String imagePath = "";

    try {
      String fileName = file.getOriginalFilename();
      String contentType = file.getContentType();

      ObjectMetadata objectMetadata = new ObjectMetadata();
      objectMetadata.setContentType(contentType);
      
      PutObjectRequest putObjectRequest =  new PutObjectRequest(
        BUCKET_NAME + "/" + STORE_FOLDER, 
        fileName, 
        file.getInputStream(),
        objectMetadata
      ).withCannedAcl(CannedAccessControlList.PublicRead);
    
      space.putObject(putObjectRequest);
      imagePath = getImagePathName(file);
      log.info("Upload image success!");
    } catch(IOException exception) {
      log.error("Failed to upload image to the storage", exception.getMessage());
    }

    return imagePath;
  }

  public String uploadPersonnelImage(MultipartFile file) {
    log.info("Uploading image to bucket {}", BUCKET_NAME);
   String imagePath = "";

    // try {
    //   String fileName = file.getOriginalFilename();
    //   String contentType = file.getContentType();

    //   ObjectMetadata objectMetadata = new ObjectMetadata();
    //   objectMetadata.setContentType(contentType);
      
    //   PutObjectRequest putObjectRequest =  new PutObjectRequest(
    //     BUCKET_NAME + "/personnel", 
    //     fileName, 
    //     file.getInputStream(),
    //     objectMetadata
    //   ).withCannedAcl(CannedAccessControlList.PublicRead);
    
    //   space.putObject(putObjectRequest);
    //   imagePath = getImagePathName(file);
    //   log.info("Upload image success!");
    // } catch(IOException exception) {
    //   log.error("Failed to upload image to the storage", exception.getMessage());
    // }

    return imagePath;
  }

  public String getImagePathName(MultipartFile file) {
    String fileName = file.getOriginalFilename();
    return System.getenv(STORAGE_PATH_URL) + "/" + STORE_FOLDER + "/" + fileName;
  }
}
