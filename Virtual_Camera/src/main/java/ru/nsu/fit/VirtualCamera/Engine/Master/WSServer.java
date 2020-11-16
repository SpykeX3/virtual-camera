package ru.nsu.fit.VirtualCamera.Engine.Master;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.server.NativeWebSocketServletContainerInitializer;
import org.eclipse.jetty.websocket.server.WebSocketUpgradeFilter;

import javax.servlet.ServletException;

public class WSServer {
  Server server;

  public WSServer(int port) throws ServletException {
    server = new Server();
    ServerConnector connector = new ServerConnector(server);
    connector.setPort(port);
    server.addConnector(connector);

    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);

    NativeWebSocketServletContainerInitializer.configure(
        context,
        (servletContext, nativeWebSocketConfiguration) -> {
          nativeWebSocketConfiguration.getPolicy().setMaxTextMessageBufferSize(65535);

          nativeWebSocketConfiguration.addMapping("/api/", MasterWebSocket.class);
        });

    WebSocketUpgradeFilter.configure(context);
  }

  public void start() throws Exception {
    server.start();
  }

  public void join() throws InterruptedException {
    server.join();
  }

  public void stop() throws Exception {
    server.stop();
  }

  public static void main(String[] args) {
    try {
      WSServer server = new WSServer(8080);
      server.start();
      server.join();
    } catch (Throwable t) {
      t.printStackTrace(System.err);
    }
  }
}
