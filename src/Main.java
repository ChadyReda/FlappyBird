import javax.swing.*;

public class Main {
    public static void main(String []args) {
        int width = 360;
        int height = 640;

        JFrame frame = new JFrame("Flappy Bird");
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Pannel FlappyBird = new Pannel();
        frame.add(FlappyBird);
        frame.pack();
        FlappyBird.requestFocus();
        frame.setVisible(true);

    }
}
       