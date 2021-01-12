import java.util.ArrayList;

public class History {

    ArrayList<Position> position_history;
    ArrayList<String> command_history;
    ArrayList<Object[]> move_history;
    ArrayList<Integer> colour_history;

    public History(){
        position_history = new ArrayList<Position>();
        command_history = new ArrayList<String>();
        move_history = new ArrayList<Object[]>();
        colour_history = new ArrayList<Integer>();
    }

    public History(History history){
        History clone = history.clone();
        this.position_history = clone.position_history;
        this.command_history = clone.command_history;
        this.move_history = clone.move_history;
        this.colour_history = clone.colour_history;
    }



    public History clone(){
        History clone = new History();
        for(int i = 0 ; i < position_history.size() ; i++){
            clone.position_history.add(this.position_history.get(i)/*.clone()*/);
        }
        for(int i = 0 ; i < command_history.size() ; i++){
            clone.command_history.add(this.command_history.get(i));
        }
        for(int i = 0 ; i < move_history.size() ; i++){
            clone.move_history.add(this.move_history.get(i));
        }
        for(int i = 0 ; i < colour_history.size() ; i++){
            clone.colour_history.add(this.colour_history.get(i));
        }
        return clone;
    }



    void addPosition(Position position){
        position_history.add(position/*.clone()*/);
    }

    void addCommandandColour(String cmd, int colour){
        command_history.add(cmd);
        colour_history.add(colour);
    }

    void addMove(int pos_x_old, int pos_y_old, Move move){
        move_history.add(new Object[]{new int[]{pos_x_old,pos_y_old},move});
    }



    Position getLastPosition(){
        return position_history.get(position_history.size()-1);
    }

    int getLastColour(){
        return colour_history.get(colour_history.size()-1);
    }




    void removeLastCommandandColour(){
        if(command_history.size() > 0){
            command_history.remove(command_history.size()-1);
            colour_history.remove(colour_history.size()-1);
        }
    }

    void removeLastMove(){
        move_history.remove(move_history.size()-1);
    }

    void removeLastPosition(){
        position_history.remove(position_history.size()-1);
    }





    void print_command_colour_history(){
        System.out.println("Legal command history:");
        for(int cmd = 0 ; cmd < command_history.size() ; cmd++){
            System.out.println(""+(colour_history.get(cmd)==1?"White":"Black") + " : " + command_history.get(cmd));
        }
    }

    void print_move_history_abbreviation(){
        Move move;
        for(int move_num = 0 ; move_num < move_history.size()-1 ; move_num++){
            move = (Move)(move_history.get(move_num))[1];
            System.out.println(""+(colour_history.get(move_num)==1?"White":"Black") + " : " + move.name + " - " + move.abbreviation);
        }
    }


}
