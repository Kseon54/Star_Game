package star_game.game.utils;

public class Timer {
    private long lastTime;
    private double ticks;
    private double ns;
    private double delta;

    public Timer(double ticks) {
        lastTime = System.nanoTime();
        this.ticks = ticks;
        ns = 1000000000 / ticks;
        delta = 0;
    }

    public void setTicks(double ticks){
        this.ticks = ticks;
    }


    public boolean isItTime() {
        long now = System.nanoTime();
        delta += (now - lastTime) / ns;
        lastTime = now;
        if (delta >= 1) {
            delta--;
            return true;
        }
        return false;
    }
}
