import com.sun.source.tree.Tree;
import javafx.geometry.HPos;

import java.awt.*;
import java.awt.desktop.AboutEvent;
import java.awt.desktop.AboutHandler;
import java.io.Console;
import java.io.IOException;
import java.util.Scanner;

public class Game implements Runnable {

    //Tree tree;
    //Thread game_thread;
    Board board;
    PlayingBoard playingBoard;
    boolean game_running;
    char promotion;


    public static void main(String[] args) throws InterruptedException, IOException {

        System.out.println("Hello World");
        Game game = new Game();

    }


    public Game(){
        game_running = true;
        colour = 1;

        Menu mainmenu = new Menu("Main Menu", this, new String[]{"classical","seirawan"/*,"testing","mouse listener","plain board"*/});
        mainmenu.listen2();
    }



    void testing() throws InterruptedException, IOException {
        int size_x = 8;
        int size_y = 8;
        int[] size = new int[]{size_x,size_y};

        Piece[][] pieces = new Piece[size_x][size_y];
        for(int x = 0 ; x < size[0] ; x++){
            for(int y = 0 ; y < size[1] ; y++){
                pieces[x][y] = new Piece(' ',-1);
            }
        }



        pieces[0][0] = new Piece('q',1);

        pieces[5][0] = new Piece('k',0);

        pieces[5][5] = new Piece("pawn",'p',1,new Move[]{new Move('1',1,false,false),new Move('v',1,true,true)});
        pieces[4][4] = new Piece("pawn",'p',0,new Move[]{new Move('1',0,false,false),new Move('v',0,true,true)});


        Position position = new Position(size,pieces);
        board = new Board(size_x,size_y,position,this);

        //PlayingBoard playingBoard;
        playingBoard = new PlayingBoard(8,8,this);

        board.drawBoard();

        board.drawBoard_reach(1);

        board.move("kf2",0);

        board.drawBoard();

        board.drawBoard_reach(5,5);

        board.move("f7",1);

        board.drawBoard_reach(5,6);

        board.move("f8=q",1);

        board.drawBoard();

        board.move("kf3",0);

        board.drawBoard();

        board.move("ke2",0);

        board.drawBoard();


        board.move("e4",0);
        board.move("e3",0);

        board.move("kd2",0);

        board.move("e2",0);
        board.move("e1=r",0);

        board.drawBoard();

        board.move("qf2",1);
        board.drawBoard();

        board.position.history.print_command_colour_history();

        board.drawBoardHistory();


        //playingBoard.exit();
        playingBoard.update(board.position);

    }

