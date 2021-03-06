# CLI prototype

The command line interface currently supports sending linear configuration requests to the virtual camera daemon. This means that each functional block takes exactly one argument. The chain starts with either a device or video and ends with a device. For example: `file` -> `block1` -> `block2` -> `video1` or `video1` -> `block1` -> `block2` -> `video2` are possible.

A request can be also read from a JSON file (see syntax below). 

## Syntax
Using a video as the source:
```
-v file.avi -t video1 block1 block2
```
Using another device as the source:
```
-d video1 -t video2 block1 block2
```
Reading from file:
```
-f config.json
```
Additionally, width and height (in pixels) for the output device can be specified as arguments for the corresponding functional block. Use `-x` and `-y` keys for that:
```
-v file.avi -t video1 -x 1920 -y 1080
```

## Details
Currently the CLI connects to the daemon via WebSocket. Currently it supports the following:
* Configuration requests

### Configuration requests
Configuration requests are JSON with the following structure:
```json
{
  "command": "configure",
  "body": {
    "module_name": "module",
    "inputs": ["other", "modules"],
    "args": ["arguments", "for", "blocks"]
  }
}
```
In case of reading configuration from command line (rather than file), the arguments arrays are empty for all functional blocks except the source video/device and target device where the video/device is passed as single argument. Example of such request:
```json
{
  "command": "configure",
  "args": ["video1"],
  "body": {
    "module_name": "device_output",
    "inputs": [
      {
        "module_name": "block1",
        "args": [],
        "inputs": [
          {
            "module_name": "video_input",
            "args": "/file.avi",
            "inputs": []
          }
        ]
      }
    ]
  }
}
```
