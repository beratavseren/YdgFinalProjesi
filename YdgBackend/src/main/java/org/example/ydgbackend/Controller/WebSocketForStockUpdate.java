package org.example.ydgbackend.Controller;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/ws/stockUpdate/{werehouseId}")
@Component
public class WebSocketForStockUpdate {

    private static final Map<Long, Set<Session>> RoomToSessionsMap = new ConcurrentHashMap<>();
    private static final Map<Session, Long> SessionToRoomMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("werehouseId") String werehouseId) {
        Long id = Long.parseLong(werehouseId);
        RoomToSessionsMap.putIfAbsent(id, ConcurrentHashMap.newKeySet());
        RoomToSessionsMap.get(id).add(session);
        SessionToRoomMap.put(session, id);
        System.out.println("Stock Update WebSocket Connected");
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        System.out.println("Stock Update WebSocket Message Received");
        System.out.println(message);
    }

    @OnClose
    public void onClose(Session session) {
        Long id = SessionToRoomMap.get(session);
        RoomToSessionsMap.get(id).remove(session);

        if (RoomToSessionsMap.get(id).isEmpty()) {
            RoomToSessionsMap.remove(id);
        }

        SessionToRoomMap.remove(session);

        System.out.println("Stock Update WebSocket Disconnected");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        onClose(session);
        System.out.println("Stock Update WebSocket Error");
        System.out.println(throwable.getMessage());
    }

    public static void sendMessage(String message, Long id) {
        Set<Session> sessions = RoomToSessionsMap.get(id);

        for (Session s : sessions) {
            try {
                s.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}