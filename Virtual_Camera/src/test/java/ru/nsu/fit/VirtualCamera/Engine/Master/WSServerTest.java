package ru.nsu.fit.VirtualCamera.Engine.Master;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.junit.Test;

import java.net.URI;
import java.util.concurrent.Future;

import static org.junit.Assert.fail;

public class WSServerTest {
  @Test
  public void testWS() {
    try {
      WSServer server = new WSServer(8081);
      server.start();
      URI uri = URI.create("ws://localhost:8081/api/");

      WebSocketClient client = new WebSocketClient();
      try {
        try {
          client.start();
          // The socket that receives events
          MasterWebSocket socket = new MasterWebSocket();
          // Attempt Connect
          Future<Session> fut = client.connect(socket, uri);
          // Wait for Connect
          Session session = fut.get();
          // Send a message
          session.getRemote().sendString("Hello");
          // Send another message
          session.getRemote().sendString("Goodbye");
          // Close session
          session.close();
        } finally {
          client.stop();
        }
      } catch (Throwable t) {
        t.printStackTrace(System.err);
        fail();
      }
      server.stop();
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }
}
