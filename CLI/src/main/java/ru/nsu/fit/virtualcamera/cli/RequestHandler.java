package ru.nsu.fit.virtualcamera.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class RequestHandler {
  void sendPOST(String body, String destination) {
    try {
      URL url = new URL(destination);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("POST");
      con.setDoOutput(true);
      con.connect();
      OutputStream outputStream = con.getOutputStream();
      outputStream.write(body.getBytes());
      outputStream.close();

      InputStream inputStream = con.getInputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

      StringBuilder response = new StringBuilder();
      String responseLine;
      while ((responseLine = reader.readLine()) != null) {
        response.append(responseLine);
      }
      inputStream.close();
      con.disconnect();

      System.out.println(response);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
