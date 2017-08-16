package marklogic.jms.receiver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marklogic.client.io.DOMHandle;
import com.marklogic.client.io.JacksonHandle;
import com.marklogic.client.io.StringHandle;

import marklogic.client.ClientProvider;
import marklogic.client.Writer;
import marklogic.client.WriterImpl;

/**
 * 
 * @author Sanju Thomas
 *
 */

public class AsyncReceiver implements MessageListener, ExceptionListener {
	
	private static final Logger logger = LoggerFactory.getLogger(AsyncReceiver.class);
	
	private final ObjectMapper MAPPER = new ObjectMapper();
    
    private Writer writer;
    
    public AsyncReceiver(Properties properties){
        this.writer = new WriterImpl(new ClientProvider(properties));
    }

    public void onException(JMSException e) {
    	logger.error(e.getMessage(), e);
    }

    public void onMessage(Message m) {
    	
        if (m instanceof TextMessage) {
            TextMessage message = (TextMessage) m;
            try {
                final String messageText = message.getText();
                if(null != messageText){
                    if(messageText.trim().startsWith("<")){
                    	writer.write(new DOMHandle(build(messageText)));
                    }else if(messageText.trim().startsWith("{")){
                        writer.write(new JacksonHandle(MAPPER.readTree(messageText)));
                    }else{
                    	writer.write(new StringHandle(messageText));
                    }
                }
            } catch (Exception e) {
            	logger.error(e.getMessage(), e);
            }
        } 
    }
    
    private Document build(String xml) throws ParserConfigurationException, SAXException, IOException{
    	
    	final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    	final DocumentBuilder builder = factory.newDocumentBuilder();
    	return builder.parse(new ByteArrayInputStream(xml.getBytes()));
    }
    
}
