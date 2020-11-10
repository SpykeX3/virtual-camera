package ru.nsu.fit.VirtualCamera.Engine.Master;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;

public class DeviceRouter implements HttpHandler {
  String prefix;
  ConfigurationHandler confHandler;

  public DeviceRouter(String prefix) {
    this.prefix = prefix;
    this.confHandler = new ConfigurationHandler();
  }

  private void badRequest(HttpExchange exchange) throws IOException {
    byte[] response = "{\"error\":\"Bad request\"}".getBytes();
    exchange.sendResponseHeaders(400, response.length);
    OutputStream os = exchange.getResponseBody();
    os.write(response);
    os.close();
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String url = exchange.getRequestURI().getPath().substring(prefix.length());

    String[] parameters = url.split("/");
    if (parameters.length != 2) {
      badRequest(exchange);
      return;
    }
    String id = parameters[0];
    String action = parameters[1];

    switch (action) {
      case "configure":
        {
          confHandler.handle(exchange, id);
          break;
        }
      default:
        {
          badRequest(exchange);
        }
    }
    exchange.close();
  }
}
