package edu.uclm.esi.tecsistweb.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uclm.esi.tecsistweb.model.User;
import edu.uclm.esi.tecsistweb.model.exception.TySWebException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

@Service
public class WeatherService {

    private static final String API_KEY = "61dceff01df344fda3c151155230302";
    private static final String DEFAULT_PATH = "https://api.weatherapi.com/v1/forecast.json";


    public void getTemperature(User user, String latitude, String longitude) {
        String path = DEFAULT_PATH + "?key=" + API_KEY + "&q=" + latitude + "," + longitude + "&days=1&aqi=yes&alerts=yes";


        try {
            URL url = new URL(path);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            try {
                con.setRequestMethod("GET");
            } catch (ProtocolException e) {
                throw new RuntimeException(e);
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            String json = content.toString();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(json);

            String city = jsonNode.get("location").get("name").asText();
            String region = jsonNode.get("location").get("region").asText();
            String country = jsonNode.get("location").get("country").asText();
//            user.setCity(city + ", " + region + ", " + country);
            user.setCity(city);

            String temperature = jsonNode.get("current").get("temp_c").asText()+" ºC";
            user.setTemperature(temperature);

        } catch (MalformedURLException | ProtocolException e) {
            throw new TySWebException(HttpStatus.SERVICE_UNAVAILABLE, new Exception("Unnable to calculate location"));
        } catch (IOException e) {
            throw new TySWebException(HttpStatus.SERVICE_UNAVAILABLE, new Exception("Unnable to calculate location"));
        }
    }
}
