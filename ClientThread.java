import java.net.*;
import java.io.*;
import java.util.*;

public class ClientThread extends Thread {
	private Socket socket;
	private GWackChannel server;
	private PrintWriter out;
	private BufferedReader in;
	private String name;
	public boolean valid = true;
	
	public ClientThread(Socket socket, GWackChannel server) {
		this.socket = socket;
		this.server = server;
		try {
            out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BufferedReader getIn() {
		return in;
	}
	
	public PrintWriter getOut() {
		return out;
	}
	
	public boolean isValid() {
		return valid;
	}
	
	public String getClientName() {
		return name;
	}
	
    public void sendMessage(String message) {
        out.println(message);
        out.flush();
    }
	
	public void run() {
		try {
			if(!in.readLine().equals("SECRET")) {
				socket.close();
				return;
			}
			if (!in.readLine().equals("3c3c4ac618656ae32b7f3431e75f7b26b1a14a87")) {
				socket.close();
				return;
			}
			if (!in.readLine().equals("NAME")) {
				socket.close();
				return;
			}
			name = in.readLine();
			server.enqueueClientNames();
			String inputLine;
			while (true) {
	            inputLine = in.readLine();
	            if (inputLine.equals("LOGOUT")) {
	                valid = false;
	                server.removeClients();
	                socket.close();
	                break;
	            }
	            server.enqueue(inputLine, name);
	        }
	    } 
		catch (Exception e) {
			valid = false;
			System.out.println("hi1");
			server.removeClients();
	        e.printStackTrace();	        
		}

	}
}
