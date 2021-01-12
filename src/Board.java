import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class Board {

    //Piece[][] board;
    int[] size;
    //History history;
    //boolean[][] baseline_moved;
    Position position;
    PlayingBoard playingBoard;
    Game game;


    public Board(int size_x, int size_y) throws InterruptedException {
        size = new int[]{size_x,size_y};
        position = new Position(size);
        //playingBoard = new PlayingBoard(8,8,position);
    }

    public Board(int size_x, int size_y, Position position, Game game) throws InterruptedException {
        this.game = game;
        size = new int[]{size_x,size_y};
        this.position = position;
        //playingBoard = new PlayingBoard(8,8,position);
    }

    public Board clone(){
        try {
            return new Board(size[0],size[1],position.clone(),this.game);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    /*
    boolean[][] attacks(int pos_x, int pos_y){ //outdated

        boolean[][] result = new boolean[size[0]][size[1]];
        if(board[pos_x][pos_y].abbreviation == ' ') return result;

        Piece attacker = board[pos_x][pos_y];
        Move[] moves = attacker.getMoves(pos_x,pos_y);
        int x;
        int y;
        ArrayList movelist;
        int[] pattern;
        for(Move move : moves){
            if(!move.attacking) continue;
            movelist = move.moves;

            if(!move.blockedOnWay && !move.continued){ //eg cat jump and knight jump and king
                for(int pattern_num = 0 ; pattern_num < movelist.size() ; pattern_num++){
                    pattern = (int[])movelist.get(pattern_num);
                    x = pos_x + pattern[0];
                    y = pos_y + pattern[1];
                    if(!coordinatesOnBoard(x,y)) continue;
                    result[x][y] = true;
                }
                continue;
            }
            else if(move.blockedOnWay && !move.continued){ //eg leap
                boolean blocked;
                for(int pattern_num = 0 ; pattern_num < movelist.size() ; pattern_num++){
                    pattern = (int[])movelist.get(pattern_num);
                    blocked = false;
                    if( pattern[0] != 0 && pattern[1] != 0 && Math.abs(pattern[0]) != Math.abs(pattern[1]) ){
                        System.out.println("Unexpected(1)");
                        System.exit(1);
                    }
                    for(int step = 1 ; step < Math.max(pattern[0],pattern[1]) ; step++){
                        x = pos_x + step*(pattern[0] / Math.max(pattern[0],pattern[1]) );
                        y = pos_y + step*(pattern[1] / Math.max(pattern[0],pattern[1]) );
                        if(board[x][y].abbreviation != ' ') blocked = true;
                    }
                    x = pos_x + pattern[0];
                    y = pos_y + pattern[1];
                    if(!coordinatesOnBoard(x,y)) continue;
                    result[x][y] = !blocked;
                }
                continue;
            }

            //check for unexpected inputs
            for(int pattern_num = 0 ; pattern_num < movelist.size() ; pattern_num++){
                pattern = (int[])movelist.get(pattern_num);
                if( pattern[0] != 0 && pattern[1] != 0 && Math.abs(pattern[0]) != Math.abs(pattern[1]) ){
                    System.out.println("Unexpected(2)");
                    System.exit(1);
                }
            }

            //from here on blockedOnWay and continued
            //eg queen, bishop, rook


            boolean blocked;
            for(int pattern_num = 0 ; pattern_num < movelist.size() ; pattern_num++){
                blocked = false;
                pattern = (int[])movelist.get(pattern_num);

                x = pos_x + pattern[0];
                y = pos_y + pattern[1];
                while(!blocked && coordinatesOnBoard(x,y)){

                    result[x][y] = true;

                    if(board[x][y].abbreviation != ' ') blocked = true;
                    x += pattern[0];
                    y += pattern[1];
                }

            }

        }

        return result;
    }
     */

    /*
    boolean[][] attacks(int colour){
        boolean[][] temp;
        boolean[][] result = new boolean[size[0]][size[1]];
        for(int x = 0 ; x < size[0] ; x++){
            for(int y = 0 ; y < size[1] ; y++){
                if(board[x][y].colour == colour){
                    temp = attacks(x,y);
                    for(int xx = 0 ; xx < size[0] ; xx++){
                        for(int yy = 0 ; yy < size[1] ; yy++){
                            result[xx][yy] = result[xx][yy] | temp[xx][yy];
                        }
                    }
                }

            }
        }
        return result;
    }
     */


    boolean[][] reaches(int pos_x, int pos_y){
        Object[][] analysisALL = position.analysisALL;
        Object[] analysis = (Object[]) analysisALL[pos_x][pos_y];
        boolean[][] reach = new boolean[size[0]][size[1]];
        boolean[][] movesto = (boolean[][]) analysis[0];
        boolean[][] defends = (boolean[][]) analysis[2];
        boolean[][] attacks = (boolean[][]) analysis[4];
        for(int x = 0 ; x < size[0] ; x++){
            for(int y = 0 ; y < size[1] ; y++){
                reach[x][y] = attacks[x][y] | defends[x][y] | movesto[x][y];
            }
        }
        return reach;
    }

    boolean[][] reaches(int colour){
        Object[][] analysisALL = position.analysisALL;
        boolean[][] result = new boolean[size[0]][size[1]];

        for(int x = 0 ; x < size[0] ; x++){
            for(int y = 0 ; y < size[1] ; y++){
                if(position.position[x][y].colour == colour){
                    Object[] analysis = (Object[]) analysisALL[x][y];
                    boolean[][] movesto = (boolean[][]) analysis[0];
                    boolean[][] defending = (boolean[][]) analysis[2];
                    boolean[][] attacking = (boolean[][]) analysis[4];
                    for(int xx = 0 ; xx < size[0] ; xx++){
                        for(int yy = 0 ; yy < size[1] ; yy++){
                            result[xx][yy] = result[xx][yy] | movesto[xx][yy] | defending[xx][yy] | attacking[xx][yy] ;
                        }
                    }
                }
            }
        }
        return result;
    }

    boolean[][] attacks(int colour){
        Object[][] analysisALL = position.analysisALL;
        boolean[][] result = new boolean[size[0]][size[1]];

        for(int x = 0 ; x < size[0] ; x++){
            for(int y = 0 ; y < size[1] ; y++){
                if(position.position[x][y].colour == colour){
                    Object[] analysis = (Object[]) analysisALL[x][y];
                    boolean[][] attacking = (boolean[][]) analysis[4];
                    for(int xx = 0 ; xx < size[0] ; xx++){
                        for(int yy = 0 ; yy < size[1] ; yy++){
                            result[xx][yy] = result[xx][yy] | attacking[xx][yy];
                        }
                    }
                }
            }
        }
        return result;
    }

    boolean[][] attacks(int pos_x, int pos_y){
        Object[][] analysisALL = position.analysisALL;
        boolean[][] result = new boolean[size[0]][size[1]];

        Object[] analysis = (Object[]) analysisALL[pos_x][pos_y];
        boolean[][] attacking = (boolean[][]) analysis[4];
        for(int xx = 0 ; xx < size[0] ; xx++){
            for(int yy = 0 ; yy < size[1] ; yy++){
                result[xx][yy] = result[xx][yy] | attacking[xx][yy];
            }
        }

        return result;
    }

    /*
    boolean isAttacked(int pos_x, int pos_y, Object[][] analysisALL){
        Piece[][] board = position.position;

        Object[] cur_analysis;
        boolean[][] cur_attacking;
        int attacked_colour = board[pos_x][pos_y].colour;
        for(int x = 0 ; x < size[0] ; x++){
            for(int y = 0 ; y < size[1] ; y++){
                if(board[x][y].colour == -1) continue;
                if(board[x][y].colour != attacked_colour){
                    cur_analysis = (Object[]) analysisALL[x][y];
                    cur_attacking = (boolean[][]) cur_analysis[4];
                    if(cur_attacking[pos_x][pos_y]) return true;
                }
            }
        }
        return false;
    }
     */

    boolean isDefended(int pos_x, int pos_y, Object[][] analysisALL){
        Piece[][] board = position.position;

        Object[] cur_analysis;
        boolean[][] cur_defender;
        int defended_colour = board[pos_x][pos_y].colour;
        for(int x = 0 ; x < size[0] ; x++){
            for(int y = 0 ; y < size[1] ; y++){
                if(board[x][y].colour == defended_colour){
                    cur_analysis = (Object[]) analysisALL[x][y];
                    cur_defender = (boolean[][]) cur_analysis[3];
                    if(cur_defender[pos_x][pos_y]) return true;
                }
            }
        }
        return false;
    }

/*
    boolean incheck(int colour){
        Object[][] analysisALL = position.analysisALL;
        Piece[][] board = position.position;

        for(int x = 0 ; x < size[0] ; x++){
            for(int y = 0 ; y < size[1] ; y++){
                if( board[x][y].colour == colour && board[x][y].abbreviation == 'k' ){
                    return isAttacked(x,y,analysisALL);
                }
            }
        }
        return false;
    }

 */

    boolean inMate(int colour){
        Object[][] analysisAll = position.analysisALL;

        Piece[][] board = position.position;

        PrintStream silence = System.out;
        System.setOut(new PrintStream(OutputStream.nullOutputStream()));
        Board testboard;
        for(int x = 0 ; x < size[0] ; x++){
            for(int y = 0 ; y < size[1] ; y++){
                Object[] analysis = (Object[]) analysisAll[x][y];
                boolean[][] movesto = (boolean[][]) analysis[0];
                boolean[][] attacks = (boolean[][]) analysis[4];
                if(board[x][y].colour == colour){
                    for(int xx = 0 ; xx < size[0] ; xx++){
                        for(int yy = 0 ; yy < size[1] ; yy++){

                            testboard = clone();

                            if(attacks[xx][yy] | movesto[xx][yy]) testboard.move(x,y,xx,yy);
                            if(!testboard.position.incheck(colour)){
                                System.setOut(silence);
                                return false;
                            }
                        }
                    }
                }
            }
        }
        System.setOut(silence);
        return true;
    }

    boolean move(int pos_x_old, int pos_y_old, int pos_x_new, int pos_y_new, char promotion){

        Object[][] analysisALL = position.history.getLastPosition().analysisALL;
        Object[] analysis = (Object[]) analysisALL[pos_x_old][pos_y_old];
        boolean[][] reaches = reaches(pos_x_old,pos_y_old);
        boolean[][] movesto = (boolean[][]) analysis[0];
        Move[][] movesto_moves = (Move[][]) analysis[1];
        boolean[][] defends = (boolean[][]) analysis[2];
        boolean[][] attacks = (boolean[][]) analysis[4];
        Move[][] attacks_moves = (Move[][]) analysis[5];

        //System.out.println("attacks: " + attacks[pos_x_new][pos_y_new] + " - movesto: " + movesto[pos_x_new][pos_y_new]);
        if(movesto[pos_x_new][pos_y_new] | attacks[pos_x_new][pos_y_new]){

            if(movesto[pos_x_new][pos_y_new]){
                position.history.addMove(pos_x_old,pos_y_old,movesto_moves[pos_x_new][pos_y_new]);
            }
            else {
                position.history.addMove(pos_x_old,pos_y_old,attacks_moves[pos_x_new][pos_y_new]);
            }

            if(position.position[pos_x_old][pos_y_old].abbreviation == 'k' && Math.abs(pos_x_new - pos_x_old) > 1 ){//castles
                if(pos_x_new > pos_x_old){//kingside
                    position.setSquare(pos_x_new,pos_y_new,position.position[pos_x_old][pos_y_old]);
                    //position.setSquare(pos_x_old,pos_y_old,new Piece(' ',-1));
                    position.setSquare(pos_x_old+1,pos_y_old,position.position[pos_x_old+3][pos_y_old]);
                    position.setSquare(pos_x_old+3,pos_y_old,new Piece(' ',-1));
                }
                else {//queenside
                    position.setSquare(pos_x_new,pos_y_new,position.position[pos_x_old][pos_y_old]);
                    //position.setSquare(pos_x_old,pos_y_old,new Piece(' ',-1));
                    position.setSquare(pos_x_old-1,pos_y_old,position.position[pos_x_old-4][pos_y_old]);
                    position.setSquare(pos_x_old-4,pos_y_old,new Piece(' ',-1));
                }
            }

            if(position.position[pos_x_old][pos_y_old].abbreviation == 'p'){
                if( promotion != ' ' && (pos_y_new == 0 || pos_y_new == 7)){ //promotion
                    position.setSquare(pos_x_new,pos_y_new,new Piece(promotion,position.position[pos_x_old][pos_y_old].colour));
                    position.setSquare(pos_x_old,pos_y_old,new Piece(' ',-1));
                }
                else if(promotion == ' '){

                    int colour_attacker = position.position[pos_x_old][pos_y_old].colour;

                    if(attacks[pos_x_new][pos_y_new] && position.position[pos_x_new][pos_y_new].abbreviation == ' '){ //en passant
                        if(position.position[pos_x_new][pos_y_old].abbreviation != 'p'){
                            System.out.println("This should be impossible.");
                            System.exit(1);
                        }
                        position.setSquare(pos_x_new,pos_y_old,new Piece(' ',-1));
                    }
                    if(attacks[pos_x_new][pos_y_new]){
                        position.setSquare(pos_x_new,pos_y_new,position.position[pos_x_old][pos_y_old]);
                        position.setSquare(pos_x_old,pos_y_old,new Piece(' ',-1));
                    }

                    position.setSquare(pos_x_new,pos_y_new, new Piece("pawn",'p',colour_attacker,new Move[]{new Move('1',colour_attacker,false,false), new Move('v',colour_attacker,true,true)} ));
                    position.setSquare(pos_x_old,pos_y_old,new Piece(' ',-1));
                }
                else {
                    System.out.println("Illegal move - Promote with '=' ");
                    return false;
                }
            }
            else{
                position.setSquare(pos_x_new,pos_y_new,position.position[pos_x_old][pos_y_old]);
                position.setSquare(pos_x_old,pos_y_old,new Piece(' ',-1));
            }


        }
        else{
            System.out.println("Not a legal move...");
            return false;
        }

        position.analyse();
        position.savePosition();
        position = position.clone();

        if(position.incheck(position.position[pos_x_new][pos_y_new].colour)){
            System.out.println("You are in check - Illegal move");

            position.history.removeLastPosition();
            position.history.removeLastCommandandColour();
            position.history.removeLastMove();

            position = position.history.getLastPosition().clone();

            //position.position = position.history.position_history.get(position.history.position_history.size()-1).position;
            //position = position.history.getLastPosition();
            //position.analyse();
            //position.savePosition();
            return false;
        }

        //baseline moved
        if((pos_y_old == 0 || pos_y_old == 7)){
            switch (pos_y_old) {
                case 0:
                    position.baseline_moved(1,pos_x_old);
                    break;

                case 7:
                    position.baseline_moved(0,pos_x_old);
                    break;
            }
        }
        if((pos_y_new == 0 || pos_y_new == 7)){
            switch (pos_y_new) {
                case 0:
                    position.baseline_moved(1,pos_x_new);
                    break;

                case 7:
                    position.baseline_moved(0,pos_x_new);
                    break;
            }
        }


        if(position.incheck(position.position[pos_x_new][pos_y_new].colour==0?1:0)){
            if(inMate(position.position[pos_x_new][pos_y_new].colour==0?1:0)){
                System.out.println("Checkmate! Game over.");
                game.game_running = false;
                //System.exit(1);
            }
            else {
                System.out.println("Check.");
            }
        }

        return true;

    }

    boolean move(String command, int colour){

        Object[][] analysisALL = position.analysisALL;

        Piece[][] board = position.position;

        System.out.println( (colour==0?"Black":"White") + " plays " + command);
        position.history.addCommandandColour(command,colour);

        int len = command.length();
        char[] al = new char[]{'a','b','c','d','e','f','g','h'};
        ArrayList alphabet = new ArrayList();
        for(char l : al) alphabet.add(l);
        char[] cmd = command.toCharArray();

        if(len == 2){
            int pos_x_new = alphabet.indexOf(cmd[0]);
            int pos_y_new = Integer.valueOf(String.valueOf(cmd[1])) - 1;
            //System.out.println(command.toCharArray()[0] + " - " + command.toCharArray()[1]);
            //System.out.println(pos_x_new + " - " + pos_y_new);
            for(int x = 0 ; x < size[0] ; x++){
                for(int y = 0 ; y < size[1] ; y++){
                    if( board[x][y].abbreviation == 'p' && board[x][y].colour == colour){

                        Object[] move_analysis = (Object[]) position.analysisALL[x][y];
                        boolean[][] movesto = (boolean[][]) move_analysis[0];
                        if(movesto[pos_x_new][pos_y_new]){
                            return move(x,y,pos_x_new,pos_y_new);
                        }

                    }
                }
            }
        }

        if(len == 3){

            if(cmd[0] == 'x'){//pawn takes
                int pos_x_new = alphabet.indexOf(cmd[1]);
                int pos_y_new = Integer.valueOf(String.valueOf(cmd[2])) - 1;
                for(int x = 0 ; x < size[0] ; x++){
                    for(int y = 0 ; y < size[1] ; y++){
                        if( board[x][y].abbreviation == 'p' && board[x][y].colour == colour){

                            Object[] attacker_analysis = (Object[]) position.analysisALL[x][y];
                            boolean[][] attacks = (boolean[][]) attacker_analysis[4];
                            if(attacks[pos_x_new][pos_y_new]){
                                return move(x,y,pos_x_new,pos_y_new);
                            }

                        }
                    }
                }
            }
            else{//simple moves eg qe4 kf2

                if(command == "O-O"){
                    switch (colour){
                        case 0:
                            if(position.position[4][7].abbreviation == 'k'){
                                return move(4,7,6,7);
                            }
                            break;

                        case 1:
                            if(position.position[4][0].abbreviation == 'k'){
                                return move(4,0,6,0);
                            }
                            break;
                    }
                }

                int pos_x_new = alphabet.indexOf(cmd[1]);
                int pos_y_new = Integer.valueOf(String.valueOf(cmd[2])) - 1;
                for(int x = 0 ; x < size[0] ; x++){
                    for(int y = 0 ; y < size[1] ; y++){
                        Object[] analysis = (Object[]) analysisALL[x][y];
                        boolean[][] movesto = (boolean[][]) analysis[0];
                        boolean[][] attacks = (boolean[][]) analysis[4];
                        if(board[x][y].abbreviation == cmd[0] && board[x][y].colour == colour){
                            if(attacks[pos_x_new][pos_y_new] || movesto[pos_x_new][pos_y_new]){
                                return move(x,y,pos_x_new,pos_y_new);
                            }
                        }
                    }
                }
            }


        }

        if(len == 4){//eg rxg8 rad3 r1d3 f8=q

            if(command.contains("x")){//simple takes eg rxg8
                int pos_x_new = alphabet.indexOf(cmd[2]);
                int pos_y_new = Integer.valueOf(String.valueOf(cmd[3])) - 1;
                for(int x = 0 ; x < size[0] ; x++){
                    for(int y = 0 ; y < size[1] ; y++){
                        if( board[x][y].abbreviation == cmd[0] && board[x][y].colour == colour){
                            Object[] attacker_analysis = (Object[]) position.analysisALL[x][y];
                            boolean[][] attacks = (boolean[][]) attacker_analysis[4];
                            if(attacks[pos_x_new][pos_y_new]){
                                return move(x,y,pos_x_new,pos_y_new);
                            }
                        }
                    }
                }
            }

            if(command.contains("=")){
                String[] split = command.split("=");
                if( split[1].length() == 1 ){
                    char promotion = split[1].toCharArray()[0];
                    int pos_x_new = alphabet.indexOf(cmd[0]);
                    int pos_y_new = Integer.valueOf(String.valueOf(cmd[1])) - 1;

                    for(int x = 0 ; x < size[0] ; x++){
                        for(int y = 0 ; y < size[1] ; y++){
                            if( board[x][y].abbreviation == 'p' && board[x][y].colour == colour){
                                Object[] move_analysis = (Object[]) position.analysisALL[x][y];
                                boolean[][] movesto = (boolean[][]) move_analysis[0];
                                if(movesto[pos_x_new][pos_y_new]){
                                    return move(pos_x_new,y,pos_x_new,pos_y_new,promotion);
                                }
                            }
                        }
                    }


                }

            }

        }

        if(len == 5){//eg xg8=q rd1d3


            if(command == "O-O-O"){

                switch (colour){
                    case 0:
                        if(position.position[4][7].abbreviation == 'k'){
                            return move(4,7,2,7);
                        }
                        break;

                    case 1:
                        if(position.position[4][0].abbreviation == 'k'){
                            return move(4,0,2,0);
                        }
                        break;
                }

            }

            if(command.contains("=")){//pawn promote
                int pos_x_new = alphabet.indexOf(command.toCharArray()[1]);
                int pos_y_new = Integer.valueOf(String.valueOf(cmd[2])) - 1;
                for(int x = 0 ; x < size[0] ; x++){
                    for(int y = 0 ; y < size[1] ; y++){
                        if( board[x][y].abbreviation == 'p' && board[x][y].colour == colour){
                            ArrayList movelist;
                            for(Move move : board[x][y].moves){
                                if(move.name != "pawn fork") continue;
                                movelist = move.moves;
                                for(int pattern_num = 0 ; pattern_num < movelist.size() ; pattern_num++){
                                    int[] pattern = (int[])movelist.get(pattern_num);
                                    if( x + pattern[0] == pos_x_new && y + pattern[1] == pos_y_new){
                                        return move(x,y,pos_x_new,pos_y_new,cmd[4]);
                                    }
                                }
                            }
                        }
                    }
                }
            }



        }

        System.out.println("Not implemented or illegal");
        position.history.removeLastCommandandColour();
        return false;
    }

    boolean move(int pos_x_old, int pos_y_old, int pos_x_new, int pos_y_new){
        return move(pos_x_old,pos_y_old,pos_x_new,pos_y_new,' ');
    }




    void setPlayingBoard(PlayingBoard playingBoard){
        this.playingBoard = playingBoard;
    }

    void drawBoard() throws InterruptedException {
        Piece[][] board = position.position;


        //playingBoard.update(position);
        //Thread.sleep(1000);

        String temp;
        char[] alphabet = new char[]{'A','B','C','D','E','F','G','H'};
        for(int y = size[1]-1 ; y >=0 ; y--){
            if(y == size[1]-1){
                temp = ANSI_CYAN + "   _________________________________________________";
                System.out.println(temp + ANSI_RESET);
            }
            System.out.print(y+1);
            for(int x = 0 ; x < size[0] ; x++){
                if(x==0){
                    temp = ANSI_CYAN + "  |  ";
                    System.out.print(temp + ANSI_RESET);
                }
                temp = board[x][y].colour == 0 ? ANSI_RED :  ANSI_WHITE;
                temp += board[x][y].abbreviation;
                System.out.print(temp + ANSI_RESET);
                temp = ANSI_CYAN + "  |  ";
                System.out.print(temp + ANSI_RESET);
            }
            System.out.println("");
            temp = ANSI_CYAN + "   _________________________________________________";
            System.out.println(temp + ANSI_RESET);
        }
        System.out.print("   ");
        for(int x = 0 ; x < size[0] ; x++){
            System.out.print( "   " + alphabet[x] + "  " );
        }
        System.out.print("\n\n");
    }

    void drawBoard(Piece[][] board){

        String temp;
        char[] alphabet = new char[]{'A','B','C','D','E','F','G','H'};
        for(int y = size[1]-1 ; y >=0 ; y--){
            if(y == size[1]-1){
                temp = ANSI_CYAN + "   _________________________________________________";
                System.out.println(temp + ANSI_RESET);
            }
            System.out.print(y+1);
            for(int x = 0 ; x < size[0] ; x++){
                if(x==0){
                    temp = ANSI_CYAN + "  |  ";
                    System.out.print(temp + ANSI_RESET);
                }
                temp = board[x][y].colour == 0 ? ANSI_RED :  ANSI_WHITE;
                temp += board[x][y].abbreviation;
                System.out.print(temp + ANSI_RESET);
                temp = ANSI_CYAN + "  |  ";
                System.out.print(temp + ANSI_RESET);
            }
            System.out.println("");
            temp = ANSI_CYAN + "   _________________________________________________";
            System.out.println(temp + ANSI_RESET);
        }
        System.out.print("   ");
        for(int x = 0 ; x < size[0] ; x++){
            System.out.print( "   " + alphabet[x] + "  " );
        }
        System.out.print("\n\n");
    }

    void drawBoard(char[][] symbol){
        String temp;
        char[] alphabet = new char[]{'A','B','C','D','E','F','G','H'};
        for(int y = size[1]-1 ; y >=0 ; y--){
            if(y == size[1]-1){
                temp = ANSI_CYAN + "   _________________________________________________";
                System.out.println(temp + ANSI_RESET);
            }
            System.out.print(y+1);
            for(int x = 0 ; x < size[0] ; x++){
                if(x==0){
                    temp = ANSI_CYAN + "  |  ";
                    System.out.print(temp + ANSI_RESET);
                }
                temp = position.position[x][y].colour == 0 ? ANSI_RED : position.position[x][y].colour == -1 ? ANSI_BLUE :  ANSI_WHITE;
                temp += symbol[x][y];
                System.out.print(temp + ANSI_RESET);
                temp = ANSI_CYAN + "  |  ";
                System.out.print(temp + ANSI_RESET);
            }
            System.out.println("");
            temp = ANSI_CYAN + "   _________________________________________________";
            System.out.println(temp + ANSI_RESET);
        }
        System.out.print("   ");
        for(int x = 0 ; x < size[0] ; x++){
            System.out.print( "   " + alphabet[x] + "  " );
        }
        System.out.print("\n\n");
    }

    void drawBoardHistory(){

        System.out.println("Board History:");

        for(int step = 0 ; step < position.history.position_history.size() ; step++){
            drawBoard(position.history.position_history.get(step).position);
        }
    }

    void drawBaselineMoved(){
        System.out.println("Baseline moved:");
        char[][] moved = new char[size[0]][size[1]];
        boolean[][] baseline_moved = position.baseline_moved;

        int y,col;

        y = 0;
        col = 1;
        for(int x = 0 ; x < size[0] ; x++){
            if(baseline_moved[col][x]){
                moved[x][y] = 'x';
            }
        }

        y = 7;
        col = 0;
        for(int x = 0 ; x < size[0] ; x++){
            if(baseline_moved[col][x]){
                moved[x][y] = 'x';
            }
        }

        drawBoard(moved);

    }


    void drawBoard_reach(int colour){
        System.out.println("All reached squares by " + (colour==0?"Black":"White") + ":" );
        char[][] attacked = new char[size[0]][size[1]];
        boolean[][] reaches = reaches(colour);
        for(int x = 0 ; x < size[0] ; x++){
            for(int y = 0 ; y < size[1] ; y++){
                //if(x == pos_x && y == pos_y) attacked[x][y] = board.board[x][y].abbreviation;
                if(reaches[x][y]) attacked[x][y] = 'x';
            }
        }
        drawBoard(attacked);
    }

    void drawBoard_reach(int pos_x, int pos_y){
        Piece[][] board = position.position;

        char[] al = new char[]{'a','b','c','d','e','f','g','h'};
        ArrayList alphabet = new ArrayList();
        for(char l : al) alphabet.add(l);
        char cmd = (char) pos_x;
        char coord1 = al[cmd];
        System.out.println("All reachable squares by " + (board[pos_x][pos_y].colour==0?"Black":"White") + "'s " + board[pos_x][pos_y].name + " on " + coord1+(pos_y+1) + ":" );
        char[][] reached = new char[size[0]][size[1]];
        boolean[][] reach = reaches(pos_x,pos_y);
        for(int x = 0 ; x < size[0] ; x++){
            for(int y = 0 ; y < size[1] ; y++){
                if(x == pos_x && y == pos_y) reached[x][y] = board[x][y].abbreviation;
                if(reach[x][y]) reached[x][y] = 'x';
            }
        }
        drawBoard(reached);
    }

    void drawBoard_allAttackedSquares(int colour){
        System.out.println("All attacked squares by " + (colour==0?"Black":"White") + ":" );
        char[][] attacked = new char[size[0]][size[1]];
        boolean[][] attacks = attacks(colour);
        for(int x = 0 ; x < size[0] ; x++){
            for(int y = 0 ; y < size[1] ; y++){
                //if(x == pos_x && y == pos_y) attacked[x][y] = board.board[x][y].abbreviation;
                if(attacks[x][y]) attacked[x][y] = 'x';
            }
        }
        drawBoard(attacked);
    }

    void drawBoard_allAttackedSquares(int pos_x, int pos_y){
        Piece[][] board = position.position;

        char[] al = new char[]{'a','b','c','d','e','f','g','h'};
        ArrayList alphabet = new ArrayList();
        for(char l : al) alphabet.add(l);
        char cmd = (char) pos_x;
        char coord1 = al[cmd];
        System.out.println("All attacked squares by " + (board[pos_x][pos_y].colour==0?"Black":"White") + "'s " + board[pos_x][pos_y].name + " on " + coord1+(pos_y+1) + ":" );
        char[][] attacked = new char[size[0]][size[1]];
        boolean[][] attacks = attacks(pos_x,pos_y);
        for(int x = 0 ; x < size[0] ; x++){
            for(int y = 0 ; y < size[1] ; y++){
                if(x == pos_x && y == pos_y) attacked[x][y] = board[x][y].abbreviation;
                if(attacks[x][y]) attacked[x][y] = 'x';
            }
        }
        drawBoard(attacked);
    }

    boolean coordinatesOnBoard(int x, int y){
        return 0 <= x && x < size[0] && 0 <= y && y < size[1];
    }


    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";


}
