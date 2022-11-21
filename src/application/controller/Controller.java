package application.controller;

import application.Message;
import application.Player;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    Player player;
    private static final int PLAY_1 = 1;
    private static final int PLAY_2 = 2;
    private static final int EMPTY = 0;
    private static final int BOUND = 90;
    private static final int OFFSET = 15;
    @FXML
    private Pane base_square;
    @FXML
    private Rectangle game_panel;
    private static boolean TURN = false;

    private static int[][] chessBoard = new int[3][3];
    private static final boolean[][] flag = new boolean[3][3];

    public Controller() {}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void init(Player p) {
        this.player= p;
        drawChess();
        if(player.getTurn()==-1){game_panel.setOnMouseClicked(e-> new Message(Alert.AlertType.INFORMATION).display("Invalid operation","Please wait for another player!"));}
        else if (player.getTurn() == player.getNowTurn()) {
            game_panel.setOnMouseClicked(event -> {
                int x = (int) (event.getX() / BOUND);
                int y = (int) (event.getY() / BOUND);
                if (refreshBoard(x, y)) {
                    TURN = !TURN;
                }});
            if(checkGameEnding()){
                player.send("End");
                player.send(getGameEnder()+"win");
            }
        }
        else game_panel.setOnMouseClicked(e-> new Message(Alert.AlertType.INFORMATION).display("Invalid operation","It is NOT your turn! Please wait."));
    }

    public boolean refreshBoard (int x, int y) {
        if (chessBoard[x][y] == EMPTY) {
            chessBoard[x][y] = TURN ? PLAY_1 : PLAY_2;
            player.setChessBoard(x,y, player.getNowTurn());
            drawChess();
            player.send(x+","+y);
            System.out.println("I am playing "+x+","+y);
            return true;
        }
        return false;
    }

    private void drawChess () {
        System.out.println("drawChess");
        for (int i = 0; i < chessBoard.length; i++) {
            for (int j = 0; j < chessBoard[0].length; j++) {
                if (flag[i][j]) {
//                     This square has been drawing, ignore.
                    continue;
                }
                chessBoard[i][j]=player.getChessBoard()[i][j];
                switch (chessBoard[i][j]) {
                    case PLAY_1:
                        drawCircle(i, j);
                        break;
                    case PLAY_2:
                        drawLine(i, j);
                        break;
                    case EMPTY:
                        // do nothing
                        break;
                    default:
                        System.err.println("Invalid value!");
                }
            }
        }
    }

    private void drawCircle (int i, int j) {
        Circle circle = new Circle();
        base_square.getChildren().add(circle);
        circle.setCenterX(i * BOUND + BOUND / 2.0 + OFFSET);
        circle.setCenterY(j * BOUND + BOUND / 2.0 + OFFSET);
        circle.setRadius(BOUND / 2.0 - OFFSET / 2.0);
        circle.setStroke(Color.RED);
        circle.setFill(Color.TRANSPARENT);
        flag[i][j] = true;
    }

    private void drawLine (int i, int j) {
        Line line_a = new Line();
        Line line_b = new Line();
        base_square.getChildren().add(line_a);
        base_square.getChildren().add(line_b);
        line_a.setStartX(i * BOUND + OFFSET * 1.5);
        line_a.setStartY(j * BOUND + OFFSET * 1.5);
        line_a.setEndX((i + 1) * BOUND + OFFSET * 0.5);
        line_a.setEndY((j + 1) * BOUND + OFFSET * 0.5);
        line_a.setStroke(Color.BLUE);

        line_b.setStartX((i + 1) * BOUND + OFFSET * 0.5);
        line_b.setStartY(j * BOUND + OFFSET * 1.5);
        line_b.setEndX(i * BOUND + OFFSET * 1.5);
        line_b.setEndY((j + 1) * BOUND + OFFSET * 0.5);
        line_b.setStroke(Color.BLUE);
        flag[i][j] = true;
    }


    public boolean player1Win(){
        for (int i = 0; i < 3; i++) {
            if(chessBoard[i][0]==1&&chessBoard[i][1]==1&&chessBoard[i][2]==1) return true;
            if(chessBoard[0][i]==1&&chessBoard[1][i]==1&&chessBoard[2][i]==1) return true;
        }
        if(chessBoard[1][1]==1&&chessBoard[2][2]==1&&chessBoard[0][0]==1) return true;
        if(chessBoard[0][2]==1&&chessBoard[1][1]==1&&chessBoard[2][0]==1) return true;
        return false;
    }
    public boolean player2Win(){
        for (int i = 0; i < 3; i++) {
            if(chessBoard[i][0]==2&&chessBoard[i][1]==2&&chessBoard[i][2]==2) return true;
            if(chessBoard[0][i]==1&&chessBoard[1][i]==1&&chessBoard[2][i]==1) return true;}
        if(chessBoard[1][1]==2&&chessBoard[2][2]==2&&chessBoard[0][0]==2) return true;
        if(chessBoard[0][2]==2&&chessBoard[1][1]==2&&chessBoard[2][0]==2) return true;
        return false;
    }
    public int getGameEnder(){
        if(player1Win()){return 1;}
        if (player2Win()){return 2;}
        return 0;
    }
    public boolean checkGameEnding(){
        if(player1Win()){return true;}
        if (player2Win()){return true;}
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!flag[i][j]) return false;
            }
        }
        return true;
    }

}
