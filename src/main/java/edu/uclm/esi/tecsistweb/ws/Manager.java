package edu.uclm.esi.tecsistweb.ws;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Manager {

    @Getter
    @Setter
    List<WebSocketSession> webSocketSessionList = new ArrayList<>();
    @Getter
    @Setter
    private Map<String, SessionWS> sessions_map_name = new HashMap<>();
    @Getter
    @Setter
    private Map<String, SessionWS> sessions_map_id = new HashMap<>();
    private static Manager manager;
    private Manager() {
    }

    public static Manager get(){
        if (manager==null){
            manager = new Manager();
        }
        return manager;
    }
}
