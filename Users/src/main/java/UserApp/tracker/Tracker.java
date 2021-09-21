package UserApp.tracker;

import UserApp.model.User;
import UserApp.service.UserService;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

;

public class Tracker extends Thread{

    private Logger logger = LoggerFactory.getLogger(Tracker.class);
    // Changing the average tracking interval
    // private static final long trackingPollingInterval =
    // TimeUnit.MINUTES.toSeconds(0,5);

    private static final long trackingPollingInterval = TimeUnit.SECONDS.toSeconds(20);
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final UserService userService;
    private boolean stop = false;

    public Tracker(UserService userService) {
        this.userService = userService;
        executorService.submit(this);
        // I think that this lauch the run process in Java.
    }

    /**
     * Assures to shut down the Tracker thread
     */
    public void stopTracking() {
        stop = true;
        executorService.shutdownNow();
    }

    @Override
    public void run() {
        StopWatch stopWatch = new StopWatch();
        while (true) {
            if (Thread.currentThread().isInterrupted() || stop) {
                logger.info("Tracker stopping");
                break;
            }

            if(userService.lock.isLocked()) {

                try {
                    Thread.currentThread().wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            List<User> users = userService.users;
            logger.info("========Begin Tracker. Tracking " + users.size() + " users.");
            stopWatch.start();
            logger.info("Do we get here ? ");
            users.forEach(u -> userService.trackUserLocation(u));
            logger.info("I don't think we get till this points ? ");
            stopWatch.stop();
            logger.info("========Tracker Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
            stopWatch.reset();
            logger.info("" + trackingPollingInterval);
            try {
                logger.info("========Tracker sleeping");
                TimeUnit.SECONDS.sleep(trackingPollingInterval);
            } catch (InterruptedException e) {
                break;
            }
            logger.info("========LOLILOL");
        }

    }
}