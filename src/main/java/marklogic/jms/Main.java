package marklogic.jms;

/**
 * 
 * @author Sanju Thomas
 *
 */
public class Main {
    
    public static void main(String[] args) {
        
        if(args.length != 1){
            System.err.println("######################################");
            System.err.println("marklogic.jms.Main connection.properties");
            System.err.println("######################################");
            throw new IllegalArgumentException("Missing property file!");
        }
    }

}
