package bomberman.logic;

import bomberman.entities.GameBoard;

import java.util.Calendar;

public class GameTime implements Runnable {
    private long counter;
    private Calendar dateTime;

    public GameTime() {
        setCounter(0L);
        this.dateTime = Calendar.getInstance();
        Thread thread = new Thread(this);
        thread.start();
    }

    public long getCounter() {
        return this.counter;
    }

    public void setCounter(long counter) {
        this.counter = counter;
    }

    public synchronized void run() {
        while (!GameBoard.getInstance().isPaused()) {
            try {
                wait(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            this.counter += this.dateTime.get(Calendar.SECOND) - Calendar.getInstance().get(Calendar.SECOND);
            this.dateTime = Calendar.getInstance();
        }
    }
}