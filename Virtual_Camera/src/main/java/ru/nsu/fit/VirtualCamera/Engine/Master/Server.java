package ru.nsu.fit.VirtualCamera.Engine.Master;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

  public static void main(String[] args) {
    HttpServer server;
    try {
      server = HttpServer.create(new InetSocketAddress(5051), 0);
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }
    server.createContext("/api/devices/", new DeviceRouter("/api/devices/"));
    server.start();
  }
}
