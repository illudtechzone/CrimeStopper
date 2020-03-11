package com.illud.crimestopper.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;


@Configuration
public class MinioServerConfiguration {
	
	private final Logger log = LoggerFactory.getLogger(MinioServerConfiguration.class);

	@Value("${minio.server.url}")
	private String url;

	@Value("${minio.server.accessKey}")
	private String accesskey;

	@Value("${minio.server.secretKey}")
	private String secretKey;

	@Value("${minio.buckets.media}")
	private String mediaBucketName;

	

	@Bean
	public MinioClient getMinioClient() throws InvalidEndpointException, InvalidPortException {
		MinioClient minioClient = new MinioClient(url, accesskey, secretKey);
		try {
			boolean mediaBucketFound = minioClient.bucketExists(mediaBucketName);
			
			if (mediaBucketFound) {
				log.info("MediaBucket already exists " + mediaBucketName);
			} else {
				minioClient.makeBucket(mediaBucketName);
				log.info("MediaBucket created " + mediaBucketName);
			}

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return minioClient;
	}

}
