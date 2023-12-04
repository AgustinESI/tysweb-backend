package edu.uclm.esi.tecsistweb.service;


import edu.uclm.esi.tecsistweb.model.User;
import edu.uclm.esi.tecsistweb.model.exception.TySWebException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


@Service
public class EmailSenderService extends HelperService {


    @Value("${email.url.brevo}")
    private String urlBrevo;
    @Value("${email.apikey.brevo}")
    private String apikey;
    @Value("${email.name.sender.name.brevo}")
    private String nameSender;
    @Value("${email.name.sender.mail.brevo}")
    private String emailSender;
    @Value("${email.title.brevo}")
    private String titleEmailWelcome;

    private static final String PATH_EMAIL_TEMPLATES = "email/";
    private static final String URL_CONFIRMATION = "http://localhost:4200/verification";


    public void sendEmailRegistration(User user, String file) {
        String body = "";

        String url = "http://";

        try {
            try (final DatagramSocket socket = new DatagramSocket()) {
                socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                String ip = socket.getLocalAddress().getHostAddress();
                url += ip;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        url += ":4200/verification";

        try {
            ClassPathResource resource = new ClassPathResource(PATH_EMAIL_TEMPLATES + file);
            body = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
            body = body.replace("[UserName]", user.getName());
            body = body.replace("[ConfirmationLink]", url + "?id=" + user.getId());
        } catch (Exception e) {
            LOGGER.error("Unnable to get the template email: " + file);
        }

        System.out.println(body);

        this.sendEmail(user, titleEmailWelcome, body);
    }


    private void sendEmail(User user, String subject, String body) {


        Map<String, String> headerRequest = new HashMap<>();
        headerRequest.put("accept", "application/json");
        headerRequest.put("content", " application/json");
        headerRequest.put("api-key", apikey);


        JSONObject sender = new JSONObject();
        sender.put("name", this.nameSender);
        sender.put("email", this.emailSender);

        JSONArray toJsonArray = new JSONArray();
        JSONObject receive = new JSONObject();
        receive.put("name", user.getName());
        receive.put("email", user.getEmail());
        toJsonArray.put(receive);

        JSONObject emailJson = new JSONObject();
        emailJson.put("sender", sender);
        emailJson.put("to", toJsonArray);
        emailJson.put("subject", subject);
        emailJson.put("htmlContent", body);

        try {
            this.doHTTPPost(urlBrevo, headerRequest, emailJson.toString());
        } catch (Exception e) {
            throw new TySWebException(HttpStatus.BAD_REQUEST, new Exception("Error while sending email"));
        }
    }
}
