// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  String loginid; //Exercise 3-(a)
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginid, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginid = loginid; //Exercise 3-(a)
    openConnection();
    sendToServer("#login " + loginid); //Exercise 3-(b)
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
	//Exercise 2-(a)
	if(message.charAt(0)=='#')
	{
		try
		{
			handleCommandsFromClients(message);	
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}
	
    try
    {
      sendToServer(message);
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  //Exercise 2-(a)
  private void handleCommandsFromClients(String commands) throws IOException
  {
	  String[] splittedCommands = commands.split("\\s+",2);
	  String mainCommand = splittedCommands[0];
	  String setCommand = splittedCommands[1];
	  
	  if(mainCommand == "#quit")
	  {
		quit();
	  }
	  
	  else if(mainCommand == "#logoff")
	  {
		closeConnection();
	  }
	  
	  else if(mainCommand == "#sethost")
	  {
		if(isConnected() == false)
		{
			String host = setCommand.replace("<", "").replace(">", "");
			setHost(host);
		}
		else
		{
			throw new IOException("You need to log off first.");
		}
	  }
	  
	  else if(mainCommand == "#setport")
	  {
		if(isConnected() == false)
		{
			String portNumber = setCommand.replace("<", "").replace(">", "");
			int port = Integer.parseInt(portNumber);
			setPort(port);
		}
		else
		{
			throw new IOException("You need to log off first.");
		}		  
	  }
	  
	  else if(mainCommand == "#login")
	  {
		  if(isConnected() == false)
		  {
			 openConnection();
		  }
		  else
		  {
			  throw new IOException("You need to log off first.");
		  }
	  }
	  
	  else if(mainCommand == "#gethost")
	  {
		  clientUI.display("The current host name: " + getHost());
	  }
	  
	  else if(mainCommand == "#getport")
	  {
		  clientUI.display("The current port number: " + getPort());
	  }
	  
	  else
	  {
		  throw new IOException("The command is invalid.");
	  }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  //Exercise 1-(a)
  protected void connectionClosed(boolean connectionOpen) {
	  if(connectionOpen != true)
	  {
		  System.exit(0);
	  }
  }
  
  //Exercise 1-(a)
  protected void connectionException(Exception exception) {
	  System.out.println("The serve has shut down");
	  connectionClosed(false);
  }
  
}
//End of ChatClient class
