package application;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Queue;

public class Server  {
  ServerSocket serverSocket;
  Socket socket;
  Queue<Socket> waitingServers=new LinkedList<>();
  public Server(){
    try {
      serverSocket=new ServerSocket(7503);
      System.out.println("服务端启动");
      while (true){
        socket=serverSocket.accept();
        System.out.println("客户端连接");
        waitingServers.offer(socket);//waiting queue
        match(socket);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void match(Socket socket) throws IOException {
    if(waitingServers.size()==1){
      send(socket,"Wait");
    }
    if (waitingServers.size()==2){
      Socket opponent=waitingServers.poll();
      waitingServers.poll();
      send(opponent,"Match");
      send(socket,"Match");
      send(opponent,"1");
      send(socket,"2");
      ServerThread serverThread=new ServerThread(serverSocket,opponent,socket);
      serverThread.start();
    }
  }

  public void send(Socket socket,String s){
    try {
      OutputStream os=socket.getOutputStream();
      BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(os));
      writer.write(s+"\n");
      System.out.println("服务器告诉玩家"+socket.toString()+s);
      writer.flush();
    } catch (IOException e) {
      System.out.println("A player lost connection");
    }
  }

  class ServerThread extends Thread{
    ServerSocket server;
    Socket player1;
    Socket player2;
    BufferedReader reader1;
    BufferedReader reader2;
    String message1;
    String message2;
    public ServerThread(ServerSocket server,Socket p1,Socket p2) throws IOException {
      this.server=server;
      player1=p1;
      player2=p2;
    }

    @Override
    public void run() {
      super.run();
      try {
        reader1 = new BufferedReader(new InputStreamReader(player1.getInputStream()));
        reader2 = new BufferedReader(new InputStreamReader(player2.getInputStream()));
        while (true){
          if((message1=reader1.readLine())!=null){
            System.out.println("来自玩家1的信息： "+message1);
            if (message1.equals("0,0")){send(player2,"0,0");send(player1,"Turn");send(player2,"Turn");}
            if (message1.equals("0,1")){send(player2,"0,1");send(player1,"Turn");send(player2,"Turn");}
            if (message1.equals("0,2")){send(player2,"0,2");send(player1,"Turn");send(player2,"Turn");}
            if (message1.equals("1,0")){send(player2,"1,0");send(player1,"Turn");send(player2,"Turn");}
            if (message1.equals("1,1")){send(player2,"1,1");send(player1,"Turn");send(player2,"Turn");}
            if (message1.equals("1,2")){send(player2,"1,2");send(player1,"Turn");send(player2,"Turn");}
            if (message1.equals("2,0")){send(player2,"2,0");send(player1,"Turn");send(player2,"Turn");}
            if (message1.equals("2,1")){send(player2,"2,1");send(player1,"Turn");send(player2,"Turn");}
            if (message1.equals("2,2")){send(player2,"2,2");send(player1,"Turn");send(player2,"Turn");}
            if (message1.equals("1win")) {send(player1,"Win");send(player2,"Lose");}
            if (message1.equals("2win")) {send(player2,"Win");send(player1,"Lose");}
            if (message1.equals("0win")){send(player1,"Tie");send(player2,"Tie");}
            if (message1.equals("Bye")){send(player1,"Bye");player1.shutdownInput();player1.close();send(player2,"Bye");player2.shutdownInput();player2.close();}}
          if ((message2=reader2.readLine())!=null){
            System.out.println("来自玩家2的信息： "+message2);
            if (message2.equals("0,0")){send(player1,"0,0");send(player1,"Turn");send(player2,"Turn");}
            if (message2.equals("0,1")){send(player1,"0,1");send(player1,"Turn");send(player2,"Turn");}
            if (message2.equals("0,2")){send(player1,"0,2");send(player1,"Turn");send(player2,"Turn");}
            if (message2.equals("1,0")){send(player1,"1,0");send(player1,"Turn");send(player2,"Turn");}
            if (message2.equals("1,1")){send(player1,"1,1");send(player1,"Turn");send(player2,"Turn");}
            if (message2.equals("1,2")){send(player1,"1,2");send(player1,"Turn");send(player2,"Turn");}
            if (message2.equals("2,0")){send(player1,"2,0");send(player1,"Turn");send(player2,"Turn");}
            if (message2.equals("2,1")){send(player1,"2,1");send(player1,"Turn");send(player2,"Turn");}
            if (message2.equals("2,2")){send(player1,"2,2");send(player1,"Turn");send(player2,"Turn");}
            if (message2.equals("1win")){send(player2,"Lose");send(player1,"Win");}
            if (message2.equals("2win")){send(player1,"Lose");send(player2,"Win");}
            if (message2.equals("0win")){send(player1,"Tie");send(player2,"Tie");}
            if (message2.equals("Bye")){
              send(player1,"Bye");player1.close();
              send(player2,"Bye");player2.close();}}}}
      catch (Exception e) {
        System.out.println("SocketException");

        try {
          if(player1.isConnected()){send(player1,"Lost");}
        } catch (Exception ex) {
          System.out.println("Player1 lost connection.");
        }
        try {
          if(player2.isConnected()){send(player2,"Lost");}
        } catch (Exception ex) {
          System.out.println("Player2 lost connection.");
        }
        this.stop();}

    }
  }
  public static void main(String[] args) {
    Server server=new Server();
  }
}
