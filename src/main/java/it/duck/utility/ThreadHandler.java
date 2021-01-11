package it.duck.utility;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadHandler {

    private static final ArrayList<TimerTask> timers = new ArrayList<>();

    private static final ExecutorService service = Executors.newCachedThreadPool(new CustomThreadFactory());
    private static final Timer timer = new Timer(true);


    public static void startNewThread(Runnable run) {
        service.execute(run);
    }

    public static void registerNewTimer(TimerTask timerTask, long delay, long period) {
        timers.add(timerTask);

        if(period > 0) {
            timer.schedule(timerTask, delay, period);
        } else {
            timer.schedule(timerTask, delay);
        }
    }

    public static void stopEverything() {
        stopService(service);

        timer.cancel();
        for(TimerTask timerTask : timers) {
            timerTask.cancel();
        }
    }


    private static void stopService(ExecutorService service) {
        new Thread(() -> {
            service.shutdown();
            try {
                if (!service.awaitTermination(60, TimeUnit.SECONDS)) {
                    service.shutdownNow();
                    if (!service.awaitTermination(60, TimeUnit.SECONDS)) {
                        System.err.println("Service did not terminate");
                    }
                }
            } catch (InterruptedException e) {
                service.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}
