package edu.uclm.esi.tecsistweb.ws;

import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

@Getter
public class SessionWS {

    private String name;
    private WebSocketSession session;
    private HttpSession httpSession;

    public SessionWS(String name, WebSocketSession session) {
        this.name = name;
        this.session = session;
    }

    public String getId() {
        return this.session.getId();
    }
}