    void classical() throws InterruptedException {
        int size_x = 8;
        int size_y = 8;
        int[] size = new int[]{size_x,size_y};

        Piece[][] pieces = new Piece[size_x][size_y];
        for(int x = 0 ; x < size[0] ; x++){
            for(int y = 0 ; y < size[1] ; y++){
                pieces[x][y] = new Piece(' ',-1);
            }
        }

        pieces[0][0] = new Piece('r',1);
        pieces[1][0] = new Piece('n',1);
        pieces[2][0] = new Piece('b',1);
        pieces[3][0] = new Piece('q',1);
        pieces[4][0] = new Piece('k',1);
        pieces[5][0] = new Piece('b',1);
        pieces[6][0] = new Piece('n',1);
        pieces[7][0] = new Piece('r',1);
        pieces[0][1] = new Piece('p',1);
        pieces[1][1] = new Piece('p',1);
        pieces[2][1] = new Piece('p',1);
        pieces[3][1] = new Piece('p',1);
        pieces[4][1] = new Piece('p',1);
        pieces[5][1] = new Piece('p',1);
        pieces[6][1] = new Piece('p',1);
        pieces[7][1] = new Piece('p',1);

        pieces[0][7] = new Piece('r',0);
        pieces[1][7] = new Piece('n',0);
        pieces[2][7] = new Piece('b',0);
        pieces[3][7] = new Piece('q',0);
        pieces[4][7] = new Piece('k',0);
        pieces[5][7] = new Piece('b',0);
        pieces[6][7] = new Piece('n',0);
        pieces[7][7] = new Piece('r',0);
        pieces[0][6] = new Piece('p',0);
        pieces[1][6] = new Piece('p',0);
        pieces[2][6] = new Piece('p',0);
        pieces[3][6] = new Piece('p',0);
        pieces[4][6] = new Piece('p',0);
        pieces[5][6] = new Piece('p',0);
        pieces[6][6] = new Piece('p',0);
        pieces[7][6] = new Piece('p',0);


        /*
        board.move("h3",1);
        board.drawBoard();

        board.drawBoard_reach(1);

        board.move("b6",0);
        board.drawBoard();

        board.move("e4",1);
        board.drawBoard();

        board.drawBoard_reach(3,0);

        board.move("qf3",1);
        board.drawBoard();

        board.move("e5",1);
        board.drawBoard();

        board.move("h6",0);
        board.drawBoard();

        board.move("e6",1);
        board.drawBoard();

        board.move("d6",0);
        board.drawBoard();

        //board.position.history.print_command_colour_history();

        board.move("xf7",1);
        board.drawBoard();

        board.move("a6",0);
        board.drawBoard();

        board.move("b5",0);
        board.drawBoard();

        board.move("kd7",0);

        board.move("xg8=q",1);
        board.drawBoard();

        board.move("rxg8",0);
        board.drawBoard();

        board.move("qf5",1);
        board.drawBoard();

        board.move("ke8",0);
        board.drawBoard();

        board.move("qd3",1);
        board.move("f3",1);
        board.move("f4",1);
        board.move("f5",1);

        board.drawBoard();

        board.move("g5",0);

        board.drawBoard();

        board.drawBoard_allAttackedSquares(5,4);

        //board.move("a6",0);
        //board.drawBoard();
        //board.drawBoard_allAttackedSquares(5,4);

        board.move("xg6",1);
        board.drawBoard();

        board.move("na6",0);
        board.move("nb8",0);

        board.drawBaselineMoved();

         */

        /*
        board.move("be2",1);
        board.move("nf3",1);
        board.drawBoard_reach(4,0);
        board.move("O-O",1);
        board.drawBoard();

         */

        /*
        board.move("b3",1);
        board.move("bb2",1);
        board.move("nc3",1);

        board.drawBoard();

        board.drawBoard_reach(4,0);

        board.move("O-O-O",1);
        board.drawBoard();

        //board.move("a3",1);
        //board.drawBoard();
        //board.drawBoard_allAttackedSquares(5,4);




        board.position.history.print_command_colour_history();

        board.drawBoardHistory();

        //position.history.print_move_history_abbreviation();
         */

         Position position = new Position(size,pieces);
         board = new Board(size_x,size_y,position,this);
         playingBoard = new PlayingBoard(8,8,this);
         playingBoard.update(board.position);

    }

