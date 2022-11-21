package application;

import application.controller.Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.*;
import java.net.Socket;

public class Player extends  Application{
    Socket socket;
    int turn=-1;
    int nowTurn=0;
    Controller controller;
    int[][] chessBoard = new int[3][3];

    OutputStream os;
    Stage primaryStage;
    Pane root;

    @Override
    public void start(Stage primaryStage){
        try {
            this.primaryStage=primaryStage;
            primaryStage.setTitle("Tic Tac Toe");
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
            primaryStage.show();
            ReadServerThread read=new ReadServerThread();
            read.start();
            primaryStage.setOnCloseRequest(windowEvent -> {System.exit(0);});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
        try {
            socket=new Socket("localhost",7503);
            System.out.println("客户端建立");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("mainUI.fxml"));
            root = fxmlLoader.load();
            controller = fxmlLoader.getController();
            controller.init(this);
            System.out.println("controllerInit");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ReadServerThread extends Thread{
        BufferedReader reader ;
        String s;
        ReadServerThread() throws IOException {}
        @Override
        public void run() {
            super.run();
            {
                System.out.println("readThread run");
                try {
                    reader= new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    while (true){
                while ((s=reader.readLine())!=null) {
                    System.out.println("others action: "+s);
                        if(s.equals("Match")) {Platform.runLater(()->new Message(Alert.AlertType.INFORMATION).display(s,"Match successfully!"));};
                        if(s.equals("Wait")) {Platform.runLater(() -> new Message(Alert.AlertType.INFORMATION).display(s, "Please wait for another player!"));}
                        if (s.equals("1")) {turn=1;nowTurn=1;Platform.runLater(()->controller.init(Player.this));}
                        if (s.equals("2")) {turn=2;nowTurn=1;Platform.runLater(()->controller.init(Player.this));}
                        if (s.equals("Turn")){if (nowTurn==1) nowTurn=2;else nowTurn=1;Platform.runLater(()->controller.init(Player.this));}
                        if(s.equals("0,0")){setChessBoard(0,0,nowTurn);Platform.runLater(()->controller.init(Player.this));}
                        if(s.equals("0,1")){setChessBoard(0,1,nowTurn);Platform.runLater(()->controller.init(Player.this));}
                        if(s.equals("0,2")){setChessBoard(0,2,nowTurn);Platform.runLater(()->controller.init(Player.this));}
                        if(s.equals("1,0")){setChessBoard(1,0,nowTurn);Platform.runLater(()->controller.init(Player.this));}
                        if(s.equals("1,1")){setChessBoard(1,1,nowTurn);Platform.runLater(()->controller.init(Player.this));}
                        if(s.equals("1,2")){setChessBoard(1,2,nowTurn);Platform.runLater(()->controller.init(Player.this));}
                        if(s.equals("2,0")){setChessBoard(2,0,nowTurn);Platform.runLater(()->controller.init(Player.this));}
                        if(s.equals("2,1")){setChessBoard(2,1,nowTurn);Platform.runLater(()->controller.init(Player.this));}
                        if(s.equals("2,2")){setChessBoard(2,2,nowTurn);Platform.runLater(()->controller.init(Player.this));}
                        if (s.equals("Win")) {Platform.runLater(()->new Message(Alert.AlertType.INFORMATION).display(s,"You win!"));Platform.runLater(()->primaryStage.close());send("Bye");}
                        if (s.equals("Lose")) {Platform.runLater(()->new Message(Alert.AlertType.INFORMATION).display(s,"You lose!"));Platform.runLater(()->primaryStage.close());send("Bye");}
                        if (s.equals("Tie")) {Platform.runLater(()->new Message(Alert.AlertType.INFORMATION).display(s,"Tie!"));Platform.runLater(()->primaryStage.close());send("Bye");}
                        if (s.equals("Bye")){socket.close();socket.close();this.stop();}
                        if (s.equals("Lost")){Platform.runLater(()->new Message(Alert.AlertType.INFORMATION).display("Error","Your opponent lost connection."));}
                    }}
                }
                catch (Exception e) {
                    System.out.println("Lost server");
                    {Platform.runLater(()->new Message(Alert.AlertType.INFORMATION).display("Error","Your server lost connection."));}
                    }
                }
            }
        }

    public void send(String s){
        try {
            os=socket.getOutputStream();
            BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(os));
            writer.write(s+"\n");
            System.out.println("I send sever: "+s);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int[][] getChessBoard() {
        return chessBoard;
    }
    public void setChessBoard(int x,int y,int turn){
        chessBoard[x][y]=turn;
    }

    public int getTurn() {
        return turn;
    }

    public int getNowTurn() {
        return nowTurn;
    }
    //connect
    //GUI
  //play
  //lose or win or tie
    public static void main(String[] args) {
        launch(args);
    }
}
