package edu.uclm.esi.tecsistweb.service;

import edu.uclm.esi.tecsistweb.model.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.regex.Pattern;

public class HelperService {


    public static final Logger LOGGER = (Logger) LoggerFactory.getLogger("TySWeb_Service_Handler");
    protected final String email_regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

    public enum EMAIL_PATH {
        EMAIL_PATH_REGISTER_TEMPLATE("email_register_template.html");

        private final String path;

        EMAIL_PATH(String path) {
            this.path = path;
        }

        public String path() {
            return path;
        }
    }

    public void getTimestamp(Match match) {
        LocalDate stamp = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedStamp = stamp.format(formatter);
        match.setTimestamp(formattedStamp);
    }


    public String doHTTPPost(String url, Map<String, String> header, String body) throws IOException {
        String out = "";

        URL apiUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        for (Map.Entry<String, String> set : header.entrySet()) {
            connection.setRequestProperty(set.getKey(), set.getValue());
        }


        try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            wr.writeBytes(body);
            wr.flush();
        }

        int responseCode = connection.getResponseCode();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            out = response.toString();

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out;
    }
}
