package Swing.code.snakegame;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.awt.event.*;

class SnakeScore implements Serializable {
    int score;
    public SnakeScore(){  score = 0; }
    int getScore(){   return this.score;    }
    void setScore(int score) {  this.score = score;   }
}

public class SnakeGame1 extends JFrame implements ActionListener, KeyListener{

    //All variables
    LinkedList<Integer> snakeX, snakeY;
    LinkedList<JLabel> snake;
    int x = 80, y = 60;
    JPanel box;
//    Timer timer;
    long speed = 150;

    ImageIcon bodyIcon = new ImageIcon("G:/InteliJ 2021/src/Swing/code/snakegame/images/body5.png");
    ImageIcon headIconDown = new ImageIcon("G:/InteliJ 2021/src/Swing/code/snakegame/images/hdown.png");
    ImageIcon headIconRight = new ImageIcon("G:/InteliJ 2021/src/Swing/code/snakegame/images/hright.png");
    ImageIcon headIconLeft = new ImageIcon("G:/InteliJ 2021/src/Swing/code/snakegame/images/hleft.png");
    ImageIcon headIconUp = new ImageIcon("G:/InteliJ 2021/src/Swing/code/snakegame/images/hup.png");
//    ImageIcon closmouthIcon = new ImageIcon("G:/InteliJ 2021/src/Swing/code/snakegame/images/closmouth.png");

    // Food
    int foodX = 100, foodY = 100;

    ImageIcon foodIcon = new ImageIcon("G:/InteliJ 2021/src/Swing/code/snakegame/images/food.png");
    JLabel food = new JLabel(foodIcon);
    javax.swing.Timer timer;

    // Store snake highest  score
    SnakeScore score = new SnakeScore();
    private SnakeGame1() {
        super("Snake Game ");
        BorderLayout borderLayout = new BorderLayout(10, 10);
        this.setLayout(borderLayout);
        this.setBackground(Color.cyan);
        this.setBounds(10, 40, 1350, 720);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // Creating box
        box = new JPanel();
        box.setLayout(null);
        box.setBackground(Color.getHSBColor(120, 50, 50));
        Color color = new Color(169, 245, 27, 255);
        box.setBackground(color);

        this.add(box, BorderLayout.CENTER);

        // Header
        JPanel header = new JPanel();
        header.setPreferredSize(new Dimension(100, 60));
        header.setBackground(Color.YELLOW);
        this.add(header, BorderLayout.NORTH);

        //  Bottom panel
        JPanel footer = new JPanel();
        footer.setPreferredSize(new Dimension(100, 40));
        footer.setBackground(Color.BLUE);
        this.add(footer, BorderLayout.SOUTH);

        // right
        JPanel board = new JPanel();
        board.setPreferredSize(new Dimension(300, 40));
        board.setBackground(new Color(205, 45, 246));
        this.add(board, BorderLayout.EAST);

        initGame();
        printSnake();
        foodPrint();

        timer = new javax.swing.Timer((int)speed, this);

        box.setFocusable(true);
        box.addKeyListener(this);

        this.validate();
        box.validate();
    }

    boolean isGameOver;

