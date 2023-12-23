package edu.uclm.esi.tecsistweb.http;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

public class Client implements Serializable {

    private BasicCookieStore cookieStore = new BasicCookieStore();
//
//    public static void main(String[] args) {
//        for (int i = 0; i < 10; i++) {
//            sendProfileImage();
//        }
//    }

    public static void sendProfileImage() {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpGet get = new HttpGet("https://thispersondoesnotexist.com/");
            try (CloseableHttpResponse response = client.execute(get)) {
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() != 200) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not found image");
                }
                HttpEntity entity = response.getEntity();
                byte[] bytes = EntityUtils.toByteArray(entity);

                String id = UUID.randomUUID().toString();
                try (FileOutputStream fos = new FileOutputStream("C:/Users/Agustin Sanchez/Downloads/foto_" + id + ".jpg")) {
                    fos.write(bytes);
                }

                String base64Chain = Base64.encodeBase64String(bytes);
                try (FileOutputStream fos = new FileOutputStream("C:/Users/Agustin Sanchez/Downloads/foto_" + id + ".txt")) {
                    fos.write(base64Chain.getBytes());
                }

            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        } catch (IOException e1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e1.getMessage());
        }
    }

    public String sendGet(String url) {
        try (CloseableHttpClient client = HttpClientBuilder.create().setDefaultCookieStore(this.cookieStore).build()) {
            HttpGet get = new HttpGet(url);
            try (CloseableHttpResponse response = client.execute(get)) {
                StatusLine statusLine = response.getStatusLine();

                if (statusLine.getStatusCode() != 200) {
                    return null;
                }

                HttpEntity entity = response.getEntity();
                String responseText = EntityUtils.toString(entity);
                return responseText;

            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        } catch (IOException e1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e1.getMessage());
        }
    }

    public JSONObject sendPost(String url, JSONObject payload) {
        try (CloseableHttpClient client = HttpClientBuilder.create().setDefaultCookieStore(this.cookieStore).build()) {
            HttpPost post = new HttpPost(url);
            try {
                HttpEntity entity = new StringEntity(payload.toString());
                post.setEntity(entity);
                post.setHeader("Accept", "application/json");
                post.setHeader("Content-type", "application/json");

                CloseableHttpResponse response = client.execute(post);
                entity = response.getEntity();
                String responseText = EntityUtils.toString(entity);
                if (responseText == null || responseText.length() == 0) return null;
                return new JSONObject(responseText);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        } catch (IOException e1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e1.getMessage());
        }
    }

    public JSONObject sendCurlPost(JSONObject jsoPayload, String body) {
        try (CloseableHttpClient client = HttpClientBuilder.create().setDefaultCookieStore(this.cookieStore).build()) {
            HttpPost post = new HttpPost(jsoPayload.getString("url"));
            JSONArray jsaHeaders = jsoPayload.getJSONArray("headers");
            for (int i = 0; i < jsaHeaders.length(); i++) {
                post.setHeader(jsaHeaders.getString(i), jsaHeaders.getString(i + 1));
                i++;
            }

            JSONObject jsoData = jsoPayload.getJSONObject("data");
            HttpEntity entity = new StringEntity(jsoData.toString());
            post.setEntity(entity);

            CloseableHttpResponse response = client.execute(post);
            entity = response.getEntity();
            String responseText = EntityUtils.toString(entity);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() >= 400)
                throw new ResponseStatusException(HttpStatus.resolve(statusLine.getStatusCode()), statusLine.getReasonPhrase());
            if (responseText == null || responseText.length() == 0) return null;
            return new JSONObject(responseText);
        } catch (IOException e1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e1.getMessage());
        }
    }
}