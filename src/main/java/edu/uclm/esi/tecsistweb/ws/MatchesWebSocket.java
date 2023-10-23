package edu.uclm.esi.tecsistweb.ws;

import com.google.gson.Gson;
import edu.uclm.esi.tecsistweb.model.dto.WebSocketMessage;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class MatchesWebSocket extends TextWebSocketHandler {


    List<WebSocketSession> list = new ArrayList<>();
    private Map<String, SessionWS> sessions_map_name = new HashMap<>();
    private Map<String, SessionWS> sessions_map_id = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        list.add(session);
        log.info("[INFO] Added new websocket session");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        //ws://localhost:8080/ws-matches
        WebSocketMessage _message = new Gson().fromJson(message.getPayload(), WebSocketMessage.class);

        switch (_message.getType()) {
            case "IDENT":
                //{"type":"IDENT", "name":"macario" }
                SessionWS _sessionws = new SessionWS(_message.getName(), session);

                this.sessions_map_name.put(_message.getName(), _sessionws);
                this.sessions_map_id.put(session.getId(), _sessionws);
                break;
            case "PRIVATE.MESSAGE":
                //{"type":"PRIVATE.MESSAGE", "receiver":"macario", "content": "hola"}

                String _sender = sessions_map_id.get(session.getId()).getName();

                JSONObject response = new JSONObject()
                        .put("type", _message.getType())
                        .put("content", _message.getContent())
                        .put("receiver", _message.getReceiver())
                        .put("sender", _sender);

                SessionWS _sessionWSReceiver = this.sessions_map_name.get(_message.getReceiver());

                if (_sessionWSReceiver != null) {
                    WebSocketSession _sessionReceiver = _sessionWSReceiver.getSession();
                    _sessionReceiver.sendMessage(new TextMessage(response.toString()));
                } else {
                    response.put("type", "CLOSED.SESSION");
                    session.sendMessage(new TextMessage(response.toString()));
                }


            default:
                break;
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        SessionWS _sessionWS = this.sessions_map_id.remove(session.getId());
        this.sessions_map_name.remove(_sessionWS.getName());
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    }
}
