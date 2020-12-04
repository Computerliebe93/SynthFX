package sample;

public class RunnableThread implements Runnable {
    boolean bingo = true;
    private Thread t;
    private String threadName;

    RunnableThread(String name) {
        threadName = name;
    }

    @Override
    public void run() {

        try {
            while (bingo) {
                for (int i = 1; i < 4; i++) {
                    System.out.println(threadName + "count: " + i);
                }
                Thread.sleep(50);
            }
            }catch(InterruptedException e){
        }
    } public void start(){
        System.out.println("Starting" + threadName);
        if (t == null){
            t = new Thread(this, threadName);
            t.start();
        }
    }
}
