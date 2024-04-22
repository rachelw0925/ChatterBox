import java.io.IOException;
import java.util.*;
import java.net.*;

public class GWackChannel {
	private ServerSocket serverSocket;
	private ArrayList<ClientThread> connectedClients;
    private Queue<String> outputQueue;
	
	public GWackChannel(int port) {
		try {
			serverSocket = new ServerSocket(port);
			connectedClients = new ArrayList<ClientThread>();
	        outputQueue = new LinkedList<>();

		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ServerSocket getServerSocket() {
		return serverSocket;
	}
	
	public ArrayList<ClientThread> getConnectedClients() {
		return connectedClients;
	}
    
    public Queue<String> getOutputQueue() {
        return outputQueue;
    }
    
    public synchronized void enqueue(String inputLine, String name) {
        String formattedMessage = "[" + name + "] " + inputLine;
        outputQueue.add(formattedMessage);
        broadcast();
    }
    
    public synchronized void enqueueClientNames() {
    	String clientList = getClientList();
        for (ClientThread client : connectedClients) {
            client.sendMessage(clientList);
        }
    }
    
    public synchronized void broadcast() {
    	while (!outputQueue.isEmpty()){
            String message = outputQueue.poll();
    		for (ClientThread client : connectedClients) {
                client.sendMessage(message);
            }
    	}
    }
   
    public void serve(int number) {
        if (number == -1) {
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    ClientThread clientThread = new ClientThread(clientSocket, this);
                    addClient(clientThread);
                    clientThread.start();
                } 
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } 
        else {
            for (int i = 0; i < number; i++) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    ClientThread clientThread = new ClientThread(clientSocket, this);
                    addClient(clientThread);
                    clientThread.start();
                } 
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
	public synchronized void addClient(ClientThread client) {
	    connectedClients.add(client);	
	}
	
	public String getClientList() {
	    String clientList = "START_CLIENT_LIST\n";
	    for (ClientThread clientThread : connectedClients) {
	        clientList += clientThread.getClientName() + "\n";
	    }
	    clientList += "END_CLIENT_LIST";
	    return clientList;
	}
	
    public void removeClients() {
    	Iterator<ClientThread> iterator = connectedClients.iterator();
        while (iterator.hasNext()) {
            ClientThread client = iterator.next();
            if (!client.isValid()) {
                iterator.remove();
            }
        }
        enqueueClientNames();
    }
	
    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        GWackChannel server = new GWackChannel(port);
        server.serve(-1);
    }   
		
}
