package edu.uclm.esi.tecsistweb.model.dto;

import lombok.Data;

@Data
public class WebSocketMatchMessage {

    private String type;
    private String name;
    private String receiver;
    private String id_match;

//    {"type":"IDENT", "name":"agustin"}

}
