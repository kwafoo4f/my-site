package cn.luischen.api;

import cn.luischen.exception.BusinessException;
import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * 上传文件到minin
 */
@Slf4j
@Component
public class MinioHelper {
    private MinioClient minioClient;

    @Value("${minio.accesskey}")
    private String ACCESS_KEY;
    @Value("${minio.serectkey}")
    private String SECRET_KEY;
    /**
     * 仓库
     */
    @Value("${minio.bucket}")
    private String BUCKET;
    /**
     * 外网访问地址
     */
    @Value("${minio.endpoint}")
    public String ENDPOINT;

    public boolean bucketExists(String bucket) {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        } catch (Exception e) {
            log.error("查询 bucket 失败", e);
            return false;
        }
    }

    public boolean makeBucket(String bucket) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            return true;
        } catch (Exception e) {
            log.error("创建 bucket 失败", e);
            return false;
        }
    }

    public void checkBucket(String bucket) {
        if (!bucketExists(bucket)) {
            makeBucket(bucket);
        }
    }

    /**
     * 生成预签名URL
     *
     * @param objectName
     * @return
     */
    public String presignedUrl(String objectName) {
        try {
            String presignedObjectUrl = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.PUT)
                    .bucket(BUCKET)
                    .object(objectName)
                    .expiry(60, TimeUnit.SECONDS)
                    .build());
            return presignedObjectUrl;
        } catch (Exception e) {
            log.error("生成预签名URL失败!", e);
            throw new BusinessException("生成预签名URL失败!");
        }

    }

    public String upload(MultipartFile file, String fileName) {
        InputStream inputStream = null;
        try {
            checkBucket(BUCKET);

            inputStream = file.getInputStream();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(BUCKET)
                    .object(fileName)
                    .stream(inputStream, file.getSize(), -1)// 文件流
                    .build());
            return ENDPOINT + "/" + BUCKET + "/" + fileName;
        } catch (Exception e) {
            log.error("上传失败!", e);
            throw new BusinessException("上传失败!");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("上传失败!", e);
                }
            }
        }

    }

    @PostConstruct
    public void init() {
        minioClient = MinioClient.builder()
                .endpoint(ENDPOINT)
                .credentials(ACCESS_KEY, SECRET_KEY)
                .build();
    }
}
