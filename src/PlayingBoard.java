import javafx.css.Size;
import javafx.geometry.Pos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.net.URL;

public class PlayingBoard extends JFrame implements MouseListener, Runnable {

    public static final int SIZE_Fields = 100;
    public static int size_x,size_y;
    Position position;
    PiecesPanel pp;
    BoardPanel bp;
    Game game;
    Menu history_menu;

    public PlayingBoard(int size_x, int size_y, Game game) throws InterruptedException {
        this.size_x = size_x;
        this.size_y = size_y;
        this.game = game;
        this.position = game.board.position;

        addMouseListener(this);

        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        bp = new BoardPanel(size_x,size_y);
        add(bp);          //add underlaying board
        pp = new PiecesPanel(size_x,size_y,position);
        setGlassPane(pp); //add glass pane
        getGlassPane().setVisible(true);

        System.out.println(getFrames()[0].getSize());

        history_menu = new Menu("History Menu",game,new String[]{"1","2"});

        pack();
        setVisible(true);
    }

    void update(Position position) throws InterruptedException {
        remove(bp);
        bp = new BoardPanel(size_x,size_y);
        add(bp);          //add underlaying board
        pp = new PiecesPanel(size_x,size_y,position);
        setGlassPane(pp); //add glass pane
        getGlassPane().setVisible(true);

        //setSize(1000,1000);

        pack();
        setVisible(true);

        //remove(this);
        //add(this);
        update(getGraphics());
        //setDefaultCloseOperation(EXIT_ON_CLOSE);


        history_menu.closeWindow();
        String[] cmd_history = new String[position.history.command_history.size()];
        for(int cmd = 0 ; cmd < position.history.command_history.size() ; cmd++){
            cmd_history[cmd] = cmd+1+". "+position.history.command_history.get(cmd);
        }
        history_menu = new Menu("History Menu",this,cmd_history,((cmd_history.length-1)/2)+1 );

    }

    void exit(){
        System.out.println("frame exit");
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    int[] drag_start = new int[]{0,0};
    int[] drag_end = new int[]{0,0};
    boolean hold = false;
    public void mouseClicked(MouseEvent e) {
        //Graphics g=getGraphics();
        //g.setColor(Color.BLUE);
        //g.fillOval(e.getX(),e.getY(),30,30);
    }
    public void mouseEntered(MouseEvent e) {
        //System.out.println("mouse entered");
    }
    public void mouseExited(MouseEvent e) {
        //System.out.println("mouse exited");
    }
    public void mousePressed(MouseEvent e) {
        //System.out.println("Mouse pressed");
        drag_start = new int[]{e.getX(),e.getY()};
        hold = true;
    }
    public void mouseReleased(MouseEvent e) {
        //System.out.println("Mouse released");
        drag_end = new int[]{e.getX(),e.getY()};
        try {
            DragAnalysis(drag_start[0],drag_start[1],drag_end[0],drag_end[1]);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        hold = false;
    }

    void DragAnalysis(int x_from, int y_from, int x_to, int y_to) throws InterruptedException {

        int x_old,y_old,x_new,y_new;

        x_old = x_from/SIZE_Fields;
        y_old = y_from/SIZE_Fields;
        x_new = x_to/SIZE_Fields;
        y_new = y_to/SIZE_Fields;

        game.DragReport(x_old,size_y-1-y_old,x_new,size_y-1-y_new);

    }

    @Override
    public void run() {

    }
}

class BoardPanel extends JPanel {

    public static int size_x,size_y;
    private final Color[] COLOR_ARRAY = {Color.decode("#FFFACD"), Color.decode("#493E0A")};

    public BoardPanel(int size_x, int size_y)   {

        this.size_x = size_x;
        this.size_y = size_y;

        setLayout(new GridLayout(size_y, size_x));
        int numView = 1;

        for (int i = 0; i < size_y; i++) {
            numView = (numView == 0) ? 1:0;
            for (int j = 0; j < size_x; j++) {

                if(size_x % 2 != 0 && j == 0){
                    numView = (numView == 0) ? 1:0;
                }

                TileView tw = new TileView(COLOR_ARRAY[numView]);
                //setComponentZOrder(tw,0);
                add(tw);

                numView = (numView == 0) ? 1:0;

            }
        }
    }
}

//container for pieces
class PiecesPanel extends JLabel {

    public static int size_x,size_y;
    PiecesPanel(int size_x, int size_y, Position position) throws InterruptedException {

        this.size_x = size_x;
        this.size_y = size_y;

        setOpaque(false); //make it transparent
        setLayout(new GridLayout(size_y, size_x));

        JComponent piece;
        int x,y;
        for(int test = 0 ; test < size_x*size_y ; test++){
            x = (int)((0.0+test)/(0.0+size_y));
            y = test%size_y;
            piece = new BoardPiece(position.position[y][size_x-1-x]);
            super.add(piece);
        }

    }

}

class TileView extends JLabel {

    public static final int SIZE = 100;
    TileView(Color color) {
        setPreferredSize(new Dimension(SIZE, SIZE));
        setOpaque(true);
        setBackground(color);
    }
}

class BoardPiece extends JLabel{

    public static final int SIZE = 100;

    BoardPiece(Piece piece) {

        ImageIcon imageIcon;
        if(piece.abbreviation != ' '){
            imageIcon = new ImageIcon(piece.imageIcon.getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_DEFAULT));
            //imageIcon = new ImageIcon(new ImageIcon("graphics/pieces/" + piece.abbreviation + "" + piece.colour + ".png").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_DEFAULT));
            setIcon(imageIcon);
            setVerticalTextPosition(SwingConstants.CENTER);
            setHorizontalTextPosition(SwingConstants.CENTER);
        }

    }
}