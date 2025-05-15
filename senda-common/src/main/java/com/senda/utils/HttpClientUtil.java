package com.senda.utils;

import com.alibaba.fastjson.JSONObject; // 引入FastJSON库，用于处理JSON数据
import org.apache.http.HttpEntity; // 引入HttpEntity接口
import org.apache.http.NameValuePair; // HttpClient提供的名值对接口，常用于表单参数
import org.apache.http.client.config.RequestConfig; // HttpClient请求配置类，用于设置超时等
import org.apache.http.client.entity.UrlEncodedFormEntity; // 用于构建 application/x-www-form-urlencoded 类型的请求体
import org.apache.http.client.methods.CloseableHttpResponse; // HttpClient的可关闭HTTP响应对象
import org.apache.http.client.methods.HttpGet; // HTTP GET请求方法
import org.apache.http.client.methods.HttpPost; // HTTP POST请求方法
import org.apache.http.client.utils.URIBuilder; // URI构建工具，方便添加查询参数
import org.apache.http.entity.ContentType; // HttpClient提供的ContentType枚举，如APPLICATION_JSON
import org.apache.http.entity.StringEntity; // 用于将字符串作为请求体
import org.apache.http.impl.client.CloseableHttpClient; // HttpClient的可关闭HTTP客户端接口实现
import org.apache.http.impl.client.HttpClients; // HttpClient构建工厂类
import org.apache.http.message.BasicNameValuePair; // NameValuePair的基本实现类
import org.apache.http.util.EntityUtils; // HttpClient实体工具类，用于处理响应体
import org.slf4j.Logger; // SLF4J日志接口
import org.slf4j.LoggerFactory; // SLF4J日志工厂，用于获取Logger实例

import java.io.IOException; // Java标准I/O异常
import java.net.URI; // Java标准URI类
import java.net.URISyntaxException; // URI语法错误异常
import java.nio.charset.StandardCharsets; // Java标准字符集类，如UTF_8
import java.util.ArrayList; // Java集合框架的ArrayList
import java.util.List; // Java集合框架的List接口
import java.util.Map; // Java集合框架的Map接口

/**
 * Http工具类
 * <p>
 * 该工具类封装了Apache HttpClient，提供了发送HTTP GET和POST请求的便捷方法。
 * 优化特性包括：
 * 1. 使用共享的CloseableHttpClient实例以提高性能和复用连接。
 * 2. 集成SLF4J进行日志记录。
 * 3. 对CloseableHttpResponse使用try-with-resources语句确保其自动关闭。
 * 4. 共享的HttpClient通过JVM关闭钩子（ShutdownHook）进行优雅关闭。
 * 5. 改进的错误处理，对非2xx状态码抛出IOException。
 * 6. 使用标准字符集（StandardCharsets.UTF_8）和内容类型常量（ContentType）。
 * 7. 将创建HttpEntity的逻辑提取到私有辅助方法中。
 * 8. 优化了JSONObject的参数填充方式 (使用putAll)。
 * 9. 移除了createUrlEncodedFormEntity方法签名中不必要的UnsupportedEncodingException声明。
 * </p>
 */
