package ru.nsu.fit.virtualcamera.cli;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class ClientSocket {
  private Session session;

  private CountDownLatch latch = new CountDownLatch(1);

  @OnWebSocketMessage
  public void onMessage(String message) {
    System.out.println("Message received from server: " + message);
  }

  @OnWebSocketConnect
  public void onConnect(Session session) {
    System.out.println("Connected to server");
    this.session = session;
    latch.countDown();
  }

  void sendMessage(String str) {
    try {
      session.getRemote().sendString(str);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  CountDownLatch getLatch() {
    return latch;
  }

}
