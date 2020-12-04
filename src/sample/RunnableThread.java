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

        GspModel gsp = new GspModel();

    } public void start(){
        System.out.println("Starting" + threadName);
        if (t == null){
            t = new Thread(this, threadName);
            t.start();
        }
    }
}
