import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.nio.CharBuffer;

public class Menu extends JFrame implements Runnable, Readable {

    Game game;
    PlayingBoard playingBoard;

    String title;
    String[] buttonlabels;
    //String[] supercalls;
    //String[] options;
    JButton[] buttons;
    boolean read;
    String[] result;

    public Menu(String title, Game game, String[] buttonlabels/*, String[] supercalls, String[] options*/){
        this.game = game;
        read = false;

        this.title = title;
        if(title == "Main Menu") this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.buttonlabels = buttonlabels;
        //this.supercalls = supercalls;
        //this.options = options;

        setTitle(title);
        JPanel p = new JPanel(new GridLayout(1, 3));
        JPanel p1 = new JPanel();
        Box box1 = new Box(BoxLayout.X_AXIS);

        box1.add(Box.createVerticalStrut(100));
        box1.add(Box.createHorizontalGlue());
        box1.add(Box.createVerticalGlue());

        JButton[] buttons = new JButton[buttonlabels.length];
        for(int bt = 0 ; bt < buttonlabels.length ; bt++){
            buttons[bt] = new JButton(buttonlabels[bt]);
            box1.add(buttons[bt]);
            box1.add(Box.createHorizontalStrut(15));
        }
        this.buttons = buttons;

        box1.add(Box.createHorizontalGlue());
        p1.add(box1);
        p1.setBorder(BorderFactory.createRaisedBevelBorder());

        p.add(p1);

        getContentPane().add(p);
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        this.setVisible(true);

    }

    public Menu(String title, PlayingBoard playingBoard, String[] buttonlabels, int rows){
        this.playingBoard = playingBoard;

        this.title = title;
        if(title == "Main Menu") this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.buttonlabels = buttonlabels;
        //this.supercalls = supercalls;
        //this.options = options;

        setTitle(title);
        JPanel p = new JPanel(new GridLayout(rows, 2));

        JPanel p1 = new JPanel();
        Box box1 = new Box(BoxLayout.X_AXIS);
        /*
        JPanel p1 = new JPanel();
        Box box1 = new Box(BoxLayout.X_AXIS);
        box1.add(Box.createVerticalStrut(50));
        box1.add(Box.createHorizontalGlue());
        //box1.add(Box.createVerticalGlue());
         */

        JButton[] buttons = new JButton[buttonlabels.length];
        for(int bt = 0 ; bt < buttonlabels.length ; bt++){
            if(bt%rows == 0){
                box1.add(Box.createHorizontalGlue());
                p1.add(box1);
                p1.setBorder(BorderFactory.createRaisedBevelBorder());
                p.add(p1);
                p1 = new JPanel();
                box1 = new Box(BoxLayout.X_AXIS);
                box1.add(Box.createVerticalStrut(50));
                box1.add(Box.createHorizontalGlue());
            }
            buttons[bt] = new JButton(buttonlabels[bt]);
            box1.add(buttons[bt]);
            box1.add(Box.createHorizontalStrut(15));

        }
        this.buttons = buttons;

        /*
        box1.add(Box.createHorizontalGlue());
        p1.add(box1);
        p1.setBorder(BorderFactory.createRaisedBevelBorder());
        p.add(p1);
         */

        getContentPane().add(p);
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        this.setVisible(true);

    }

    /*
    public Menu(String title, Game game, String[] buttonlabels){

        this.game = game;

        this.buttonlabels = buttonlabels;

        setTitle(title);

        JPanel p = new JPanel(new GridLayout(2, 3));

        JLabel label = new JLabel("You are in the main menu.");
        p.add(label);

        JPanel p1 = new JPanel();
        Box box1 = new Box(BoxLayout.X_AXIS);

        box1.add(Box.createVerticalStrut(100));
        box1.add(Box.createHorizontalGlue());
        box1.add(Box.createVerticalGlue());

        JButton[] buttons = new JButton[buttonlabels.length];

        for(int bt = 0 ; bt < buttonlabels.length ; bt++){
            buttons[bt] = new JButton(buttonlabels[bt]);
            box1.add(buttons[bt]);
            box1.add(Box.createHorizontalStrut(15));
        }
        this.buttons = buttons;

        box1.add(Box.createHorizontalGlue());
        p1.add(box1);
        p1.setBorder(BorderFactory.createRaisedBevelBorder());

        p.add(p1);

        getContentPane().add(p);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        this.setVisible(true);

        listen();


        //JButton b1 = new JButton("classical");
        //box1.add(b1);
        //box1.add(Box.createHorizontalStrut(15));
        //JButton b2 = new JButton("seirawan");
        //box1.add(b2);
        //box1.add(Box.createHorizontalStrut(30));
        //JButton b3 = new JButton("testing");
        //box1.add(b3);
        //box1.add(Box.createHorizontalStrut(15));
        //JButton b4 = new JButton("mouse test");
        //box1.add(b4);



        box1.add(Box.createHorizontalGlue());
        p1.add(box1);
        p1.setBorder(BorderFactory.createRaisedBevelBorder());

        p.add(p1);

        getContentPane().add(p);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        this.setVisible(true);

    }
     */

    void buttonPressed(){

    }

    void listen2(){

        for(int bt = 0 ; bt < buttons.length ; bt++){
            JButton b = buttons[bt];
            String label = buttonlabels[bt];
            JFrame thismenu = this;
            b.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (b.isEnabled()) {



                        if(title == "Main Menu"){
                            try {
                                game.game_running = true;
                                game.MenuReport(title,label);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        } else if(title == "Promotion"){

                            result = new String[2];
                            System.out.println(label);
                            read = true;
                            result[0] = title;
                            result[1] = label;
                            closeWindow();
                        }

                    }
                }
            });
        }
    }

    void closeWindow(){
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    @Override
    public void run() {
        listen2();
    }

    @Override
    public int read(CharBuffer charBuffer) throws IOException {
        if(!read) throw new IOException();
        return (int)(byte)(result[0].toCharArray()[0]);
    }

    /*
    void listen(){

        JButton b1 = buttons[0];
        JButton b2 = buttons[1];
        JButton b3 = buttons[2];
        JButton b4 = buttons[3];
        JButton b5 = buttons[4];

        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (b1.isEnabled()) {

                    System.out.println("Classical");

                    try {
                        game = new Game();
                        game.classical();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                }
            }
        });

        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (b2.isEnabled()) {

                    try {
                        game = new Game();
                        game.seirawan();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    System.out.println("Yasser is a legend");

                }
            }
        });

        b3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (b3.isEnabled()) {

                    System.out.println("testing");
                    try {
                        game = new Game();
                        game.testing();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                }
            }
        });


        b4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (b4.isEnabled()) {

                    System.out.println("mouse test");

                }
            }
        });

        b5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (b5.isEnabled()) {

                    System.out.println("plain board");

                    try {
                        game = new Game();
                        game.plainClassicBoard();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                }
            }
        });
    }
     */

}
