import java.io.*;
import java.net.*;
import java.util.*;

class TCPServer 
{
	Hashtable<String, Socket[]> map;
	ServerSocket welcomeSocket;
	TCPServer(int port_number)
	{
		try
		{
			welcomeSocket = new ServerSocket(port_number);
			map = new Hashtable<>();
		}
		catch(Exception e)
		{
			System.out.println("HEY");
		}		
	}
	
    public static void main(String argv[]) throws Exception
    {        
        TCPServer wa_server = new TCPServer(1234);
        while(true)
        {
            Socket conn_socket = wa_server.welcomeSocket.accept();
            (new ServerThread(conn_socket, wa_server)).start();
        }
    }
}


class ServerThread extends Thread
{
    Socket socket;
    Hashtable<String, Socket[]> user_info;
    ServerThread(Socket socket, TCPServer wa_server)
    {
        this.socket = socket;
        user_info = wa_server.map;
    }

    public void run()
    {
        try
        {
            while(true)
            {
                System.out.println("hihihihihihih");
                String serverSentence = "error";
                String clientSentence;
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                DataOutputStream  outToClient = new DataOutputStream(socket.getOutputStream());
                clientSentence = inFromClient.readLine();
                System.out.println(clientSentence);

                String[] split_clientSentence = clientSentence.split(" ");

                if(split_clientSentence[0].equals("REGISTER"))
                {
                    if(split_clientSentence.length>3)
                    {
                        serverSentence = "ERROR 100 Malformed username\n";
                    }
                    if(split_clientSentence[1].equals("TOSEND"))
                    {
                        String username = split_clientSentence[2];
                        if(isCorrectUsername(username))
                        {
                            serverSentence = "REGISTERED TOSEND " + username +'\n';
                            // Username and sockets stored in a Hashmap
                            // {username, rec_socket}
                            Socket[] sockets = new Socket[2];
                            sockets[0] = socket;
                            user_info.put(username, sockets);
                        }
                        else
                        {
                            serverSentence = "ERROR 100 Malformed username\n";
                        }
                    }
                    else if(split_clientSentence[1].equals("TORECV"))
                    {
                        String username = split_clientSentence[2];
                        if(isCorrectUsername(username))
                        {
                            serverSentence = "REGISTERED TORECV " + username +'\n';
                            Socket[] sockets1 = user_info.get(username);
                            sockets1[1] = socket;                        
                        }
                        else
                        {
                            serverSentence = "ERROR 100 Malformed username\n";
                        }
                    }
                    outToClient.writeBytes(serverSentence);
                }
                else if(split_clientSentence[0].equals("SEND"))
                {
                    // Reads the username from the message
                    String user_to_send = split_clientSentence[1];
                    System.out.println(user_to_send);

                    // Reads the content length from the message
                    clientSentence = inFromClient.readLine();
                    int content_length;
                    split_clientSentence = clientSentence.split(": ");
                    content_length = Integer.parseInt(split_clientSentence[1]);
                    System.out.println(content_length);

                    // Reads the message from the client
                    clientSentence = inFromClient.readLine();
                    clientSentence = inFromClient.readLine();
                    System.out.print(clientSentence);
                    
                    // Finds the username from the map formed
                    Socket[] sockets1 = user_info.get(user_to_send);
                    if(sockets1[1]!=null)
                    {
                        System.out.println("Sending Message");
                        Socket rec_socket_rec = sockets1[1];
                        DataOutputStream outToRecp = new DataOutputStream(rec_socket_rec.getOutputStream());
                        outToRecp.writeBytes(clientSentence);                    
                    }
                    else
                    {
                        outToClient.writeBytes("ERROR 101 Unable to send\n");
                    }

                }
                else
                {
                    serverSentence = "ERROR 100\n";
                    outToClient.writeBytes(serverSentence);
                }
                //System.out.println(serverSentence);
            }
            
        }
        catch(Exception e)
        {
            System.out.print("Error");
        }
    }

    public Boolean isCorrectUsername(String username)
    {
        return username.matches("[a-zA-Z0-9]+");
    }
}