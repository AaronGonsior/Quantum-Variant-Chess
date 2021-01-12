import java.util.ArrayList;

public class Position {

    Piece[][] position;
    boolean[][] baseline_moved;
    int[] size;
    History history;
    Object[][] analysisALL;

    public Position(int[] size){
        this.size = size;

        this.position = new Piece[size[0]][size[1]];
        for(int x = 0 ; x < size[0] ; x++){
            for(int y = 0 ; y < size[1] ; y++){
                position[x][y] = new Piece(' ',-1);
            }
        }

        baseline_moved = new boolean[2][size[0]];

        analysisALL = new Object[size[0]][size[1]];
        for(int x = 0 ; x < size[0] ; x++){
            for(int y = 0 ; y < size[1] ; y++){
                analysisALL[x][y] = new Object[6];
            }
        }

        history = new History();

        analyse();

        history.addPosition(this);

    }

    public Position(int[] size, Piece[][] position){
        this.size = size;
        this.position = position;
        baseline_moved = new boolean[2][size[0]];

        analysisALL = new Object[size[0]][size[1]];
        for(int x = 0 ; x < size[0] ; x++){
            for(int y = 0 ; y < size[1] ; y++){
                analysisALL[x][y] = new Object[6];
            }
        }

        history = new History();

        analyse();

        history.addPosition(this);

    }

    public Position(int[] size, Piece[][] position, boolean[][] baseline_moved){
        this.size = size;
        this.position = position;
        this.baseline_moved = baseline_moved;

        analysisALL = new Object[size[0]][size[1]];
        for(int x = 0 ; x < size[0] ; x++){
            for(int y = 0 ; y < size[1] ; y++){
                analysisALL[x][y] = new Object[6];
            }
        }

        history = new History();

        analyse();

        history.addPosition(this);
    }

    public Position(int[] size, Piece[][] position, boolean[][] baseline_moved, History history){
        this.size = size;
        this.position = position;
        this.baseline_moved = baseline_moved;
        this.history = history.clone();

        analysisALL = new Object[size[0]][size[1]];
        for(int x = 0 ; x < size[0] ; x++){
            for(int y = 0 ; y < size[1] ; y++){
                analysisALL[x][y] = new Object[6];
            }
        }

        history = new History();

        analyse();

        //history.addPosition(this);
    }

    public Position(int[] size, Piece[][] position, boolean[][] baseline_moved, History history, Object[][] analysisALL){
        this.size = size;
        this.position = position;
        this.baseline_moved = baseline_moved;
        this.history = history.clone();
        this.analysisALL = analysisALL;

        history = new History();

        analyse();

        history.addPosition(this);
    }

    public Position clone(){

        Piece[][] clone_position = new Piece[size[0]][size[1]];
        for(int x = 0 ; x < size[0] ; x++){
            for(int y = 0 ; y < size[1] ; y++){
                clone_position[x][y] = position[x][y];
            }
        }

        boolean[][] clone_baseline_moved = new boolean[2][size[0]];
        for(int col = 0 ; col < 2 ; col++){
            for(int x = 0 ; x < size[0] ; x++){
                clone_baseline_moved[col][x] = baseline_moved[col][x];
            }
        }

        return new Position(size,clone_position,clone_baseline_moved,history/*.clone()*/);
    }



    void analyse(){
        analysisALL = fullAnalysisAll();
    }

    public void setPosition(Piece[][] position){
        this.position = position;
    }

    void baseline_moved(int colour, int pos_x){
        baseline_moved[colour][pos_x] = true;
    }

    void setSquare(int x, int y, Piece piece){
        position[x][y] = piece;
    }

    void savePosition(){
        history.addPosition(this);
    }

    String convert_xyxy_to_cmd(int x_old, int y_old, int x_new, int y_new){
        String result = "";

        char[] al = new char[]{'a','b','c','d','e','f','g','h'};

        String sq_old, sq_new;
        sq_old = al[x_old] + "" + (y_old+1);
        sq_new = al[x_new] + "" + (y_new+1);

        if(position[x_old][y_old].abbreviation == 'k' && Math.abs(x_new-x_old) > 1 ){
            if(x_new > x_old){
                return "O-O";
            } else{
                return "O-O-O";
            }
        }

        if(position[x_old][y_old].abbreviation != 'p'){
            result += position[x_old][y_old].abbreviation;
        }

        if( command_clearity(x_old,y_old,x_new,y_new) != 1 ){
            if(position[x_old][y_old].abbreviation == 'p'){
                result += al[x_old];
            } else{
                result += sq_old;
            }

        }

        if( position[x_new][y_new].colour != -1 && position[x_old][y_old].colour != position[x_new][y_new].colour ){
            result += "x";
        }

        result += sq_new;


        return result;
    }

