package chapter02;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCPClient {
  private Socket socket;
  private PrintWriter pw;
  private BufferedReader br;

  public TCPClient(String ip, String port) throws IOException {
    socket = new Socket(ip, Integer.parseInt(port));

    OutputStream socketOut = socket.getOutputStream();
    pw = new PrintWriter(
      new OutputStreamWriter(socketOut, StandardCharsets.UTF_8), true
    );

    InputStream socketIn = socket.getInputStream();
    br = new BufferedReader(new InputStreamReader(socketIn, StandardCharsets.UTF_8));
  }

  public void send(String msg) {
    pw.println(msg);
  }

  public String receive() {
    String msg = null;
    try {
      msg = br.readLine();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return msg;
  }

  public void close() {
    if (socket != null) {
      try {
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
