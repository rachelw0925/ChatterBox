import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.*;
import java.io.*;
import java.net.*;

public class Project2_Tester {

  @Test
  public void test1() {
    System.out.println("checking client constructor");

    ServerSocket serverSocket = null;

    try{
        serverSocket = new ServerSocket(2021);
    }
    catch (Exception e) {
        System.err.print("Could not open serverSocket");
        e.printStackTrace();
    }

    ClientNetworking c = new ClientNetworking("Jane", "localhost", 2021, null);

    assertEquals("/127.0.0.1", c.getSocket().getLocalAddress().toString());
    assertEquals(2021, c.getSocket().getPort());

    String line = "An exception happened.";
    try {
        Socket remote = serverSocket.accept();
        BufferedReader incoming = new BufferedReader(new InputStreamReader(remote.getInputStream()));
        line = incoming.readLine();
        line += incoming.readLine();
        line += incoming.readLine();
        line += incoming.readLine();
    }
    catch (Exception e){
    }
    assertEquals("SECRET3c3c4ac618656ae32b7f3431e75f7b26b1a14a87NAMEJane", line);

    try{
        serverSocket.close();
    } catch (Exception e){
        System.out.println("Error closing socket.");
    }
  }

  @Test
  public void test2() {
    System.out.println("checking client writing");

    ServerSocket serverSocket = null;

    try{
        serverSocket = new ServerSocket(2022);
    }
    catch (Exception e) {
        System.err.print("Could not open serverSocket");
        e.printStackTrace();
    }

    ClientNetworking c = new ClientNetworking("Jane", "localhost", 2022, null);
    c.writeMsg("hello");

    String line = "An exception happened.";
    try {
        Socket remote = serverSocket.accept();
        BufferedReader incoming = new BufferedReader(new InputStreamReader(remote.getInputStream()));
        line = incoming.readLine();
        line += incoming.readLine();
        line += incoming.readLine();
        line += incoming.readLine();
        line += incoming.readLine();
    }
    catch (Exception e){
    }
    assertEquals("SECRET3c3c4ac618656ae32b7f3431e75f7b26b1a14a87NAMEJanehello", line);

    try{
        serverSocket.close();
    } catch (Exception e){
        System.out.println("Error closing socket.");
    }
  }

  @Test
  public void test3() {
    System.out.println("checking client disconnect");

    ServerSocket serverSocket = null;

    try{
        serverSocket = new ServerSocket(2023);
    }
    catch (Exception e) {
        System.err.print("Could not open serverSocket");
        e.printStackTrace();
    }

    ClientNetworking c = new ClientNetworking("Jane", "localhost", 2023, null);
    assertEquals(false, c.getSocket().isClosed());
    c.disconnect();

    String line = "An exception happened.";
    try {
        Socket remote = serverSocket.accept();
        BufferedReader incoming = new BufferedReader(new InputStreamReader(remote.getInputStream()));
        line = incoming.readLine();
        line += incoming.readLine();
        line += incoming.readLine();
        line += incoming.readLine();
        line += incoming.readLine();
    }
    catch (Exception e){
    }
    assertEquals("SECRET3c3c4ac618656ae32b7f3431e75f7b26b1a14a87NAMEJaneLOGOUT", line);
    assertEquals(true, c.getSocket().isClosed());

    try{
        serverSocket.close();
    } catch (Exception e){
        System.out.println("Error closing socket.");
    }
  }

  @Test
  public void test4() {
    System.out.println("checking client recieve member list");

    ServerSocket serverSocket = null;

    try{
        serverSocket = new ServerSocket(2024);
    }
    catch (Exception e) {
        System.err.print("Could not open serverSocket");
        e.printStackTrace();
    }

    GWackClientGUI gui = new GWackClientGUI();
    ClientNetworking c = new ClientNetworking("Jane", "localhost", 2024, gui);


    String line = "An exception happened.";
    try {
        Socket remote = serverSocket.accept();
        PrintWriter pw = new PrintWriter(remote.getOutputStream());
        pw.println("START_CLIENT_LIST\nJane\nJohn\nRavi\nEND_CLIENT_LIST\n");
        pw.flush();
        pw.close();
    }
    catch (Exception e){
        e.printStackTrace();
        System.out.println("Error printwriter");
    }
    gui.pack(); // otherwise the change on the GUI will not render!
    assertEquals("Jane\nJohn\nRavi\n", gui.getMembersTextArea().getText());

    try{
        serverSocket.close();
    } catch (Exception e){
        System.out.println("Error closing socket.");
    }
  }

