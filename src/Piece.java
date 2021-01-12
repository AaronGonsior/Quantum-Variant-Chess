import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Piece {

    String name;
    char abbreviation;
    int colour;
    Move[] moves;
    ImageIcon imageIcon;

    //static char[] allPieces = new char[]{' ','k','q','p','b','n','r','e','h','c','j','l'};

    public Piece(char abbreviation, int colour){
        this.abbreviation = abbreviation;
        this.colour = colour;
        imageIcon = new ImageIcon("graphics/pieces/" + abbreviation + "" + colour + ".png");

        switch (abbreviation) {
            case ' ':
                name = "None";
                colour = -1;
                moves = new Move[0];
                break;
            case 'k':
                name = "King";
                moves = new Move[4];
                moves[0] = new Move('x',false,false,true,false);
                moves[1] = new Move('+',false,false,true,false);
                moves[2] = new Move('o',false,false,false,false);
                moves[3] = new Move('O',false,false,false,false);
                break;
            case 'q':
                name = "Queen";
                moves = new Move[2];
                moves[0] = new Move('x',true,true,true,false);
                moves[1] = new Move('+',true,true,true,false);
                break;
            case 'p':
                name = "Pawn";
                moves = new Move[3];
                moves[0] = new Move('1',colour,false,false);
                moves[1] = new Move('v',colour,true,true);
                moves[2] = new Move('2',colour,false,false);
                break;
            case 'b':
                name = "Bishop";
                moves = new Move[1];
                moves[0] = new Move('x',true,true,true,false);
                break;
            case 'n':
                name = "Knight";
                moves = new Move[1];
                moves[0] = new Move('j', false,false,true,false);
                break;
            case 'r':
                name = "Rook";
                moves = new Move[1];
                moves[0] = new Move('+',true,true,true,false);
                break;
            case 'e':
                name = "Elephant";
                moves = new Move[2];
                moves[0] = new Move('+',true,true,true,false);
                moves[1] = new Move('j',false,false,true,false);
                break;
            case 'h':
                name = "Hawk";
                moves = new Move[2];
                moves[0] = new Move('x',true,true,true,false);
                moves[1] = new Move('j',false,false,true,false);
                break;
            case 'c':
                name = "Counselor";
                moves = new Move[1];
                moves[0] = new Move('x',false,false,true,false);
                break;
            case 'j':
                name = "Jaguar";
                moves = new Move[2];
                moves[0] = new Move('c',false,false,true,false);
                moves[1] = new Move('v',colour,true,true);
                break;
            case 'l':
                name = "Lion";
                moves = new Move[4];
                moves[0] = new Move('c',false,false,true,false);
                moves[1] = new Move('l',true,false,true,true);
                moves[2] = new Move('v',colour,true,false);
                moves[3] = new Move('1',colour,false,false);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + abbreviation);
        }
    }

    public Piece(String name, char abbreviation, int colour, Move[] moves){
        this.name = name;
        this.abbreviation = abbreviation;
        this.colour = colour;
        this.moves = moves;
        imageIcon = new ImageIcon("graphics/pieces/" + abbreviation + "" + colour + ".png");
    }

    public Piece clone(){
        return new Piece(name,abbreviation,colour,moves.clone());
    }

    Move[] getMoves(int pos_x, int pos_y){
        return moves;
    }

}
