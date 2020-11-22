package ru.nsu.fit.VirtualCamera.Engine.Master;

import java.io.IOException;
import org.json.JSONException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.json.JSONObject;

public class MasterWebSocket extends WebSocketAdapter {
  static ConfigurationHandler configurator = new ConfigurationHandler();

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
      JSONObject request = new JSONObject(message);
      if (!request.has("command")) {
        throw new IllegalArgumentException("No field 'command' was found in request");
      }
      String command = request.getString("command");
      switch (command) {
        case "configure":
          {
            if (!request.has("body")) {
              throw new IllegalArgumentException("No field 'body' was found in request");
            }
            configurator.process(request.getJSONObject("body"));
            reply("New configuration is loaded");
            break;
          }
        default:
          reply("Unsupported option");
      }

    } catch (IllegalArgumentException | JSONException e) {
      reply(e.toString());
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

  private void reply(String message) {
    try {
      getSession().getRemote().sendString(message);
    } catch (IOException e) {
      e.printStackTrace(); // TODO log errors
    }
  }
}