  @Test
  public void test5() {
    System.out.println("checking client recieve message");

    ServerSocket serverSocket = null;

    try{
        serverSocket = new ServerSocket(2025);
    }
    catch (Exception e) {
        System.err.print("Could not open serverSocket");
        e.printStackTrace();
    }

    GWackClientGUI gui = new GWackClientGUI();
    ClientNetworking c = new ClientNetworking("Jane", "localhost", 2025, gui);


    String line = "An exception happened.";
    try {
        Socket remote = serverSocket.accept();
        PrintWriter pw = new PrintWriter(remote.getOutputStream());
        pw.println("Hello, how great!\n And goodbye!\n");
        pw.flush();
        pw.close();
        Thread.sleep(500);
    }
    catch (Exception e){
        e.printStackTrace();
        System.out.println("Error printwriter");
    }
    gui.pack(); // otherwise the change on the GUI will not render!
    assertEquals("Hello, how great!\n And goodbye!\n\n", gui.getDisplayTextArea().getText());

    try{
        serverSocket.close();
    } catch (Exception e){
        System.out.println("Error closing socket.");
    }
  }

  @Test
  public void test6() {
    System.out.println("checking server constructor");

    GWackChannel server = new GWackChannel(2026);
    assertEquals(2026, server.getServerSocket().getLocalPort());
    assertEquals(0, server.getConnectedClients().size());
  }

  @Test
  public void test7() {
    System.out.println("checking server add and remove clients");

    try{
        GWackChannel server = new GWackChannel(2027);
        Socket socket1 = new Socket("localhost", 2027);
        Socket socket2 = new Socket("localhost", 2027);
        Socket socket3 = new Socket("localhost", 2027);
        ClientThread c1 = new ClientThread(socket1, server);
        ClientThread c2 = new ClientThread(socket2, server);
        ClientThread c3 = new ClientThread(socket3, server);

        server.addClient(c1);
        server.addClient(c2);
        server.addClient(c3);

        assertEquals(c1, server.getConnectedClients().get(0));
        assertEquals(c2, server.getConnectedClients().get(1));
        assertEquals(c3, server.getConnectedClients().get(2));
        assertEquals(3, server.getConnectedClients().size());

        c1.valid = false;
        c2.valid = true;
        c3.valid = false;
        server.removeClients();

        assertEquals(c2, server.getConnectedClients().get(0));
        assertEquals(1, server.getConnectedClients().size());

        c2.valid = false;
        server.removeClients();

        assertEquals(0, server.getConnectedClients().size());
    } catch (Exception e){
        e.printStackTrace();
        assertEquals("test7 passed", "Error in adding and removing clients");
    }
  }

  @Test
  public void test8() {
    System.out.println("checking server add and remove clients after interaction");

    try{
        GWackChannel server = new GWackChannel(2028);

        GWackClientGUI  gui1 = new GWackClientGUI();
        ClientNetworking cN1 = new ClientNetworking("Jane", "localhost", 2028, gui1);
        GWackClientGUI  gui2 = new GWackClientGUI();
        ClientNetworking cN2 = new ClientNetworking("John", "localhost", 2028, gui2);
        GWackClientGUI  gui3 = new GWackClientGUI();
        ClientNetworking cN3 = new ClientNetworking("Ravi", "localhost", 2028, gui3);

        server.serve(3);
        cN1.getOut().println("SECRET\n3c3c4ac618656ae32b7f3431e75f7b26b1a14a87\nNAME\n" + cN1.getName());
        cN1.getOut().flush();
        cN2.getOut().println("SECRET\n3c3c4ac618656ae32b7f3431e75f7b26b1a14a87\nNAME\n" + cN2.getName());
        cN2.getOut().flush();
        cN3.getOut().println("SECRET\n3c3c4ac618656ae32b7f3431e75f7b26b1a14a87\nNAME\n" + cN3.getName());
        cN3.getOut().flush();
        Thread.sleep(5);
        assertEquals("START_CLIENT_LIST\nJane\nJohn\nRavi\nEND_CLIENT_LIST", server.getClientList());
        
        cN1.writeMsg("red");
        Thread.sleep(5);

        assertEquals(0, server.getOutputQueue().size());

        cN1.writeMsg("LOGOUT");
        Thread.sleep(5);
        assertEquals("START_CLIENT_LIST\nJohn\nRavi\nEND_CLIENT_LIST", server.getClientList());

        cN2.writeMsg("LOGOUT");
        Thread.sleep(5);
        assertEquals("START_CLIENT_LIST\nRavi\nEND_CLIENT_LIST", server.getClientList());

        cN3.writeMsg("LOGOUT");
        Thread.sleep(5);
        assertEquals("START_CLIENT_LIST\nEND_CLIENT_LIST", server.getClientList());
    }
    catch (Exception e){
        e.printStackTrace();
        assertEquals("test8 passed", "error with test8");
    }
  }

