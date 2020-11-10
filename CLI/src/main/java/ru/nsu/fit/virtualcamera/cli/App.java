package ru.nsu.fit.virtualcamera.cli;

import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONArray;
import org.json.JSONObject;

public class App {
  private static final String DAEMON_URL = "http://localhost:5150";

  public static void main(String[] args) {

    Options options = new Options();

    options.addOption("t", "target", true, "Target device")
        .addOption("v", "vsource", true, "Source video")
        .addOption("d", "dsource", true, "Source device")
        .addOption("h", "help", false, "Get help");

    try {
      CommandLine cmd = new DefaultParser().parse(options, args);

      if (cmd.hasOption("h")) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("[-h] [-t target] [-v vsource | -d dsource]", options);
      } else {

        if (!cmd.hasOption("v") && !cmd.hasOption("d")) {
          System.out.println("Source not specified");
          System.exit(1);
        }


        String target = null;
        if (cmd.hasOption("t")) {
          target = cmd.getOptionValue("t");
        } else {
          System.out.println("Target not specified");
          System.exit(1);
        }

        List<String> mods = cmd.getArgList();
        JSONObject jsonObject;

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

        new RequestHandler().sendPOST(
            jsonObject.toString(), DAEMON_URL + "/api/devices/video0/configure");

      }
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }
}