    @Override
    public void actionPerformed(ActionEvent event){
        System.out.println("Event Listener");
        switch (key){
            case "R":  // Right move
                    x += 20;
                    break;
            case "L":   // LEft move
                    x -= 20;
                    break;
            case "U":   // Up Move
                    y -= 20;
                    break;
            case "D":   // Down move
                    y += 20;
                    break;
        }
        // reload
        reloadPosition(x, y);
        printSnake();
        if (snakeX.getFirst() == foodX && snakeY.getFirst() == foodY){
            foodGenerate();
            foodPrint();

            // Create new body and set icon
            snake.addLast(new JLabel(bodyIcon, SwingConstants.CENTER)); //add in snake
            snake.getLast().setBounds(snakeX.getLast(), snakeY.getLast(), 20, 20);
            box.add(snake.getLast());  // add on box panel

            snakeX.addLast(snakeX.getLast()); // add snake X and last
            snakeY.addLast(snakeY.getLast());


        }
        snake.getFirst().setIcon(icon);

        // Game over snippets
        isGameOver =   box.getHeight() - (box.getHeight() % 10) == y + 20 || box.getWidth() - (box.getWidth() % 10) == x - 20 || x == -20 || y == -20;

        if(isGameOver){
            JLabel gameLabel =new JLabel("Game over");
            JOptionPane.showConfirmDialog(this, "Do you want to play again.", "Snake Game", JOptionPane.YES_NO_OPTION);

            gameLabel.setBounds(300, 250 ,200, 100);
            gameLabel.setBackground(Color.pink);
            gameLabel.setFont(new Font("Arial", Font.BOLD, 38));
            gameLabel.setForeground(Color.RED);
            box.add(gameLabel);
            timer.stop();
            box.revalidate();

            if(snake.size() - 3 > score.getScore()){
                score.setScore(snake.size() - 3);
                try {
                    writeScore();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("score.getScore() -------------------------------- >  "+score.getScore());
    }


    void writeScore() throws FileNotFoundException {
        try{
            FileOutputStream fileOutputStream = new FileOutputStream("src/Swing/code/snakegame/ScoreFile/Score.ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(score);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    void readScore()  {
        try{
            FileInputStream fileInputStream = new FileInputStream("src/Swing/code/snakegame/ScoreFile/Score.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            score = (SnakeScore) objectInputStream.readObject();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void keyTyped(KeyEvent event){}
    @Override
    public void keyReleased(KeyEvent event){}

    String key = "R";
    boolean isStart = false;

    ImageIcon icon;
    @Override
    public void keyPressed(KeyEvent event){
            System.out.println("KeyEvent Trigger");
            int keyCode = event.getKeyCode();
            if(keyCode == KeyEvent.VK_UP){
                key = "U";
                icon = headIconUp;
//                snake.getFirst().setIcon(headIconRight);
            }
            else if(keyCode == KeyEvent.VK_DOWN){
                key = "D";
                icon = headIconDown;
            }
            else if(keyCode == KeyEvent.VK_LEFT){
                key = "L";
                icon = headIconLeft;
            }
            else if(keyCode == KeyEvent.VK_RIGHT){
                key = "R";
                icon = headIconRight;
            }
            else if(keyCode == KeyEvent.VK_SPACE){
                if(!isStart){
                    timer.start();
                    isStart = true;
                }
                else{
                    timer.stop();
                    isStart = false;
                }
                if(isGameOver){
                    System.out.println("888888888888888888 ---------------------------- >>>>>> ");
                    for (JLabel jLabel : snake) {
                        box.remove(jLabel);
                    }

                    box.remove(food);
                    box.removeAll();
                    this.validate();
                    box.validate();


                    initGame();
                    printSnake();
                    foodPrint();
                }
            }
        snake.getFirst().setIcon(icon);
    }

    void foodGenerate(){
        Random rand = new Random();
        int width = box.getWidth() - (box.getWidth() % 10);
        int height = box.getHeight() - (box.getHeight() % 10);
        foodX = rand.nextInt(width);
        foodY = rand.nextInt(height);
        while(foodX % 20 != 0  || foodY % 20 != 0){
            foodX = rand.nextInt(width);
            foodY = rand.nextInt(height);
            if (foodX % 20 == 0 && foodY % 20 == 0){
                break;
            }
        }
        System.out.println("---------- >                    Food X "+foodX+" food Y "+foodY);
        System.out.println("---------- >                    Food X "+box.getPreferredSize().getWidth()+" food Y "+box.getWidth());
        System.out.println("---------- >                    Food X "+box.getPreferredSize().getHeight()+" food Height "+box.getHeight());
        System.out.println("---------- >                    Height "+height+" food Width "+width);
    }
    void foodPrint(){
        food.setLocation(foodX, foodY);
    }

    void reloadPosition(int x1, int y1){
        snakeX.addFirst(x1);
        snakeX.removeLast();
        snakeY.addFirst(y1);
        snakeY.removeLast();
//        System.out.println(snakeX+" "+snakeY);

    }
    void printSnake(){
        for(int i = 0; i < snake.size(); i++){
            snake.get(i).setLocation(snakeX.get(i), snakeY.get(i));
        }
    }

    void  initGame(){
        List<Integer> list = Arrays.asList(80, 60, 40);
        List<Integer> list1 = Arrays.asList(60, 60, 60);
        snakeX = new LinkedList<>(list);
        snakeY = new LinkedList<>(list1);
        snake = new LinkedList<>();

        x = 80;
        y = 60;
        for(int i = 0; i < 3; i++){
            snake.add(new JLabel(bodyIcon,  SwingConstants.CENTER));
            snake.get(i).setBackground(Color.RED);
            snake.get(i).setBounds(snakeX.get(i), snakeY.get(i), 20,20);
            box.add(snake.get(i));
        }
        icon  = headIconRight;
        snake.getFirst().setIcon(headIconRight);
        // add food
        food.setBounds(foodX, foodY, 20, 20);
        food.setIcon(foodIcon);
        box.add(food);
        // read Score from file
        readScore();
    }

//    JPanel header;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->{
            new SnakeGame1().setVisible(true);
        });
    }
}