  @Test
  public void test9() {
    System.out.println("checking server can chat with a single client");

    Process process = null;
    String command = "java GWackChannel 8083";
    try {
        process = Runtime.getRuntime().exec(command);

        GWackClientGUI  gui1 = new GWackClientGUI();
        ClientNetworking cN1 = new ClientNetworking("Jane", "localhost", 2030, gui1);
        cN1.getOut().println("SECRET\n3c3c4ac618656ae32b7f3431e75f7b26b1a14a87\nNAME\n" + cN1.getName());
        cN1.getOut().flush();
        cN1.writeMsg("red");
        cN1.writeMsg("yellow");
        cN1.writeMsg("green");
        Thread.sleep(500);
            
        String[] pieces = gui1.getDisplayTextArea().getText().split("\n");
        assertEquals("[Jane] green", pieces[pieces.length - 1]);
        assertEquals("[Jane] yellow", pieces[pieces.length - 2]);
        assertEquals("[Jane] red", pieces[pieces.length - 3]);

    } catch (Exception e){
        e.printStackTrace();
        assertEquals("passed test 9", "failed test 9");
    }
    process.destroy();
  }

  @Test
  public void test10() {
    System.out.println("checking server can chat with two clients");

    Process process = null;
    String command = "java GWackChannel 2030";
    try {
        process = Runtime.getRuntime().exec(command);

        GWackClientGUI  gui1 = new GWackClientGUI();
        ClientNetworking cN1 = new ClientNetworking("Jane", "localhost", 2030, gui1);
        cN1.getOut().println("SECRET\n3c3c4ac618656ae32b7f3431e75f7b26b1a14a87\nNAME\n" + cN1.getName());
        cN1.getOut().flush();
        Thread.sleep(500);

        GWackClientGUI  gui2 = new GWackClientGUI();
        ClientNetworking cN2 = new ClientNetworking("John", "localhost", 2030, gui2);
        cN2.getOut().println("SECRET\n3c3c4ac618656ae32b7f3431e75f7b26b1a14a87\nNAME\n" + cN2.getName());
        cN2.getOut().flush();
        Thread.sleep(500);

        cN1.writeMsg("red");
        Thread.sleep(500);
        cN2.writeMsg("yellow");
        Thread.sleep(500);
        cN1.writeMsg("green");
        Thread.sleep(500);
        cN1.writeMsg("pink");
        Thread.sleep(500);
        cN2.writeMsg("blue");
        Thread.sleep(500);

        String[] pieces = gui1.getDisplayTextArea().getText().split("\n");
        assertEquals("[John] blue", pieces[pieces.length - 1]);
        assertEquals("[Jane] pink", pieces[pieces.length - 2]);
        assertEquals("[Jane] green", pieces[pieces.length - 3]);
        assertEquals("[John] yellow", pieces[pieces.length - 4]);
        assertEquals("[Jane] red", pieces[pieces.length - 5]);

        pieces = gui2.getDisplayTextArea().getText().split("\n");
        assertEquals("[John] blue", pieces[pieces.length - 1]);
        assertEquals("[Jane] pink", pieces[pieces.length - 2]);
        assertEquals("[Jane] green", pieces[pieces.length - 3]);
        assertEquals("[John] yellow", pieces[pieces.length - 4]);
        assertEquals("[Jane] red", pieces[pieces.length - 5]);

    } catch (Exception e){
        e.printStackTrace();
        assertEquals("passed test 10", "failed test 10");
    }
    process.destroy();
  }
}
