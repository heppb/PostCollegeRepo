import java.util.Timer;
import java.util.TimerTask;

public class Timers extends TimerTask {
	
	TimerTask timerTask;
    Timer timer;
	public Timers()
	{
		timer = new Timer(true);
	}
	
	@Override
	public void run() {
		System.out.println("Test");
		startTimerTask();
		System.out.println("Test Over");
	}
	
	public void startTimerTask()
	{
		try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}
	
/*	public void startTimer()
	{
        timer.schedule(timerTask, 0);
        System.out.println("TimerTask started");
        //cancel after sometime
        timer.cancel();
        System.out.println("TimerTask cancelled");
	}
	public static void main(String[] args){
	
	}*/
}
