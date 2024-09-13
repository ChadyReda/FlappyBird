import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class Pannel  extends JPanel implements ActionListener, KeyListener{

    int width = 360;
    int height = 640;

    Image backgroundImage;
    Image birdImage;
    Image topPipeImage;
    Image bottomPipeImg;

    //Bird
    int birdX = width / 8;
    int birdY = height / 2;
    int birdWidth = 34;
    int birdHeight = 24;

    class Bird {
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image image;

        Bird(Image image){
            this.image = image;
        }
    }

    //Pipes
    int pipeX = width;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    class Pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        boolean passed = false;
        Image image;

        Pipe(Image image){
            this.image = image;
        }
    }



    //game logic
    Bird bird;
    int  VelocityY = 0;
    int  VelocityX = -4;
    int Gravity = 1;
    Timer gameLoop;
    Timer placePipesTimer;
    ArrayList<Pipe> pipes;


    public Pannel () {
        setPreferredSize(new Dimension(width, height));
        setFocusable(true);
        addKeyListener(this);

        //load images
        backgroundImage = new ImageIcon(getClass().getResource("./Ressources/flappybirdbg.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("./Ressources/flappybird.png")).getImage();
        topPipeImage = new ImageIcon(getClass().getResource("./Ressources/toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./Ressources/bottompipe.png")).getImage();

        //bird
        bird = new Bird(birdImage);
        //pipes
        pipes = new ArrayList<Pipe>();


        //place pipe timer
        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });

        gameLoop = new Timer(1000 / 60, this);
        placePipesTimer.start();
        gameLoop.start();

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        //background
        g.drawImage(backgroundImage, 0, 0, width, height, null);
        //bird
        g.drawImage(bird.image, bird.x, bird.y, bird.width, bird.height, null);

        for (Pipe pipe : pipes) {
            g.drawImage(pipe.image, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

    }

    public void move() {
        //bird
        VelocityY += Gravity;
        bird.y += VelocityY;
        bird.y = Math.max(bird.y, 5);

        //pipe
        for (Pipe pipe : pipes) {
            pipe.x += VelocityX;
        }

    }

    public void placePipes() {
        Pipe topPipe = new Pipe(topPipeImage);
        pipes.add(topPipe);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }


    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            VelocityY = -12;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
    
}
