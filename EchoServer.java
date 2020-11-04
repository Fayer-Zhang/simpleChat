// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;
import common.*;  //Exercise 2-(b)
/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  ChatIF serverUI; //Exercise 2-(b)
  
  boolean serverIsConnected; //Exercise 2-(c)
  
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }
  
  //Exercise 2-(b)
  public EchoServer(int port, ChatIF serverUI) 
  {
	  super(port); //Call the superclass constructor
	  this.serverUI = serverUI;
  }
  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	
	//Exercise 3-(c)
	  boolean firstCommand = (boolean)client.getInfo("First command received after a client connects");
	  String commands = msg.toString();
	  String[] splittedCommands = commands.split("\\s+",2);
	  String mainCommand = splittedCommands[0];
	  
	  if(firstCommand == true){	
		
			client.setInfo("First command received after a client connects", false);
			
			if(mainCommand.equals("#login"))
			{
				String setCommand = splittedCommands[1];
				client.setInfo("loginID", setCommand);
			}
			
			else{
				try{
					client.sendToClient("You need to login first");
					client.close();
				}
				catch(IOException e){
				}
			}
		}
	  
		else{
			if(mainCommand.equals("#login")){
				try{
					client.sendToClient("You have already logged in");
				}
				catch(IOException e){
				}
			}
		    System.out.println("Message received: <" + msg + "> from <" + client.getInfo("loginID") + ">");
		    this.sendToAllClients((String)client.getInfo("loginID")+ ">"+ msg);
		}
    
  }
  
  
  //Exercise 2-(b)&(c)
  public void handleMessageFromServerUI(String message)
  {

	  if(message.charAt(0)=='#')
	  {
		try
		{
			handleCommandsFromServer(message);	
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	  }
		
	  else
	  {
		  serverUI.display("SERVER MSG>" + message);
		  sendToAllClients("SERVER MSG>" + message);
	  }
  }
  
  //Exercise 2-(c)
  private void handleCommandsFromServer(String command) throws IOException
  {
	  String[] commandArray = command.split(" ", 2);
	  String mainCommand = commandArray[0];	  
	  
	  if(mainCommand.equals("#quit"))
	  {
		  System.exit(0); 
	  }
	  else if(mainCommand.equals("#stop"))
	  {
		stopListening();
	  }

	  else if(mainCommand.equals("#close"))
	  {
		  close();
	  }

	  else if(mainCommand.equals("#setport"))
	  {
		if(serverIsConnected == false)
		{
			String setCommand = commandArray[1];
			String portNumber = setCommand.replace("<", "").replace(">", "");
			int port = Integer.parseInt(portNumber);
			
			setPort(port);
		}
		else
		{
			throw new IOException("You need to close the server first.");
		}		  
	  }

	  else if(mainCommand.equals("#start"))
	  {
		  if(serverIsConnected == false)
		  {
			 listen();
		  }
		  else
		  {
			  throw new IOException("You need to close the server first.");
		  }
	  }

	  else if(mainCommand.equals("#getport"))
	  {
		  serverUI.display("The current port number: " + getPort());
	  }

	  else
	  {
		  throw new IOException("The command is invalid.");
	  }
  }  
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
    serverIsConnected = true; //Exercise 2-(c)
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println("Server has stopped listening for connections.");
    serverIsConnected = false; //Exercise 2-(c)
  }
  
  //Class methods ***************************************************
  
  //Exercise 1-(c)
  protected void clientConnected(ConnectionToClient client) 
  {
	  System.out.println("A new client is attempting to connect to the server.");
	  System.out.println("Message received: #login <" + (String)client.getInfo("loginID") + "> from null." );
	  System.out.println("<" + (String)client.getInfo("loginID") + "> has logged on.");
	  client.setInfo("First command received after a client connects", true);
  }
  
  //Exercise 1-(c)
  synchronized protected void clientDisconnected(ConnectionToClient client) 
  {
	  System.out.println("WARNING - The server has stopped listening for connections");
	  System.out.println("SERVER SHUTTING DOWN! DISCONNECTING!");
	  System.out.println("Abnormal termination of connection.");
  }
  
  //Exercise 1-(c)
  synchronized protected void clientException(ConnectionToClient client, Throwable exception) 
  {
	  System.out.println((String)client.getInfo("loginID") + " has disconnected.");
  }
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
