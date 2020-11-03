import java.io.*;
import common.*;
import java.util.Scanner;

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
	Scanner fromConsole;
	
	
	//Constructors ****************************************************
	public ServerConsole(int port)
	{
		try 
	    {
			server = new EchoServer(port, this);
	    }
	    
	    catch(IOException exception) 
	    {
	      System.out.println("Error: Can't setup connection!");
	      System.exit(1);
	    }
		
		// Create scanner object to read from console
	    fromConsole = new Scanner(System.in);
	}

	
	//Instance methods ************************************************
	public void accept() 
	{
		try
	    {

	      String message;

	      while (true) 
	      {
	        message = fromConsole.nextLine();
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
	  chat.accept();  //Wait for console data
	}
}