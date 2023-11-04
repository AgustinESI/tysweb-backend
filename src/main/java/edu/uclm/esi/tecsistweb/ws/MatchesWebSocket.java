package edu.uclm.esi.tecsistweb.ws;

import com.google.gson.Gson;
import edu.uclm.esi.tecsistweb.model.dto.WebSocketMessage;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;


@Slf4j
@Component
public class MatchesWebSocket extends TextWebSocketHandler {


    private static final String INDENT = "INDENT";
    private static final String PRIVATE_MESSAGE = "PRIVATE.MESSAGE";
    private static final String NEW_USER = "NEW.USER";
    private static final String WELCOME = "WELCOME";
    private static final String CLOSED_SESSION = "CLOSED.SESSION";


    List<WebSocketSession> list = new ArrayList<>();
    private Map<String, SessionWS> sessions_map_name = new HashMap<>();
    private Map<String, SessionWS> sessions_map_id = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        this.list.add(session);
        log.info("[INFO] Added new websocket session");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        //ws://localhost:8080/ws-matches
        WebSocketMessage _message = new Gson().fromJson(message.getPayload(), WebSocketMessage.class);

        switch (_message.getType()) {
            case INDENT:
                //{"type":"IDENT", "name":"macario" }
                SessionWS _sessionws = new SessionWS(_message.getName(), session);

                this.sessions_map_name.put(_message.getName(), _sessionws);
                this.sessions_map_id.put(session.getId(), _sessionws);

                this.expand(session, "type", NEW_USER, "name", _sessionws.getName());
                this.notifyMySelf(session);

                break;
            case PRIVATE_MESSAGE:
                //{"type":"PRIVATE.MESSAGE", "receiver":"macario", "content": "hola"}

                String _sender = sessions_map_id.get(session.getId()).getName();

                JSONObject response = new JSONObject().put("type", _message.getType()).put("content", _message.getContent()).put("receiver", _message.getReceiver()).put("sender", _sender);

                SessionWS _sessionWSReceiver = this.sessions_map_name.get(_message.getReceiver());

                if (_sessionWSReceiver != null) {
                    WebSocketSession _sessionReceiver = _sessionWSReceiver.getSession();
                    _sessionReceiver.sendMessage(new TextMessage(response.toString()));
                } else {
                    response.put("type", CLOSED_SESSION);
                    session.sendMessage(new TextMessage(response.toString()));
                }


            default:
                break;
        }
    }

    /**
     * El usuario que se logea primero debe notificarse a si mismo del resto de usuarios conectados
     *
     * @param session
     */
    private void notifyMySelf(WebSocketSession session) {
        JSONObject jso = new JSONObject();
        jso.put("type", WELCOME);

        JSONArray jsonArray = new JSONArray();

        Collection<SessionWS> connectedUsers = this.sessions_map_name.values();

        for (SessionWS _sessionWS : connectedUsers) {
            if (_sessionWS.getSession() != session) {
                jsonArray.put(_sessionWS.getName());
            }
        }
        jso.put("users", jsonArray);

        try {
            session.sendMessage(new TextMessage(jso.toString()));
        } catch (IOException e) {
            this.deleteSession(session);
        }
    }

    /**
     * Posicion impar: valor
     * Posicion impar: clave
     * type: NEW.USER, name: Pepe, age: 20
     * Notoficamos para que el resto de usuarios sepan que ha entrado un nuevo usuario al chat
     * Difundiremos a todos menos a la sesion que lo envia
     *
     * @return
     */
    private void expand(WebSocketSession sender, Object... keyValue) {
        JSONObject jso = new JSONObject();

        for (int i = 0; i < keyValue.length; i += 2) {
            String key = keyValue[i].toString();
            String value = keyValue[i + 1].toString();
            jso.put(key, value);
        }

        for (WebSocketSession session : this.list) {
            if (session != sender) {
                try {
                    session.sendMessage(new TextMessage(jso.toString()));
                } catch (IOException e) {
                    this.deleteSession(session);
                }
            }
        }

    }

    private void deleteSession(WebSocketSession session) {
        this.list.remove(session);
        SessionWS _sessionWS = this.sessions_map_id.remove(session.getId());
        this.sessions_map_name.remove(_sessionWS.getName());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
       deleteSession(session);
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    }
}
