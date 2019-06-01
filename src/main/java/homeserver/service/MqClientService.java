package homeserver.service;

import homeserver.model.Humidity;
import homeserver.model.Temperature;

import java.io.IOException;
import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class MqClientService {
    
    public Temperature currentTemperature;
    public Humidity currentHumidity;

    public void handleTemperature(byte[] body) throws IOException {
        String message = new String(body, "UTF-8");
        currentTemperature = new Temperature("RPi.DHT11.Temperature", new Date(), Double.valueOf(message));
    }
    
    public void handleHumidity(byte[] body) throws IOException {
        String message = new String(body, "UTF-8");
        currentHumidity = new Humidity("RPi.DHT11.Humidity", new Date(), Double.valueOf(message));
    }
}