    void seirawan() throws InterruptedException {

        int size_x = 8;
        int size_y = 8;
        int[] size = new int[]{size_x,size_y};

        Piece[][] pieces = new Piece[size_x][size_y];
        for(int x = 0 ; x < size[0] ; x++){
            for(int y = 0 ; y < size[1] ; y++){
                pieces[x][y] = new Piece(' ',-1);
            }
        }

        pieces[0][0] = new Piece('r',1);
        pieces[1][0] = new Piece('n',1);
        pieces[2][0] = new Piece('h',1);
        pieces[3][0] = new Piece('q',1);
        pieces[4][0] = new Piece('k',1);
        pieces[5][0] = new Piece('e',1);
        pieces[6][0] = new Piece('n',1);
        pieces[7][0] = new Piece('r',1);
        pieces[0][1] = new Piece('p',1);
        pieces[1][1] = new Piece('j',1);
        pieces[2][1] = new Piece('p',1);
        pieces[3][1] = new Piece('p',1);
        pieces[4][1] = new Piece('p',1);
        pieces[5][1] = new Piece('p',1);
        pieces[6][1] = new Piece('j',1);
        pieces[7][1] = new Piece('p',1);

        pieces[6][2] = new Piece('p',1);
        pieces[1][2] = new Piece('p',1);
        pieces[0][2] = new Piece('l',1);
        pieces[7][2] = new Piece('l',1);

        pieces[0][7] = new Piece('r',0);
        pieces[1][7] = new Piece('n',0);
        pieces[2][7] = new Piece('e',0);
        pieces[3][7] = new Piece('q',0);
        pieces[4][7] = new Piece('k',0);
        pieces[5][7] = new Piece('h',0);
        pieces[6][7] = new Piece('n',0);
        pieces[7][7] = new Piece('r',0);
        pieces[0][6] = new Piece('p',0);
        pieces[1][6] = new Piece('j',0);
        pieces[2][6] = new Piece('p',0);
        pieces[3][6] = new Piece('p',0);
        pieces[4][6] = new Piece('p',0);
        pieces[5][6] = new Piece('p',0);
        pieces[6][6] = new Piece('j',0);
        pieces[7][6] = new Piece('p',0);

        pieces[6][5] = new Piece('p',0);
        pieces[1][5] = new Piece('p',0);
        pieces[0][5] = new Piece('l',0);
        pieces[7][5] = new Piece('l',0);

        Position position = new Position(size,pieces);
        board = new Board(size_x,size_y,position,this);
        playingBoard = new PlayingBoard(8,8,this);
        playingBoard.update(board.position);


    }

    void plainClassicBoard() throws InterruptedException {

        int size_x = 8;
        int size_y = 8;
        int[] size = new int[]{size_x,size_y};

        Piece[][] pieces = new Piece[size_x][size_y];
        for(int x = 0 ; x < size[0] ; x++){
            for(int y = 0 ; y < size[1] ; y++){
                pieces[x][y] = new Piece(' ',-1);
            }
        }

        pieces[0][0] = new Piece('r',1);
        pieces[1][0] = new Piece('n',1);
        pieces[2][0] = new Piece('b',1);
        pieces[3][0] = new Piece('q',1);
        pieces[4][0] = new Piece('k',1);
        pieces[5][0] = new Piece('b',1);
        pieces[6][0] = new Piece('n',1);
        pieces[7][0] = new Piece('r',1);
        pieces[0][1] = new Piece('p',1);
        pieces[1][1] = new Piece('p',1);
        pieces[2][1] = new Piece('p',1);
        pieces[3][1] = new Piece('p',1);
        pieces[4][1] = new Piece('p',1);
        pieces[5][1] = new Piece('p',1);
        pieces[6][1] = new Piece('p',1);
        pieces[7][1] = new Piece('p',1);

        pieces[0][7] = new Piece('r',0);
        pieces[1][7] = new Piece('n',0);
        pieces[2][7] = new Piece('b',0);
        pieces[3][7] = new Piece('q',0);
        pieces[4][7] = new Piece('k',0);
        pieces[5][7] = new Piece('b',0);
        pieces[6][7] = new Piece('n',0);
        pieces[7][7] = new Piece('r',0);
        pieces[0][6] = new Piece('p',0);
        pieces[1][6] = new Piece('p',0);
        pieces[2][6] = new Piece('p',0);
        pieces[3][6] = new Piece('p',0);
        pieces[4][6] = new Piece('p',0);
        pieces[5][6] = new Piece('p',0);
        pieces[6][6] = new Piece('p',0);
        pieces[7][6] = new Piece('p',0);

        Position position = new Position(size,pieces);
        board = new Board(size_x,size_y,position,this);
        playingBoard = new PlayingBoard(8,8,this);
        playingBoard.update(board.position);

        //Thread t_board = new Thread(playingBoard);
        //t_board.start();


        /*
        Scanner in = new Scanner(System.in);
        int colour = 1;
        boolean legal;
        while(true){
            String cmd = in.nextLine();
            if(cmd == "exit"){
                playingBoard.exit();
                System.exit(1);
            }
            if(cmd == "update"){
                playingBoard.update(board.position);
                System.out.println("Board updated");
                continue;
            }
            System.out.println( (colour==1?"White":"Black") + " to move. Awaiting input.");
            legal = board.move(cmd,colour);
            playingBoard.update(board.position);
            System.out.println("legal: " + legal);
            if(legal) colour = colour == 1 ? 0 : 1;


        }

         */








    }


