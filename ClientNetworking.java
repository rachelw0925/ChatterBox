import java.util.*;
import javax.swing.*;
import java.net.*;
import java.io.*;


public class ClientNetworking {
    private String name;
    private String ip;
    private int port;
	private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private MessageReceiver messageReceiver;
    private GWackClientGUI gui;
    private volatile boolean isConnected = false;

    public ClientNetworking(String name, String ip, int port, GWackClientGUI gui) {
    	try {
            socket = new Socket(ip, port);
            out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            isConnected = true;
            writeMsg("SECRET");
            writeMsg("3c3c4ac618656ae32b7f3431e75f7b26b1a14a87");
            writeMsg("NAME");
            writeMsg(name);
            out.flush();
            if (gui != null) {
            	gui.handleButton();
            }
            messageReceiver = new MessageReceiver(in, gui);
            messageReceiver.start();
        } 
        catch (IOException e) {
            JOptionPane.showMessageDialog(gui, "Cannot connect", "Connection Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public PrintWriter getOut() {
        return out;
    }
    
    public String getName() {
        return name;
    }

    public void writeMsg(String message) {
        if (isConnected) {
        	out.println(message);
            out.flush();
        }
    }

    public void disconnect() {
        try {
        	writeMsg("LOGOUT");
            out.flush();
            socket.close();

            isConnected = false;
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private class MessageReceiver extends Thread {
        private BufferedReader in;
        private GWackClientGUI gui;

        public MessageReceiver(BufferedReader in, GWackClientGUI gui) {
            this.in = in;
            this.gui = gui;
        }

        public void run() {
            try {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.equals("START_CLIENT_LIST")) {
                        String clientList = "";
                        while (!(inputLine = in.readLine()).equals("END_CLIENT_LIST")) {
                            clientList += inputLine + "\n";
                        }
                        gui.setMembersAreaText(clientList);
                    } 
                    else {
                        gui.newMessage(inputLine);
                    }
                }
            } 
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}