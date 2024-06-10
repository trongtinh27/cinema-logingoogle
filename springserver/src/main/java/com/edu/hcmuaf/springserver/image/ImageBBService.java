package com.edu.hcmuaf.springserver.image;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class ImageBBService {
    private static final String IMG_BB_API_URL = "https://api.imgbb.com/1/upload";
    private static final String API_KEY = "8c2c7c5c94797f04504f969ec51749a4";

    public String uploadImage(byte[] imageBytes) throws Exception {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost req = new HttpPost(IMG_BB_API_URL);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            //
            builder.addTextBody("key", API_KEY);
            builder.addBinaryBody("image", imageBytes);

            //
            HttpEntity multipart = builder.build();
            req.setEntity(multipart);

            //
            HttpResponse res = httpClient.execute(req);
            HttpEntity resEntity = res.getEntity();

            //
            if (resEntity != null) {
                String resBody = EntityUtils.toString(resEntity);
                return resBody.substring(resBody.indexOf("url") + 6, resBody.indexOf("\"}"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Failed to upload image to ImgBB");
        }
        return null;
    }

    public String uploadImage(String base64Image) {
        RestTemplate restTemplate = new RestTemplate();

        // Tạo MultiValueMap để gửi yêu cầu POST
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("key", API_KEY);
        bodyMap.add("image", base64Image);

        // Gửi yêu cầu POST và nhận phản hồi từ ImgBB
        ImgBBResponse response = restTemplate.postForObject(IMG_BB_API_URL, bodyMap, ImgBBResponse.class);

        if (response != null && response.getData() != null) {
            return response.getData().getUrl();
        } else {
            throw new RuntimeException("Failed to upload image to ImgBB");
        }
    }

    public String convertByteArrayToBase64(byte[] imgBytes) {
        // Chuyển đổi mảng byte[] thành chuỗi Base64
        String base64String = Base64.getEncoder().encodeToString(imgBytes);

        // Loại bỏ các ký tự không hợp lệ nếu có
        base64String = base64String.replaceAll("[^a-zA-Z0-9+/=]", "");

        return base64String;
    }
}