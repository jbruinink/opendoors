package com.jdriven.opendoors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebsocketMessageService implements MessageService, WebSocketHandler {

    private static final Logger LOG = LoggerFactory.getLogger(WebsocketMessageService.class);

    private Map<String, WebSocketSession> sessions;

    WebsocketMessageService() {
        sessions = new ConcurrentHashMap<>();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOG.debug("Connection established with {}", session.getId());
        sessions.put(session.getId(), session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        LOG.debug("Received message from {}", session.getId());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        LOG.error("Transport error for session {}", session.getId(), exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        LOG.debug("Connection closed with {}", session.getId());
        sessions.remove(session.getId()).close();
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    @Override
    public void sendMessage(String message) {
        sessions.values().forEach(session -> {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                LOG.error("Unable to send message to {}", session.getId(), e);
            }
        });
    }
}
