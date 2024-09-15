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
    int pipeGap = height / 4;
    String gameDifficulty = "easy";
    int Gravity = 1;
    int pipeDelay = 1500;
    Timer gameLoop;
    Timer placePipesTimer;
    ArrayList<Pipe> pipes;
    Random random = new Random();
    boolean gameOver = false;
    double gameScore = 0;

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

        placePipesTimer = new Timer(pipeDelay, new ActionListener() {
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
        placePipesTimer.setDelay(checkDifficulty(gameScore));
        draw(g);
    }

    public int checkDifficulty(double score) {
        if(gameScore > 5 && gameScore < 10){
            pipeDelay = 1200;
            gameDifficulty = "Normal";
        }else if(gameScore > 10 && gameScore < 20) {
            pipeDelay = 1000;
            gameDifficulty = "Hard";
        } else if (gameScore > 20) {
            pipeDelay = 800;
            gameDifficulty = "Impossible";
        }
        return 1500;
    }


    //this draws the components to the panel
    public void draw(Graphics g){
        //background
        g.drawImage(backgroundImage, 0, 0, width, height, null);
        //bird
        g.drawImage(bird.image, bird.x, bird.y, bird.width, bird.height, null);
        for (Pipe pipe : pipes) {
            g.drawImage(pipe.image, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }
        //showing the game score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if(!gameOver) {
            g.drawString("Your Score: " + String.valueOf((int) gameScore), 10, 35);
            g.drawString(gameDifficulty, 250, 35);
        }else {
            g.drawString("Game over press Space to reset", 10, 35);
        }
        //showing the game difficulty
    }

    //this method is responsible for moving our components
    public void move() {
        //moves the bird according to gravity and velocity
        VelocityY += Gravity;
        bird.y += VelocityY;
        bird.y = Math.max(bird.y, 5);

        //moves the pipes to the left
        for (Pipe pipe : pipes) {
            pipe.x += VelocityX;
            if (collision(bird, pipe)){
                gameOver = true;
            }
            if (!pipe.passed && bird.x > pipe.x + pipe.width){
                pipe.passed = true;
                gameScore += 0.5;
            }
        }

        // check if the bird touches the ground
        if (bird.y > height) {
            gameOver = true;
        }
    }

    public void placePipes() {
        int randomPipeY = (int)(pipeY - (double) pipeHeight / 4 - Math.random() * ((double) pipeHeight / 2));
        Pipe topPipe = new Pipe(topPipeImage);
        topPipe.y = randomPipeY;
        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + pipeGap;
        pipes.add(topPipe);
        pipes.add(bottomPipe);
    }

    public boolean collision(Bird b, Pipe p){
        return  b.x < p.x + p.width && // bird top left corner doesn't reach pipe top right corner
                b.x + b.width > p.x && // bird top right corner passes pipe top left corner
                b.y < p.y + p.height && // bird top left corner doesn't reach pipe bottom left corner
                b.y + b.height > p.y; // bird bottom left corner passes pipe top left corner
    }


    //the action performed 60 times a second
    @Override
    public void actionPerformed(ActionEvent e) {
        //check for gameOver
        if (gameOver) {
            gameLoop.stop();
            placePipesTimer.stop();
        }
        //this will cause our components to move
        move();
        // this will call paintComponent() method cause to JPanel Class
        repaint();
    }


    //this code runs whenever we press a Key on our keyboard
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            VelocityY = -9;
            if(gameOver){
                //restart by resting conditions
                gameDifficulty = "easy";
                gameOver = false;
                VelocityY = 0;
                pipes.clear();
                bird.y = birdY;
                gameScore = 0;
                gameLoop.start();
                placePipesTimer.start();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
    
}
