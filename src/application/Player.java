package application;

import application.controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class Player extends  Application{
    Socket socket;
    int number;
    boolean turn;
    boolean nowTurn;
    Controller controller;
    boolean[][] flag = new boolean[3][3];

    public Player(){
        try {
            socket=new Socket("localhost",7503);
            System.out.println("客户端建立");
            ReadServerThread read=new ReadServerThread();
            read.start();
//            controller=new Controller(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("mainUI.fxml"));
            Pane root = fxmlLoader.load();
            Controller controller = fxmlLoader.getController();
            controller.init(this);
            primaryStage.setTitle("Tic Tac Toe");
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ReadServerThread extends Thread{
        InputStream is;
        @Override
        public void run() {
            super.run();
            {
                while (true) {
                    try {
                        is = socket.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                        String s=reader.readLine();
                        if(s.equals("Match")) new Message(s,"Match successfully!");
                        if(s.equals("Wait")) new Message(s,"Please wait for another player!");
                        if (s.equals("1")) {number=1;turn=true;nowTurn=true;}
                        if (s.equals("2")) {number=2;turn=false;nowTurn=true;}
                        if (s.equals("turn")) nowTurn=!nowTurn;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public void send(String s){
        try {
            OutputStream os=socket.getOutputStream();
            BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(os));
            writer.write(s+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Socket getSocket() {
        return socket;
    }

    public boolean[][] getFlag() {
        return flag;
    }

    public int getNumber() {
        return number;
    }

    public boolean getTurn() {
        return turn;
    }

    public boolean getNowTurn() {
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
