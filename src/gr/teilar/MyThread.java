package gr.teilar;

public class MyThread implements Runnable {
    private Thread t;
    private String threadName;
    private int x;
    private double rTime;

    MyThread(String name, int x) {
        threadName = name;
        System.out.println("Creating " +  threadName );
        this.x = x;
    }

    public double getrTime() {
        return rTime;
    }

    public void run() {
        System.out.println("Running " +  threadName );
        int preSongs;
        int songsPerX;
        int songsPerMin;
        double rTime = 0;
        int i = 1;
        while(Main.songNum < Main.TotalSongs) {
            try {
                //System.out.println("Thread: " + threadName + ", " + i);
                //System.out.println(Main.songNum);
                preSongs = Main.songNum;
                Thread.sleep((1*1000)*x);
                //System.out.println(Main.songNum);
                songsPerX = Main.songNum - preSongs;
                songsPerMin = (int)(((double)songsPerX*60)/x);
                //System.out.println("Per x: " + Main.songNum + " - " + preSongs + " = " + songsPerX);
                //System.out.println("Per min: " + songsPerMin);
                //rTime += (double)(Main.TotalSongs-Main.songNum)/songsPerMin;
                //System.out.println(Main.TotalSongs + " / " + songsPerMin + " = " + Main.TotalSongs/songsPerMin);
                this.rTime = (double)(Main.TotalSongs-Main.songNum)/songsPerMin;//(rTime/i)-Main.elapsedMin;
                System.out.println("Remaining time: " + minsToHoursAndMins((int)this.rTime));
                //System.out.println("elapsed time: " + Main.elapsedMin);
                //i++;
            } catch (InterruptedException | ArithmeticException e) {
                System.out.println("Thread " +  threadName + " interrupted.");
            }
        }

        System.out.println("Thread " +  threadName + " exiting.");
    }

    public void start () {
        System.out.println("Starting " +  threadName );
        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }

    private static String minsToHoursAndMins(int mins) {
        return mins/60 + " hours and " + mins%60 + " minutes";
    }
}