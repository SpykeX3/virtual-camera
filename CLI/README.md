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
      "args": ["other", "modules"]
  }
}
```
