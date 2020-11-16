package ru.nsu.fit.VirtualCamera.Engine.Master;

import java.io.IOException;
import java.util.Locale;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

public class MasterWebSocket extends WebSocketAdapter {
  @Override
  public void onWebSocketConnect(Session sess) {
    super.onWebSocketConnect(sess);
    System.out.println("Socket Connected: " + sess);
  }

  @Override
  public void onWebSocketText(String message) {
    super.onWebSocketText(message);
    System.out.println("Received TEXT message: " + message);
    try {
      getSession().getRemote().sendString(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onWebSocketClose(int statusCode, String reason) {
    super.onWebSocketClose(statusCode, reason);
    System.out.println("Socket Closed: [" + statusCode + "] " + reason);
  }

  @Override
  public void onWebSocketError(Throwable cause) {
    super.onWebSocketError(cause);
    cause.printStackTrace(System.err);
  }
}
