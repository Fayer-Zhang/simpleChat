import java.io.*;
import common.*;
import java.util.Scanner;
import ocsf.server.*;

public class ServerConsole implements ChatIF{
	
	//Class variables *************************************************
	  
	/**
	 * The default port to connect on.
	 */
	final public static int DEFAULT_PORT = 5555;
	
	
	//Instance variables **********************************************
	EchoServer server;
	
	/**
	 * Scanner to read from the console
	 */
	//Scanner fromConsole;
	
	
	//Constructors ****************************************************
	public ServerConsole(int port)
	{
		
		server = new EchoServer(port,this);
		// Create scanner object to read from console
	    //fromConsole = new Scanner(System.in);
	}

	
	//Instance methods ************************************************
	public void accept() 
	{
		try
	    {
		  BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
	      String message;
	      
	      while (true) 
	      {
	        message = fromConsole.readLine();
	        server.handleMessageFromServerUI(message);
	      }
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println("Unexpected error while reading from console!");
	    }
	}
	
	/**
	 * This method overrides the method in the ChatIF interface.  It
	 * displays a message onto the screen.
	 *
	 * @param message The string to be displayed.
	 */
	public void display(String message) 
	{
		System.out.println("> " + message);
	}
	
	public static void main(String[] args) 
	{
	  int port = 0;
	    
	  try
	  {
	    port = Integer.parseInt(args[0]); //Get port from command line
	  }
	  catch(Throwable t)
	  {
	    port = DEFAULT_PORT; //Set port to 5555
	  }
	  
	  ServerConsole chat= new ServerConsole(port);
	  
	  try 
	  {
		  chat.server.listen(); //Start listening for connections
	  } 
	  catch (Exception ex) 
	  {
		  System.out.println("ERROR - Could not listen for clients!");
	  }
	  chat.accept();  //Wait for console data
	}
}