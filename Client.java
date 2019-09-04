import java.io.*;
import java.net.*;
import java.util.*;

<<<<<<< HEAD
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
=======
    public static int port = 1234;
    public String userName = "";
    public static String hostIP = "localhost";
    public Socket clientSocketSen;
    public Socket clientSocketRec;

    public static void main(String argv[]) throws Exception
    {
        Client ob = new Client();
        ob.registerToSend();
        ob.registerToReceive();
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Do you wat to send or receive msg:");
        String option = inFromUser.readLine();
        if(option.equals("1")){
            System.out.println("To:");
            String to = inFromUser.readLine();
            System.out.println("Message:");
            String message = inFromUser.readLine();
            ob.sendMessage(to , message);
        }else{
            while(true){
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(ob.clientSocketRec.getInputStream()));
                String response = inFromServer.readLine();
                System.out.println(response);
            }

>>>>>>> 9bba4d6c261b61c68790037f7552df2e7a397002
        }
    }
}


<<<<<<< HEAD
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
=======
    private void sendMessage(String to , String message) throws Exception
    {
        DataOutputStream outToServer = new DataOutputStream(clientSocketSen.getOutputStream());
        System.out.println(clientSocketSen);
        outToServer.writeBytes("SEND " + to + "\nContent-length: " + message.length() + "\n\n" + message);
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocketSen.getInputStream()));
        String response = inFromServer.readLine();
        String[] splitRes = response.split(" ");
        if(!splitRes[0].equals("SENT")){
            if(splitRes[0].equals("ERROR") && splitRes[1] == "102"){
                System.out.println("Unable To Send ....");
            }else{
                System.out.println("Header Incomplete ....");
            }
        }else{
            System.out.println("Message Sent Successfully ....!!");
        }
    }

    private void registerToSend() throws Exception
>>>>>>> 9bba4d6c261b61c68790037f7552df2e7a397002
    {
        try
        {
            System.out.println("hihihihihihih");
            String serverSentence = "error";
            String clientSentence;
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream  outToClient = new DataOutputStream(socket.getOutputStream());
            clientSentence = inFromClient.readLine();
            System.out.println(clientSentence);

<<<<<<< HEAD
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
=======
        System.out.println("User Name:");
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        clientSocketSen = new Socket(hostIP, port);
        System.out.println(clientSocketSen);
        DataOutputStream outToServer = new DataOutputStream(clientSocketSen.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocketSen.getInputStream()));
        userName = inFromUser.readLine();
        outToServer.writeBytes("REGISTER TOSEND " + userName + "\n\n");
        String response = inFromServer.readLine();
        System.out.println(response);
        String[] splitRes = response.split(" ");
        if(!(splitRes[0].equals("REGISTERED") && splitRes[1].equals("TOSEND") && splitRes[2].equals(userName))){
            System.out.println("NOT A VALID USER NAME OR USERNAME ALREADY REGISTERED!!!");
            clientSocketSen.close();
            registerToSend();
        }
>>>>>>> 9bba4d6c261b61c68790037f7552df2e7a397002

            	// Reads the message from the client
            	clientSentence = inFromClient.readLine();
            	clientSentence = inFromClient.readLine();
            	System.out.print(clientSentence);
            }
            else
            {
                serverSentence = "ERROR 100\n";
            }
            //System.out.println(serverSentence);
            outToClient.writeBytes(serverSentence);
        }
        catch(Exception e)
        {
            System.out.print("Error");
        }
    }

<<<<<<< HEAD
    public Boolean isCorrectUsername(String username)
=======
    private void registerToReceive() throws Exception
>>>>>>> 9bba4d6c261b61c68790037f7552df2e7a397002
    {
        return username.matches("[a-zA-Z0-9]+");
    }
}

class Receiver extends Thread {

}