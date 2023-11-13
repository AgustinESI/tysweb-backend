package edu.uclm.esi.tecsistweb.model.dto;

import lombok.Data;

@Data
public class WebSocketChatMessage {

    private String type;
    private String name;
    private String receiver;
    private String content;

//    {"type":"IDENT", "name":"agustin"}

}
