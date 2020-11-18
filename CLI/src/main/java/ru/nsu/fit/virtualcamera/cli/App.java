package ru.nsu.fit.virtualcamera.cli;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class App {
  private static final String DESTINATION = "ws://localhost:8080/api/";

  public static void main(String[] args) {

    Options options = new Options();

    options.addOption("t", "target", true, "Target device")
        .addOption("v", "vsource", true, "Source video")
        .addOption("d", "dsource", true, "Source device")
        .addOption("h", "help", false, "Get help")
        .addOption("f", "file", true, "Read JSON from file");

    try {
      CommandLine cmd = new DefaultParser().parse(options, args);

      if (cmd.hasOption("h")) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("[-h] [-t target] [-v vsource | -d dsource]", options);
      } else {

        if (!cmd.hasOption("v") && !cmd.hasOption("d") && !cmd.hasOption("f")) {
          System.out.println("Source not specified");
          System.exit(1);
        }


        String target = null;
        if (cmd.hasOption("t")) {
          target = cmd.getOptionValue("t");
        } else if (!cmd.hasOption("f")) {
          System.out.println("Target not specified");
          System.exit(1);
        }

        List<String> mods = cmd.getArgList();
        JSONObject jsonObject = null;

        if (cmd.hasOption("f")) {
          try (FileReader reader = new FileReader(cmd.getOptionValue("f"))) {

            JSONTokener tokener = new JSONTokener(reader);

            jsonObject = new JSONObject(tokener);
          } catch (FileNotFoundException e) {
            System.out.println("File not found");
          } catch (IOException e) {
            e.printStackTrace();
          }
        } else {

          if (cmd.hasOption("v")) {
            String vsource = cmd.getOptionValue("v");

            jsonObject = new JSONObject()
                .put("module_name", "video_input")
                .put("args", new JSONArray().put(vsource));

          } else {
            String dsource = cmd.getOptionValue("d");

            jsonObject = new JSONObject()
                .put("module_name", "device_input")
                .put("args", new JSONArray().put(dsource));
          }

          for (String mod : mods) {
            jsonObject = new JSONObject()
                .put("module_name", mod)
                .put("args", new JSONArray().put(jsonObject));
          }

          jsonObject = new JSONObject()
              .put("module_name", "device_output")
              .put("args", new JSONArray().put(target).put(jsonObject));

          jsonObject = new JSONObject()
              .put("command", "configure")
              .put("body", jsonObject);
        }

        ClientSocket socket = new ClientSocket();
        WebSocketClient client = new WebSocketClient();

        try {
          client.start();
          client.connect(socket, new URI(DESTINATION));
          socket.getLatch().await();
          socket.sendMessage(jsonObject.toString());
          Thread.sleep(1000);
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          try {
            client.stop();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }
}
