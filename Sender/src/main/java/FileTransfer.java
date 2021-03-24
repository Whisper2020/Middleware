import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.BlobMessage;
import org.apache.activemq.command.ActiveMQBlobMessage;
import org.apache.activemq.command.ActiveMQQueue;

import javax.jms.*;
import javax.swing.*;
import java.io.File;
import java.io.InputStream;

public class FileTransfer {
    public static void main(String[] args) {
        try {

            ActiveMQConnectionFactory factoryA = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_USER,
                    ActiveMQConnectionFactory.DEFAULT_PASSWORD,"tcp://127.0.0.1:61616?jms.blobTransferPolicy.defaultUploadUrl=http://localhost:8161/fileserver/");


            ActiveMQConnection conn = (ActiveMQConnection) factoryA.createConnection();

            conn.start();
            ActiveMQSession session = (ActiveMQSession) conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = new ActiveMQQueue("FileTransfer");
            MessageProducer producer = session.createProducer(queue);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("请选择要传送的文件");
            if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
                return;
            File file = fileChooser.getSelectedFile();
            BlobMessage blobMessage = session.createBlobMessage(file);
            blobMessage.setStringProperty("FILE.NAME", file.getName());
            blobMessage.setLongProperty("FILE.SIZE", file.length());
            System.out.println("开始发送文件：" + file.getName() + "，文件大小：" + file.length() + "字节");
            producer.send(blobMessage);
            System.out.println("文件发送完成。");
            producer.close();
            session.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