    int command_clearity(int x_old, int y_old, int x_new, int y_new){

        int possibilities = 0;

        for(int x = 0 ; x < size[0] ; x++){
            for(int y = 0 ; y < size[1] ; y++){
                Object[] analysis = (Object[]) analysisALL[x][y];
                boolean[][] movesto = (boolean[][]) analysis[0];
                boolean[][] attacks = (boolean[][]) analysis[4];
                if( position[x][y].abbreviation == position[x_old][y_old].abbreviation && (movesto[x_new][y_new] || attacks[x_new][y_new]) ){
                    possibilities++;
                }
            }
        }

        return possibilities;
    }



    // 0 - boolean[][] movesto - can pos_x,pos_y move to [][]?
    // 1 - Move[][] movesto_moves - with which move can pos_x,pos_y move to [][]?
    // 2 - boolean[][] defends - does pos_x,pos_y defend [][]?
    // 3 - Move[][] defending_moves - which Move of pos_x,pos_y defends [][]?
    // 4 - boolean[][] attacks - does pos_x,pos_y attack [][]?
    // 5 - Move[][] attacking_moves - which Move of pos_x,pos_y attacks [][]?
    Object[] fullAnalysis(int pos_x, int pos_y){

        Piece[][] board = position;

        boolean[][] movesto = new boolean[size[0]][size[1]];
        Move[][] movesto_moves = new Move[size[0]][size[1]];
        boolean[][] defends = new boolean[size[0]][size[1]];
        Move[][] defending_moves = new Move[size[0]][size[1]];
        boolean[][] attacks = new boolean[size[0]][size[1]];
        Move[][] attacking_moves = new Move[size[0]][size[1]];

        /*case parameter:
        blocked_on_way
        continued
        attacking
        move_only_with_attacking
         */

        Piece attacker = board[pos_x][pos_y];
        Move[] moves = attacker.getMoves(pos_x,pos_y);
        int x;
        int y;
        boolean blocked;
        ArrayList movelist;
        int[] pattern;
        for(Move move : moves){

            if(move.move_type == "standard"){

                String szenario = "" + (move.blockedOnWay?"1":"0") + (move.continued?"1":"0") + (move.attacking?"1":"0") + (move.move_only_with_attacking?"1":"0"); //xx01 can never exist (if you can only move when you attack but you can't attack you can't move)

                switch (szenario){

                    case "0000": // 1
                        movelist = move.moves;
                        for(int pattern_num = 0 ; pattern_num < movelist.size() ; pattern_num++) {
                            pattern = (int[]) movelist.get(pattern_num);
                            x = pos_x + pattern[0];
                            y = pos_y + pattern[1];
                            if(!coordinatesOnBoard(x,y)) continue;
                            if(board[x][y].colour == -1){
                                movesto[x][y] = true;
                                movesto_moves[x][y] = move;
                            }
                        }
                        break;

                    case "0010": //king , jumps
                        movelist = move.moves;
                        for(int pattern_num = 0 ; pattern_num < movelist.size() ; pattern_num++) {
                            pattern = (int[]) movelist.get(pattern_num);
                            x = pos_x + pattern[0];
                            y = pos_y + pattern[1];
                            if(!coordinatesOnBoard(x,y)) continue;
                            if(board[x][y].colour == -1){
                                movesto[x][y] = true;
                                movesto_moves[x][y] = move;
                            }
                            else if(board[pos_x][pos_y].colour != board[x][y].colour){
                                attacks[x][y] = true;
                                attacking_moves[x][y] = move;
                            }
                            else{
                                defends[x][y] = true;
                                defending_moves[x][y] = move;
                            }
                        }
                        break;

                    case "0011": //pawn fork
                        if(history.move_history != null && !history.move_history.isEmpty() && history.move_history.size() >= 2){
                            /*en passant possible*/
                            // ! we are in analysis here

                            Object[] lastmove_mine;
                            Object[] lastmove_opponent;

                            if(history.colour_history.size() != 0 && history.getLastColour() != position[pos_x][pos_y].colour){
                                //lastmove_mine = history.move_history.get(history.move_history.size()-2);
                                lastmove_opponent = history.move_history.get(history.move_history.size()-1);
                                if((Move)(lastmove_opponent[1]) == null){
                                    continue;
                                }
                                if( ( (Move)(lastmove_opponent[1]) ).abbreviation == '2' && ( pos_x == ((int[])(lastmove_opponent[0]))[0] + 1 ||  pos_x == ((int[])(lastmove_opponent[0]))[0] - 1 ) && pos_y ==  (((int[])(lastmove_opponent[0]))[1]) + ((position[pos_x][pos_y].colour==0)?2:-2) ){
                                    attacks[ ((int[])(lastmove_opponent[0]))[0] ][ (((int[])(lastmove_opponent[0]))[1]) + ((position[pos_x][pos_y].colour==0)?1:-1) ] = true;
                                }
                            }
                            /*
                            else {
                                //lastmove_mine = history.move_history.get(history.move_history.size()-1);
                                //lastmove_opponent = history.move_history.get(history.move_history.size()-2);
                                //System.out.println("en passant error");
                                //continue;
                            }
                            */



                        }

                        movelist = move.moves;
                        for(int pattern_num = 0 ; pattern_num < movelist.size() ; pattern_num++) {
                            pattern = (int[]) movelist.get(pattern_num);
                            x = pos_x + pattern[0];
                            y = pos_y + pattern[1];
                            if(!coordinatesOnBoard(x,y)) continue;
                            if(board[x][y].colour == -1) continue;
                            if(board[pos_x][pos_y].colour != board[x][y].colour){
                                attacks[x][y] = true;
                                attacking_moves[x][y] = move;
                            }
                            else{
                                defends[x][y] = true;
                                defending_moves[x][y] = move;
                            }
                        }


                        break;

                    case "0100": //doesn't exist (yet)

                        break;

                    case "0110": //doesn't exist (yet)

                        break;

                    case "0111": //doesn't exist (yet)

                        break;

                    case "1000": //rush (2)
                        blocked = false;
                        movelist = move.moves;
                        for(int pattern_num = 0 ; pattern_num < movelist.size() ; pattern_num++) {
                            pattern = (int[]) movelist.get(pattern_num);
                            for(int step = 1 ; step < Math.max(pattern[0],pattern[1]) ; step++) {
                                x = pos_x + step * (pattern[0] / Math.max(pattern[0], pattern[1]));
                                y = pos_y + step * (pattern[1] / Math.max(pattern[0], pattern[1]));
                                if(!coordinatesOnBoard(x,y)) continue;
                                if (board[x][y].abbreviation != ' ') blocked = true;
                            }
                            x = pos_x + pattern[0];
                            y = pos_y + pattern[1];
                            if(!coordinatesOnBoard(x,y)) continue;
                            if(!blocked && board[x][y].colour == -1){
                                movesto[x][y] = true;
                                movesto_moves[x][y] = move;
                            }
                        }
                        break;

                    case "1010": //doesn't exist (yet)

                        break;

                    case "1011": //leap

                        blocked = false;
                        movelist = move.moves;
                        for(int pattern_num = 0 ; pattern_num < movelist.size() ; pattern_num++) {
                            blocked = false;
                            pattern = (int[]) movelist.get(pattern_num);
                            for(int step = 1 ; step < Math.max(pattern[0],pattern[1]) ; step++) {
                                x = pos_x + step * (pattern[0] / Math.max(pattern[0], pattern[1]));
                                y = pos_y + step * (pattern[1] / Math.max(pattern[0], pattern[1]));
                                if(!coordinatesOnBoard(x,y)) continue;
                                if (board[x][y].abbreviation != ' ') blocked = true;
                            }
                            x = pos_x + pattern[0];
                            y = pos_y + pattern[1];
                            if(!coordinatesOnBoard(x,y)) continue;
                            if(blocked) continue;
                            if(board[x][y].colour == -1) continue;
                            if(board[pos_x][pos_y].colour != board[x][y].colour){
                                attacks[x][y] = true;
                                attacking_moves[x][y] = move;
                            }
                            else{
                                defends[x][y] = true;
                                defending_moves[x][y] = move;
                            }
                        }

                        break;

                    case "1100": //doesn't exist (yet)

                        break;

                    case "1110": // x , +

                        blocked = false;
                        movelist = move.moves;
                        for(int pattern_num = 0 ; pattern_num < movelist.size() ; pattern_num++) {
                            blocked = false;
                            pattern = (int[]) movelist.get(pattern_num);
                            x = pos_x + pattern[0];
                            y = pos_y + pattern[1];
                            while(coordinatesOnBoard(x,y) && !blocked){

                                if(board[x][y].colour == -1){
                                    movesto[x][y] = true;
                                    movesto_moves[x][y] = move;
                                }
                                else if(board[pos_x][pos_y].colour != board[x][y].colour){
                                    blocked = true;
                                    attacks[x][y] = true;
                                    attacking_moves[x][y] = move;
                                }
                                else{
                                    blocked = true;
                                    defends[x][y] = true;
                                    defending_moves[x][y] = move;
                                }

                                x += pattern[0];
                                y += pattern[1];
                            }
                        }

                        break;

                    case "1111": //doesn't exist (yet)

                        break;

                }

            }
            else if(move.move_type == "conditional"){
                //we are in analyse!

                char abbreviation = move.abbreviation;
                int colour;
                Object[][] lastanalysisAll;

                switch (abbreviation){

                    case 'o':
                        colour = position[pos_x][pos_y].colour;
                        if( position[pos_x][pos_y].abbreviation != 'k' || pos_x != 4 || !(pos_y == 0 || pos_y == 7) ) continue;
                        if( (colour == 0 && pos_y != 7) || (colour == 1 && pos_y != 0) ) continue;
                        if(!coordinatesOnBoard(pos_x,pos_y) || !coordinatesOnBoard(pos_x+3,pos_y)) continue;
                        if(baseline_moved[colour][pos_x] || baseline_moved[colour][pos_x+3]) continue;
                        if(position[pos_x+3][pos_y].abbreviation != 'r') continue;
                        if(position[pos_x+1][pos_y].abbreviation != ' ' || position[pos_x+2][pos_y].abbreviation != ' ') continue;

                        lastanalysisAll = history.getLastPosition().analysisALL;
                        if( isAttacked(pos_x,pos_y,lastanalysisAll) || isAttacked(pos_x+1,pos_y,lastanalysisAll) || isAttacked(pos_x+2,pos_y,lastanalysisAll) ) continue;

                        movesto[pos_x+2][pos_y] = true;
                        movesto_moves[pos_x+2][pos_y] = move;

                        break;

                    case 'O':
                        colour = position[pos_x][pos_y].colour;
                        if( position[pos_x][pos_y].abbreviation != 'k' || pos_x != 4 || !(pos_y == 0 || pos_y == 7) ) continue;
                        if( (colour == 0 && pos_y != 7) || (colour == 1 && pos_y != 0) ) continue;
                        if(!coordinatesOnBoard(pos_x,pos_y) || !coordinatesOnBoard(pos_x-4,pos_y)) continue;
                        if(baseline_moved[colour][pos_x] || baseline_moved[colour][pos_x-4]) continue;
                        if(position[pos_x-4][pos_y].abbreviation != 'r') continue;
                        if(position[pos_x-1][pos_y].abbreviation != ' ' || position[pos_x-2][pos_y].abbreviation != ' ' || position[pos_x-3][pos_y].abbreviation != ' ') continue;

                        lastanalysisAll = history.getLastPosition().analysisALL;
                        if( isAttacked(pos_x,pos_y,lastanalysisAll) || isAttacked(pos_x-1,pos_y,lastanalysisAll) || isAttacked(pos_x-2,pos_y,lastanalysisAll) || isAttacked(pos_x-3,pos_y,lastanalysisAll) ) continue;

                        movesto[pos_x-2][pos_y] = true;
                        movesto_moves[pos_x-2][pos_y] = move;

                        break;

                }

            }



        }

        return new Object[]{movesto,movesto_moves,defends,defending_moves,attacks,attacking_moves};

    }

    Object[][] fullAnalysisAll(){
        Object[][] result = new Object[size[0]][size[1]];
        for(int x = 0 ; x < size[0] ; x++){
            for(int y = 0 ; y < size[1] ; y++){
                result[x][y] = fullAnalysis(x,y);
            }
        }
        return result;
    }


    boolean isAttacked(int pos_x, int pos_y, Object[][] analysisALL){
        Piece[][] board = position;

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




    boolean incheck(int colour){
        //Object[][] analysisALL = analysisAll;
        Piece[][] board = position;

        for(int x = 0 ; x < size[0] ; x++){
            for(int y = 0 ; y < size[1] ; y++){
                if( board[x][y].colour == colour && board[x][y].abbreviation == 'k' ){
                    return isAttacked(x,y,analysisALL);
                }
            }
        }
        return false;
    }



    boolean coordinatesOnBoard(int x, int y){
        return 0 <= x && x < size[0] && 0 <= y && y < size[1];
    }


}
