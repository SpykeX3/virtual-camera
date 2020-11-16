package ru.nsu.fit.VirtualCamera.Engine.Master;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.server.NativeWebSocketServletContainerInitializer;
import org.eclipse.jetty.websocket.server.WebSocketUpgradeFilter;

import javax.servlet.ServletException;

public class WSServer {

  public static void main(String[] args) {
    Server server = new Server();
    ServerConnector connector = new ServerConnector(server);
    connector.setPort(8080);
    server.addConnector(connector);

    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);

    NativeWebSocketServletContainerInitializer.configure(
        context,
        (servletContext, nativeWebSocketConfiguration) -> {
          nativeWebSocketConfiguration.getPolicy().setMaxTextMessageBufferSize(65535);

          nativeWebSocketConfiguration.addMapping("/api/", WSServer.class);
        });

    try {
      WebSocketUpgradeFilter.configure(context);
    } catch (ServletException e) {
      e.printStackTrace();
    }

    try {
      server.start();
      server.join();
    } catch (Throwable t) {
      t.printStackTrace(System.err);
    }
  }
}