public class HttpClientUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);
    private static final int TIMEOUT_MSEC = 5 * 1000;
    private static final CloseableHttpClient HTTP_CLIENT = HttpClients.custom()
            .setDefaultRequestConfig(buildDefaultRequestConfig())
            .build();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                LOGGER.info("Closing shared HttpClient due to JVM shutdown...");
                HTTP_CLIENT.close();
                LOGGER.info("Shared HttpClient closed successfully.");
            } catch (IOException e) {
                LOGGER.error("Failed to close shared HttpClient during JVM shutdown", e);
            }
        }));
    }

    private static RequestConfig buildDefaultRequestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(TIMEOUT_MSEC)
                .setConnectionRequestTimeout(TIMEOUT_MSEC)
                .setSocketTimeout(TIMEOUT_MSEC)
                .build();
    }

    private static RequestConfig buildRequestSpecificConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(TIMEOUT_MSEC)
                .setConnectionRequestTimeout(TIMEOUT_MSEC)
                .setSocketTimeout(TIMEOUT_MSEC)
                .build();
    }

    /**
     * 从参数Map创建 application/x-www-form-urlencoded 类型的 HttpEntity。
     * 使用 StandardCharsets.UTF_8 时，构造函数 UrlEncodedFormEntity(List, Charset) 不会抛出 UnsupportedEncodingException。
     *
     * @param paramMap 参数Map。
     * @return 创建的 HttpEntity，如果paramMap为null或为空则返回null。
     */
    private static HttpEntity createUrlEncodedFormEntity(Map<String, String> paramMap) {
        if (paramMap == null || paramMap.isEmpty()) {
            return null;
        }
        List<NameValuePair> paramList = new ArrayList<>();
        for (Map.Entry<String, String> param : paramMap.entrySet()) {
            paramList.add(new BasicNameValuePair(param.getKey(), param.getValue()));
        }
        // 使用接受Charset对象的构造函数，该构造函数不声明UnsupportedEncodingException
        return new UrlEncodedFormEntity(paramList, StandardCharsets.UTF_8);
    }

    /**
     * 从参数Map创建 application/json 类型的 HttpEntity。
     * 使用 JSONObject.putAll() 进行批量添加。
     *
     * @param paramMap 参数Map。
     * @return 创建的 HttpEntity，如果paramMap为null或为空则返回null。
     */
    private static HttpEntity createJsonEntity(Map<String, String> paramMap) {
        if (paramMap == null || paramMap.isEmpty()) {
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        // 使用 putAll 批量添加参数，更简洁
        jsonObject.putAll(paramMap);
        // StringEntity 构造函数使用 String 和 ContentType，ContentType.APPLICATION_JSON 默认使用 UTF-8
        return new StringEntity(jsonObject.toString(), ContentType.APPLICATION_JSON);
    }


    public static String doGet(String url, Map<String, String> paramMap) throws IOException, URISyntaxException {
        URIBuilder builder = new URIBuilder(url);
        if (paramMap != null && !paramMap.isEmpty()) {
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                builder.addParameter(entry.getKey(), entry.getValue());
            }
        }
        URI uri = builder.build();
        HttpGet httpGet = new HttpGet(uri);
        // httpGet.setConfig(buildRequestSpecificConfig()); // 可选：如果GET需要特定配置

        LOGGER.debug("Executing GET request to URI: {}", uri);
        try (CloseableHttpResponse response = HTTP_CLIENT.execute(httpGet)) {
            int statusCode = response.getStatusLine().getStatusCode();
            String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            if (statusCode >= 200 && statusCode < 300) {
                LOGGER.debug("GET request successful (Status: {}), Response body snippet: {}", statusCode, responseBody.substring(0, Math.min(responseBody.length(), 200)));
                return responseBody;
            } else {
                LOGGER.error("GET request failed to {} with status code: {} and response body: {}", uri, statusCode, responseBody);
                throw new IOException("HTTP GET request to " + uri + " failed with status code " + statusCode + ". Response: " + responseBody);
            }
        } catch (IOException e) {
            LOGGER.error("GET request to {} encountered IOException", uri, e);
            throw e;
        }
    }

    public static String doPost(String url, Map<String, String> paramMap) throws IOException {
        HttpPost httpPost = new HttpPost(url);

        // 使用辅助方法创建 entity
        // createUrlEncodedFormEntity 现在不声明 UnsupportedEncodingException
        HttpEntity entity = createUrlEncodedFormEntity(paramMap);
        if (entity != null) {
            httpPost.setEntity(entity);
        }

        httpPost.setConfig(buildRequestSpecificConfig());

        LOGGER.debug("Executing POST (form) request to URL: {}", url);
        try (CloseableHttpResponse response = HTTP_CLIENT.execute(httpPost)) {
            int statusCode = response.getStatusLine().getStatusCode();
            String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            if (statusCode >= 200 && statusCode < 300) {
                LOGGER.debug("POST (form) request successful (Status: {}), Response body snippet: {}", statusCode, responseBody.substring(0, Math.min(responseBody.length(), 200)));
                return responseBody;
            } else {
                LOGGER.error("POST (form) request failed to {} with status code: {} and response body: {}", url, statusCode, responseBody);
                throw new IOException("HTTP POST (form) request to " + url + " failed with status code " + statusCode + ". Response: " + responseBody);
            }
        } catch (IOException e) {
            LOGGER.error("POST (form) request to {} encountered IOException", url, e);
            throw e;
        }
    }

    public static String doPost4Json(String url, Map<String, String> paramMap) throws IOException {
        HttpPost httpPost = new HttpPost(url);

        // 使用辅助方法创建 entity
        HttpEntity entity = createJsonEntity(paramMap);
        if (entity != null) {
            httpPost.setEntity(entity);
        }

        httpPost.setConfig(buildRequestSpecificConfig());

        LOGGER.debug("Executing POST (json) request to URL: {}", url);
        try (CloseableHttpResponse response = HTTP_CLIENT.execute(httpPost)) {
            int statusCode = response.getStatusLine().getStatusCode();
            String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            if (statusCode >= 200 && statusCode < 300) {
                LOGGER.debug("POST (json) request successful (Status: {}), Response body snippet: {}", statusCode, responseBody.substring(0, Math.min(responseBody.length(), 200)));
                return responseBody;
            } else {
                LOGGER.error("POST (json) request failed to {} with status code: {} and response body: {}", url, statusCode, responseBody);
                throw new IOException("HTTP POST (json) request to " + url + " failed with status code " + statusCode + ". Response: " + responseBody);
            }
        } catch (IOException e) {
            LOGGER.error("POST (json) request to {} encountered IOException", url, e);
            throw e;
        }
    }
}