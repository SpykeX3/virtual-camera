package ru.nsu.fit.VirtualCamera.Engine.Master;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class ConfigurationHandler {

  public void handle(HttpExchange exchange, String id) throws IOException {
    // log
    System.out.println(exchange.getRequestURI());
    System.out.println(new String(exchange.getRequestBody().readAllBytes()));
    //

  }
}
