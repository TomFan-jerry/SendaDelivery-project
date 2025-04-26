package com.senda.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.UUID;

/**
 * Cloudflare R2 对象存储工具类
 * <p>
 * 重构说明：
 * 1. 移除硬编码配置，改为通过@Value注入application.yml中的配置
 * 2. 添加字段注释说明配置项来源
 * 3. 保持核心功能不变，优化配置管理方式
 */
@Component
public class R2Utils {

    // ------------ 注入配置参数 ------------

    /**
     * R2 API端点（对应application.yml中的r2.endpoint）
     */
    @Value("${r2.endpoint}")
    private String endpoint;

    /**
     * 访问密钥ID（对应application.yml中的r2.access-key）
     */
    @Value("${r2.access-key}")
    private String accessKey;

    /**
     * 私有访问密钥（对应application.yml中的r2.secret-key）
     */
    @Value("${r2.secret-key}")
    private String secretKey;

    /**
     * 存储桶名称（对应application.yml中的r2.bucket-name）
     */
    @Value("${r2.bucket-name}")
    private String bucketName;

    /**
     * 公共访问端点（对应application.yml中的r2.public-endpoint）
     */
    @Value("${r2.public-endpoint}")
    private String publicEndpoint;

    // ------------ S3 客户端（延迟初始化） ------------

    private S3Client s3Client;

    /**
     * 初始化S3客户端（通过@Value注入后构建）
     * 使用懒加载模式避免空指针异常
     */
    private void initS3Client() {
        if (s3Client == null) {
            s3Client = S3Client.builder()
                    .endpointOverride(URI.create(endpoint))
                    .region(Region.of("auto"))
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(accessKey, secretKey)
                    ))
                    .serviceConfiguration(S3Configuration.builder()
                            .pathStyleAccessEnabled(true)
                            .build())
                    .build();
        }
    }

    // ------------ 核心上传方法 ------------

    /**
     * 上传文件到R2存储桶
     *
     * @param file Spring接收的上传文件对象
     * @return 可公开访问的文件URL
     * @throws IOException 文件读取异常
     *                     <p>
     *                     流程优化：
     *                     1. 添加客户端懒加载初始化
     *                     2. 增强空值校验
     *                     3. 优化异常处理
     */
    public String upload(MultipartFile file) throws IOException {
        // 初始化客户端
        initS3Client();

        // 参数校验
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }

        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename != null ?
                originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
        String fileName = UUID.randomUUID() + suffix;

        // 处理文件流
        try (InputStream inputStream = file.getInputStream()) {
            // 智能判断MIME类型
            String contentType = resolveContentType(file, suffix);

            // 构建上传请求
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(contentType)
                    .build();

            // 执行上传
            s3Client.putObject(request, RequestBody.fromInputStream(
                    inputStream, file.getSize()));
        }

        // 生成公共访问URL
        return String.format("%s/%s", publicEndpoint, fileName);
    }

    // ------------ 辅助方法 ------------

    /**
     * 智能解析Content-Type
     *
     * @param file   上传文件对象
     * @param suffix 文件后缀
     * @return 确定的MIME类型
     */
    private String resolveContentType(MultipartFile file, String suffix) {
        String contentType = file.getContentType();

        // 有效性校验
        if (contentType == null ||
                contentType.startsWith("application/octet-stream") ||
                contentType.equals("text/plain")) {
            return getContentTypeBySuffix(suffix);
        }
        return contentType;
    }

    /**
     * 根据文件后缀推断MIME类型
     *
     * @param suffix 文件后缀（包含点，如.jpg）
     * @return 对应MIME类型，默认application/octet-stream
     * <p>
     * 扩展说明：
     * 可在此处添加更多文件类型支持，建议参考IANA MIME类型标准
     */
    private String getContentTypeBySuffix(String suffix) {
        if (suffix == null || suffix.isEmpty()) {
            return "application/octet-stream";
        }

        return switch (suffix.toLowerCase()) {
            case ".png" -> "image/png";
            case ".jpg", ".jpeg" -> "image/jpeg";
            case ".gif" -> "image/gif";
            case ".webp" -> "image/webp";
            case ".svg" -> "image/svg+xml";
            case ".pdf" -> "application/pdf";
            case ".zip" -> "application/zip";
            case ".doc" -> "application/msword";
            case ".docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case ".xls" -> "application/vnd.ms-excel";
            case ".xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case ".mp4" -> "video/mp4";
            case ".mov" -> "video/quicktime";
            default -> "application/octet-stream";
        };
    }
}