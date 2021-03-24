import javax.jms.*;
import java.util.concurrent.*;

/**
 * @author Ruzhen Zhang
 */
public class Watchdog {

    private ScheduledExecutorService scheduler= Executors.newScheduledThreadPool(1);

    public void startSenderWatchdog(Session session, MessageProducer producer){

        Runnable runnable = () -> {
            TextMessage textMessage = null;
            try {
                textMessage = session.createTextMessage("PING");
                producer.send(textMessage);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        };
        //延迟执行时间（秒）
        long delay = 0;
        //执行的时间间隔（秒）
        long period = 1;
        scheduler.scheduleAtFixedRate(runnable, delay, period, TimeUnit.SECONDS);
    }


}
