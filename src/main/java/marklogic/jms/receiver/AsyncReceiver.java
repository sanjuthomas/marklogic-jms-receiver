package marklogic.jms.receiver;

import java.util.Properties;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import marklogic.client.ClientProvider;
import marklogic.client.Writer;
import marklogic.client.WriterImpl;

/**
 * 
 * @author Sanju Thomas
 *
 */

public class AsyncReceiver implements MessageListener, ExceptionListener {
    
    private Writer writer;
    
    public AsyncReceiver(Properties properties){
        this.writer = new WriterImpl(new ClientProvider(properties));
    }

    public void onException(JMSException arg0) {}

    public void onMessage(Message m) {
        if (m instanceof TextMessage) {
            TextMessage message = (TextMessage) m;
            try {
                final String messageText = message.getText();
                if(null != messageText){
                    if(messageText.trim().startsWith("<")){
                        //write xml
                    }else if(messageText.trim().startsWith("{")){
                        //write json
                    }else{
                        //write text
                    }
                }
                
            } catch (JMSException e) {
                e.printStackTrace();
            }
        } 
    }
}
