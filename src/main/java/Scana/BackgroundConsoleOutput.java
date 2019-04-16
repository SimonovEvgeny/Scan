package Scana;

public class BackgroundConsoleOutput implements java.lang.Runnable {
    private int counter = 0;

    @Override
    public void run() {

        while(true) {
            try {
                Thread.sleep(6000);
                System.out.println(".");
                counter++;
                    if(counter==9){
                        Thread.sleep(6000);
                        System.out.println("|");
                        counter=0;
                    }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
