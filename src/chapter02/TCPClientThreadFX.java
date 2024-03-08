package chapter02;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;

public class TCPClientThreadFX extends Application {
  private TCPClient tcpClient;
  private Thread receiveThread;

  private TextField tfIP = new TextField("202.116.195.71");
  private TextField tfPort = new TextField("9999");
  private Button btnConnect = new Button("连接");

  private Button btnExit = new Button("退出");
  private Button btnSend = new Button("发送");
  private Button btnOpen = new Button("加载");
  private Button btnSave = new Button("保存");

  private TextField tfSend = new TextField();
  private TextArea taDisplay = new TextArea();

  @Override
  public void start(Stage primaryStage) throws Exception {
    BorderPane mainPane = new BorderPane();

    HBox hBoxTop = new HBox();
    hBoxTop.setAlignment(Pos.CENTER);
    hBoxTop.getChildren().addAll(
      new Label("IP地址："),
      tfIP,
      new Label("端口："),
      tfPort,
      btnConnect
    );
    btnConnect.setOnAction(event -> {
      String ip = tfIP.getText().trim();
      String port = tfPort.getText().trim();
      try {
        tcpClient = new TCPClient(ip, port);
//        String firstMsg = tcpClient.receive();
//        taDisplay.appendText(firstMsg + "\n");
      } catch (IOException e) {
        taDisplay.appendText("服务器连接失败！" + e.getMessage() + "\n");
      }
    });
    mainPane.setTop(hBoxTop);

    VBox vBox = new VBox();
    vBox.setPadding(new Insets(10, 20, 10, 20));
    vBox.getChildren().addAll(
      new Label("信息显示区域"),
      taDisplay,
      new Label("信息输入区："), tfSend
    );
    VBox.setVgrow(taDisplay, Priority.ALWAYS);
    mainPane.setCenter(vBox);

    HBox hBox = new HBox();
    hBox.setSpacing(10);
    hBox.setPadding(new Insets(10, 20, 10, 20));
    hBox.setAlignment(Pos.CENTER_RIGHT);
    btnSend.setOnAction(event -> {
      String msg = tfSend.getText();
      taDisplay.appendText(msg + "\n");
      tfSend.clear();
    });
    btnExit.setOnAction(event -> {System.exit(0);});
    tfSend.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER) {
        String msg;
        if (event.isShiftDown()) {
          msg = "echo: " + tfSend.getText();
        } else {
          msg = tfSend.getText();
        }
        taDisplay.appendText(msg + "\n");
        tcpClient.send(msg);
        tfSend.clear();

        // String receive_msg = tcpClient.receive();
        // taDisplay.appendText(receive_msg + "\n");
      }
    });
    hBox.getChildren().addAll(btnSend, btnSave, btnOpen, btnExit);
    mainPane.setBottom(hBox);

    receiveThread = new Thread(() -> {
      String msg = null;
      while ((msg = tcpClient.receive()) != null) {
        String msgTemp = msg;
        Platform.runLater(() -> {
          taDisplay.appendText(msgTemp + "\n");
        });
      }
    });

    Scene scene = new Scene(mainPane, 700, 400);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
