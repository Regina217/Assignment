package application;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class Server  {
    ServerSocket serverSocket;
    Socket socket;
    Queue<Socket> playingSevers;
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
    public void match(Socket socket){
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
       } catch (IOException e) {
           e.printStackTrace();
       }
    }
    class ServerThread extends Thread{
        ServerSocket server;
        Socket player1;
        Socket player2;
        public ServerThread(ServerSocket server,Socket p1,Socket p2){
            this.server=server;
            player1=p1;
            player2=p2;
        }
        @Override
        public void run() {
            super.run();
                try {
                    while (true) {
                        BufferedReader reader1 = new BufferedReader(new InputStreamReader(player1.getInputStream()));
                        String message1=reader1.readLine();
                        if (message1.equals("0,0")){send(player2,"0,0");send(player1,"turn");send(player2,"turn");}
                        if (message1.equals("0,1")){send(player2,"0,1");send(player1,"turn");send(player2,"turn");}
                        if (message1.equals("0,2")){send(player2,"0,2");send(player1,"turn");send(player2,"turn");}
                        if (message1.equals("1,0")){send(player2,"1,0");send(player1,"turn");send(player2,"turn");}
                        if (message1.equals("1,1")){send(player2,"1,1");send(player1,"turn");send(player2,"turn");}
                        if (message1.equals("1,2")){send(player2,"1,2");send(player1,"turn");send(player2,"turn");}
                        if (message1.equals("2,0")){send(player2,"2,0");send(player1,"turn");send(player2,"turn");}
                        if (message1.equals("2,1")){send(player2,"2,1");send(player1,"turn");send(player2,"turn");}
                        if (message1.equals("2,2")){send(player2,"2,2");send(player1,"turn");send(player2,"turn");}
                        BufferedReader reader2 = new BufferedReader(new InputStreamReader(player2.getInputStream()));
                        String message2=reader2.readLine();
                        if (message2.equals("0,0")){send(player1,"0,0");send(player1,"turn");send(player2,"turn");}
                        if (message2.equals("0,1")){send(player1,"0,1");send(player1,"turn");send(player2,"turn");}
                        if (message2.equals("0,2")){send(player1,"0,2");send(player1,"turn");send(player2,"turn");}
                        if (message2.equals("1,0")){send(player1,"1,0");send(player1,"turn");send(player2,"turn");}
                        if (message2.equals("1,1")){send(player1,"1,1");send(player1,"turn");send(player2,"turn");}
                        if (message2.equals("1,2")){send(player1,"1,2");send(player1,"turn");send(player2,"turn");}
                        if (message2.equals("2,0")){send(player1,"2,0");send(player1,"turn");send(player2,"turn");}
                        if (message2.equals("2,1")){send(player1,"2,1");send(player1,"turn");send(player2,"turn");}
                        if (message2.equals("2,2")){send(player1,"2,2");send(player1,"turn");send(player2,"turn");}
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    try {
                        serverSocket.close();
                        System.out.println("服务端关闭");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
   // state:
    // 1 Please wait your opponent... 2 successfully match!

    //wait for new players
    //match
    //no match then inform

    //track and inform lose or win

//  计数器记录奇偶来通知等待和匹配or队列
//  不停地访问输赢状态
public static void main(String[] args) {
  Server server=new Server();
}
}
