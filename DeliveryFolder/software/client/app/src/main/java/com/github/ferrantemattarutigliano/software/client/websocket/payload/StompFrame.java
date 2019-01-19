package com.github.ferrantemattarutigliano.software.client.websocket.payload;

import android.support.annotation.NonNull;

import com.github.ferrantemattarutigliano.software.client.websocket.enums.StompCommandName;
import com.github.ferrantemattarutigliano.software.client.websocket.enums.StompHeaderName;
import com.github.ferrantemattarutigliano.software.client.websocket.enums.StompSymbolName;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class StompFrame {
    private List<StompHeader> stompHeaders;
    private String stompCommand;
    private String stompBody;

    public StompFrame() {
        stompHeaders = new LinkedList<>();
    }

    public StompFrame addStompCommand(@NonNull StompCommandName command){
        this.stompCommand = command.toString();
        return this;
    }

    public StompFrame addStompHeader(@NonNull StompHeaderName header, @NonNull String value){
        StompHeader h = new StompHeader(header.toString(), value);
        stompHeaders.add(h);
        return this;
    }

    public StompFrame addBody(Serializable body){
        stompBody = body.toString();
        return this;
    }

    public String build(){
        String newline = StompSymbolName.NEWLINE.toString();
        String separator = StompSymbolName.SEPARATOR.toString();
        String terminator = StompSymbolName.TERMINATOR.toString();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(stompCommand).append(newline);
        for(int i = 0; i < stompHeaders.size(); ++i){
            String hName = stompHeaders.get(i).getHeaderName();
            String hValue = stompHeaders.get(i).getHeaderValue();
            stringBuilder.append(hName).append(separator).append(hValue).append(newline);
        }
        stringBuilder.append(newline);
        if(stompBody != null){
            stringBuilder.append(stompBody).append(newline);
        }
        stringBuilder.append(terminator);
        return stringBuilder.toString();
    }

    public List<StompHeader> getStompHeaders() {
        return stompHeaders;
    }

    public String getStompCommand() {
        return stompCommand;
    }

    public String getStompBody() {
        return stompBody;
    }
}
