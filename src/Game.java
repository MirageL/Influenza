import java.awt.Dimension;

import javax.swing.JFrame;

import java.awt.Canvas;

public class Game extends Canvas implements Runnable{
    public static final int WIDTH = 270;
    public static final int HEIGHT = WIDTH/14*10;
    public static final int SCALE = 4;
    public static final String TITLE = "Influenza";

    private Thread thread;
    private boolean running = false;

    public Game() {
        Dimension size = new Dimension(WIDTH*SCALE,HEIGHT*SCALE);
        setPreferredSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
    }

    private synchronized void start() {
        if(running) return;
        running = true;
        thread = new Thread(this, "Thread");
        thread.start();
    };

    private synchronized void stop() {
        if(!running) return;
        running = false;
        try{
            thread.join();
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    };

    @Override
    public void run(){
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        double delta = 0;
        double ns = 1000000000.0/60.0;
        int frames = 0;
        int ticks = 0;
        
        while(running){
            long now = System.nanoTime();
            delta += (now-lastTime)/ns;
            lastTime = now;
            while(delta>=1){
                tick();
                ticks++;
                delta++;
            }
            render();
            frames++;
            if(System.currentTimeMillis()-timer>1000) {
                timer += 1000;
                System.out.println(frames + " Frames per second" + ticks + " Updates per second");
                frames = 0;
                ticks = 0;
            }
        }
    }

    public void render(){}

    public void tick(){}

    public static void main(String[] args){
        Game game = new Game();
        JFrame frame = new JFrame(TITLE);
        frame.add(game);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        game.start();
    }
}