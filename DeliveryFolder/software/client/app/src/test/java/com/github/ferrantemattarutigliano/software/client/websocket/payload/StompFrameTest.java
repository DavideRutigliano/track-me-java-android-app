package com.github.ferrantemattarutigliano.software.client.websocket.payload;

import com.github.ferrantemattarutigliano.software.client.websocket.enums.StompCommandName;
import com.github.ferrantemattarutigliano.software.client.websocket.enums.StompHeaderName;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StompFrameTest {
    private StompFrame stompFrame;

    @Before
    public void initStompFrame(){
        stompFrame = new StompFrame();
    }

    @Test
    public void buildTest(){
        stompFrame.addStompCommand(StompCommandName.CONNECT);
        stompFrame.addStompHeader(StompHeaderName.ID, "1");
        String result = stompFrame.build();
        String expected =   "CONNECT\n" +
                            "id:1\n" +
                            "\n" +
                            "\u0000";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void buildMultipleHeadersTest(){
        stompFrame.addStompCommand(StompCommandName.SEND);
        stompFrame.addStompHeader(StompHeaderName.ID, "10")
                  .addStompHeader(StompHeaderName.DESTINATION, "hell")
                  .addStompHeader(StompHeaderName.CONTENT_TYPE, "test");
        String result = stompFrame.build();
        String expected =   "SEND\n" +
                            "id:10\n" +
                            "destination:hell\n" +
                            "content-type:test\n" +
                            "\n" +
                            "\u0000";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void buildWithBody(){
        stompFrame.addStompCommand(StompCommandName.ACK);
        stompFrame.addStompHeader(StompHeaderName.ID, "10")
                .addStompHeader(StompHeaderName.DESTINATION, "hell")
                .addStompHeader(StompHeaderName.CONTENT_TYPE, "test")
                .addBody("Hello darkness my old friend");
        String result = stompFrame.build();
        String expected =   "ACK\n" +
                            "id:10\n" +
                            "destination:hell\n" +
                            "content-type:test\n" +
                            "\n" +
                            "Hello darkness my old friend\n" +
                            "\u0000";
        Assert.assertEquals(expected, result);
    }
}