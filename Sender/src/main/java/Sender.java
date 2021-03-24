import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;


public class Sender {

    private final static String brokerURL = "tcp://localhost:61616";
    private final static String prompt = String.join("\n",
            "--- Welcome to Lab1: Chatroom ---",
            "|1. Chat with a target			|",
            "|2. Send broadcast message		|",
            "|3. File transfer				|",
            "|4. Quit						|",
            "---------------------------------");
    public static String aIdentifier = "";
    public static Scanner input = null;
    public static void main(String[] args) throws Exception{
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
                ActiveMQConnectionFactory.DEFAULT_USER,
                ActiveMQConnectionFactory.DEFAULT_PASSWORD,
                brokerURL
        );

        Connection connection = factory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        System.out.println("成功连接中间件ActiveMQ Broker@" + brokerURL);
        input = new Scanner(System.in);
        System.out.println("输入本机标识：");
        aIdentifier = input.next();
        boolean sym = true;
        while (sym) {
            System.out.println(prompt);
            switch (input.nextInt()) {
                case 1:
                    chat(session, false);break;
                case 2:
                    chat(session, true);break;
                case 3:
                    break;//sendFile();break;
                case 4:
                    sym = false;
                    break;
            }
        }

/*
        //watchdog开启
        Watchdog wdSdr=new Watchdog();
        wdSdr.startSenderWatchdog(session, session.createProducer(session.createQueue("Watchdog")));
*/
        connection.close();
        System.out.println("Bye.");
    }
    public static void chat(Session session, boolean broadcast) {
        String aTarget = "", msg = "";
        int i = 0;
        try {
            Topic topic = session.createTopic("Message");
            MessageProducer producer = null;
            producer = session.createProducer(topic);
            if (!broadcast) {
                System.out.println("Input target ID:");
                aTarget = input.next();
            } else {
                aTarget = "ALL";
            }
            while (!(msg = input.next()).equals("OVER")) {
                i++;
                TextMessage textMessage = session.createTextMessage(msg);
                textMessage.setStringProperty("TO", aTarget);
                textMessage.setStringProperty("FROM", aIdentifier);
                producer.send(textMessage);
                System.out.printf("[%d]Sent @%s%n", i, new SimpleDateFormat("MM/dd HH:mm:ss").format(new Date()));
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
