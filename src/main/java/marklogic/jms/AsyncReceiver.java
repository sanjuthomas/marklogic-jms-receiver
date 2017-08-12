package marklogic.jms;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.MessageListener;
import javax.jms.JMSException;
import javax.jms.ExceptionListener;
import javax.jms.QueueSession;
import javax.jms.QueueReceiver;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;

/**
 * 
 * @author Sanju Thomas
 *
 */

public class AsyncReceiver implements MessageListener, ExceptionListener {

    public void onException(JMSException arg0) {
        // TODO Auto-generated method stub
        
    }

    public void onMessage(Message arg0) {
        // TODO Auto-generated method stub
        
    }

}