    /*
    void setPiece(String str){//eg. a1r a1 rook
        char[] al = new char[]{'a','b','c','d','e','f','g','h'};
    }
     */


    boolean legal = true;
    int colour = 1;
    void DragReport(int x_old, int y_old, int x_new, int y_new) throws InterruptedException {
        if(!game_running) return;
        if(colour != board.position.position[x_old][y_old].colour){
            System.out.println("It's not your move. It's " + (colour==0?"Black":"White") + "'s move.");
            return;
        }
        char[] al = new char[]{'a','b','c','d','e','f','g','h'};

        String sq_old, sq_new;
        sq_old = al[x_old] + "" + (y_old+1);
        sq_new = al[x_new] + "" + (y_new+1);

        System.out.println(  "Dragged from " + sq_old + " to " + sq_new  );

        String cmd = board.position.convert_xyxy_to_cmd(x_old,y_old,x_new,y_new);
        System.out.println( (colour==1?"White":"Black") + " plays " + cmd);
        board.position.history.addCommandandColour(cmd,colour);

        //Menu promotion_menu;
        promotion = ' ';
        if(board.position.position[x_old][y_old].abbreviation == 'p' && ( (board.position.position[x_old][y_old].colour == 1 && y_new == 7) || (board.position.position[x_old][y_old].colour == 0 && y_new == 0) ) ){

            System.out.println("Please enter promotion abbreviation.");
            Scanner sc = new Scanner(System.in);
            promotion = sc.nextLine().toCharArray()[0];
            System.out.println("testing: prom: " + promotion);


            /*
            promotion = 'z';
            while(promotion == 'z'){

                promotion_menu = new Menu("Promotion", this, new String[]{"a","b","c","d","e","f"});

                new Thread(promotion_menu).start();

                promotion_menu.listen2();
                Thread.sleep(1000);
                promotion_menu.closeWindow();

            }
             */


            /*
            promotion_menu = new Menu("Promotion", this, new String[]{"a","b","c","d","e","f"});
            new Thread(promotion_menu).start();
            Scanner sc = new Scanner(promotion_menu);
            promotion = sc.nextLine().toCharArray()[0];
            System.out.println("testing: prom: " + promotion);
             */


            /*
            promotion_menu = new Menu("Promotion", this, new String[]{"a","b","c","d","e","f"});
            new Thread(promotion_menu).start();
            this.game_thread.sleep(10000);
             */


        }


        //if(promotion == 'z') promotion = ' ';

        legal = board.move(x_old,y_old,x_new,y_new,promotion);
        playingBoard.update(board.position);
        //System.out.println("legal: " + legal);
        if(!game_running) return;
        if(legal) colour = colour == 1 ? 0 : 1;
        System.out.println( (colour==1?"White":"Black") + " to move. Awaiting move.");


    }

    void MenuReport(String title, String label) throws InterruptedException {
        if(title == "Main Menu"){

            switch (label){

                case "classical":
                    classical();
                    break;

                case "seirawan":
                    seirawan();
                    break;

            }

        }
        else if(title == "Promotion"){

            switch (label){
                case "a":
                    this.promotion = 'q';
                    System.out.println(label);
                    break;
            }

        }

    }

    @Override
    public void run() {

    }
}
