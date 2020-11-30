package ru.nsu.fit.VirtualCamera.Engine.Master;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import ru.nsu.fit.VirtualCamera.Engine.Blocks.CameraReadingBlock;
import ru.nsu.fit.VirtualCamera.Engine.Blocks.CameraWritingBlock;
import ru.nsu.fit.VirtualCamera.Engine.Blocks.FileReadingBlock;
import ru.nsu.fit.VirtualCamera.Engine.Blocks.HorizontalMirror;
import ru.nsu.fit.VirtualCamera.Engine.Frame;
import ru.nsu.fit.VirtualCamera.Engine.FrameStream;
import ru.nsu.fit.VirtualCamera.Engine.FunctionalBlock;

import java.util.*;

public class ConfigurationHandler {
  private final HashSet<FunctionalBlock> activeBlocks;

  public ConfigurationHandler() {
    activeBlocks = new HashSet<>();
  }

  public synchronized void process(JSONObject jsonConfiguration) {
    stopBlocks();
    activeBlocks.clear();
    processFBTree(jsonConfiguration);
    startBlocks();
  }

  void stopBlocks() {
    for (FunctionalBlock block : activeBlocks) {
      block.kill();
    }
  }

  private void startBlocks() {
    for (FunctionalBlock block : activeBlocks) {
      new Thread(block).start();
    }
  }

  private FunctionalBlock createBlockByName(String blockName, List<String> args) {
    try {
      switch (blockName) {
        case "device_output":
          return new CameraWritingBlock(args);
        case "device_input":
          return new CameraReadingBlock(args);
        case "video_input":
          return new FileReadingBlock(args);
        case "horizontal_mirror":
          return new HorizontalMirror(args);
        default:
          throw new IllegalArgumentException("Unknown module name");
      }
    } catch (Exception e) {
      // TODO perform cleaning? Raise?
      e.printStackTrace();
    }
    return null;
  }

  private FrameStream processFBTree(JSONObject jsonFB) throws JSONException {
    FrameStream stream = new FrameStream(64);
    String moduleName = jsonFB.getString("module_name");
    ArrayList<String> argList = null;
    LinkedList<FrameStream> inputsList = new LinkedList<>();

    JSONArray args = jsonFB.optJSONArray("args");
    if (args != null) {
      int len = args.length();
      argList = new ArrayList<>(len);
      for (int i = 0; i < len; i++) {
        argList.add(args.getString(i));
      }
    }

    JSONArray inputs = jsonFB.optJSONArray("inputs");
    if (inputs != null) {
      int len = inputs.length();
      for (int i = 0; i < len; i++) {
        inputsList.addFirst(processFBTree(inputs.getJSONObject(i)));
      }
    }
    FunctionalBlock createdBlock;
    createdBlock = createBlockByName(moduleName, Objects.requireNonNullElseGet(argList, ArrayList::new));
    System.out.println("Created "+createdBlock.getClass().getName());
    for (FrameStream input : inputsList) {
      createdBlock.addInputStream(input);
    }
    createdBlock.addOutputStream(stream);
    activeBlocks.add(createdBlock);
    return stream;
  }
}
