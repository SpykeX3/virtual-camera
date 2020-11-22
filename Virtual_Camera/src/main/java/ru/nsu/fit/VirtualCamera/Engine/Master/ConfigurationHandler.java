package ru.nsu.fit.VirtualCamera.Engine.Master;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import ru.nsu.fit.VirtualCamera.Engine.Frame;
import ru.nsu.fit.VirtualCamera.Engine.FrameStream;
import ru.nsu.fit.VirtualCamera.Engine.FunctionalBlock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class ConfigurationHandler {
  private HashSet<FunctionalBlock> activeBlocks;

  public ConfigurationHandler() {
    activeBlocks = new HashSet<>();
  }

  public synchronized void process(JSONObject jsonConfiguration) {
    stopBlocks();
    activeBlocks.clear();
    processFBTree(jsonConfiguration);
    startBlocks();
  }

  private void stopBlocks() {
    for (FunctionalBlock block : activeBlocks) {
      block.kill();
    }
  }

  private void startBlocks() {
    for (FunctionalBlock block : activeBlocks) {
      new Thread(block).start();
    }
  }

  private FunctionalBlock createBlockByName(String blockName) {
    return new FunctionalBlock() {
      @Override
      public Frame performWork() {

        return null;
      }
    };
    // TODO create actual blocks
  }

  private FunctionalBlock createBlockByName(String blockName, List<String> args) {
    return new FunctionalBlock() {
      @Override
      public Frame performWork() {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
        return null;
      }
    };
    // TODO create actual blocks with args
  }

  private FrameStream processFBTree(JSONObject jsonFB) throws JSONException {
    FrameStream stream = new FrameStream();
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
    if (argList != null) {
      createdBlock = createBlockByName(moduleName, argList);
    } else {
      createdBlock = createBlockByName(moduleName);
    }
    for (FrameStream input : inputsList) {
      createdBlock.addInputStream(input);
    }
    createdBlock.addOutputStream(stream);
    activeBlocks.add(createdBlock);
    return stream;
  }
}
