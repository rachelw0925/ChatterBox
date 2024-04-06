import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.border.EmptyBorder;

public class GWackClientGUI extends JFrame {
	private JLabel name;
	private JTextField nameField;
	private JLabel ip;
	private JTextField ipField;
	private JLabel port;
	private JTextField portField;
	private JButton connect;
	private JButton disconnect;
	private JLabel members;
	private JTextArea membersArea;
	private JLabel messages;
	private JTextArea messagesArea;
	private JLabel compose;
	private JTextArea composeArea;
	private JButton send;
	private ClientNetworking client;
	
	public GWackClientGUI() {
		setInitialParams();
		makeLayout();
		setUpListeners();
	}
	
	private void setInitialParams() {
		name = new JLabel("Name  ");
		nameField = new JTextField(10);
		ip = new JLabel("IP Address  ");
		ipField = new JTextField(10);
		port = new JLabel("Port  ");
		portField = new JTextField(5);
		connect = new JButton("Connect");
		members = new JLabel("Members Online");
		membersArea = new JTextArea();
		membersArea.setEditable(false);
		messages = new JLabel("Messages");
		messagesArea = new JTextArea();
		messagesArea.setPreferredSize(new Dimension(300, 300));
		messagesArea.setEditable(false);
		compose = new JLabel("Compose");
		composeArea = new JTextArea();
		composeArea.setPreferredSize(new Dimension(100, 100));
		send = new JButton("Send");
	}
    
	public JTextField getNameField() {
        return nameField;
    }

    public JTextArea getMessagesArea() {
        return messagesArea;
    }
    
    public JTextArea getMembersArea() {
    	return membersArea;
    }
	
	private void makeLayout() {
		JPanel parametersPanel = new JPanel();
		parametersPanel.setLayout(new FlowLayout());
		parametersPanel.add(createPanel(name, nameField));
		parametersPanel.add(createPanel(ip, ipField));
		parametersPanel.add(createPanel(port, portField));
		parametersPanel.add(connect);
	    	
		JPanel membersPanel = new JPanel();
	    membersPanel.setLayout(new BorderLayout());
	    membersPanel.add(members, BorderLayout.NORTH);
	    membersPanel.add(membersArea, BorderLayout.CENTER);
	    
	    JPanel composePanel = new JPanel();
	    EmptyBorder border = new EmptyBorder(10, 10, 10, 10);
	    membersPanel.setBorder(border);
	    composePanel.setLayout(new BorderLayout());
	    composePanel.add(compose, BorderLayout.NORTH);
	    composePanel.add(composeArea, BorderLayout.CENTER);
	    
	    JPanel messagesPanel = new JPanel();
	    messagesPanel.setLayout(new BorderLayout());
	    messagesPanel.add(messages, BorderLayout.NORTH);
	    messagesPanel.add(messagesArea, BorderLayout.CENTER);
	    
	    JPanel center1Panel = new JPanel ();
	    center1Panel.setLayout(new BorderLayout());
	    center1Panel.add(messagesPanel, BorderLayout.NORTH);
	    center1Panel.add(composePanel, BorderLayout.CENTER);
	    
		JPanel centerPanel = new JPanel();
	    EmptyBorder border1 = new EmptyBorder(10, 10, 10, 10);
	    centerPanel.setBorder(border1);
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(membersPanel, BorderLayout.WEST);
		centerPanel.add(center1Panel, BorderLayout.CENTER);
	    
	    JPanel sendPanel = new JPanel();
	    EmptyBorder border2 = new EmptyBorder(10, 10, 10, 10);
	    sendPanel.setBorder(border2);
	    sendPanel.setLayout(new BorderLayout());
	    sendPanel.add(send, BorderLayout.EAST);
	    
	    add(parametersPanel, BorderLayout.NORTH);
	    add(sendPanel, BorderLayout.SOUTH);
	    add(centerPanel, BorderLayout.CENTER);
	}
	
	private JPanel createPanel(JLabel label, JTextField field) {
		JPanel createPanel = new JPanel(new BorderLayout());
		createPanel.add(label, BorderLayout.WEST);
		createPanel.add(field, BorderLayout.CENTER);
		return createPanel;
	}

	private void setUpListeners() {
	    connect.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        if (connect.getText().equals("Connect")) {
		            String portText = portField.getText();
		            int portNumber = -1;
		            try {
		                portNumber = Integer.parseInt(portText);
		                if (portNumber < 0 || portNumber > 65535) {
		                    throw new NumberFormatException();
		                }
		            } 
		            catch (NumberFormatException e1) {
		                JOptionPane.showMessageDialog(GWackClientGUI.this, "Invalid port number. Please enter a valid port between 0 and 65535.", "Connection Error", JOptionPane.ERROR_MESSAGE);
		                return;
		            }
		            connect.setText("Disconnect");
		    		nameField.setEditable(false);
		    		ipField.setEditable(false);
		    		portField.setEditable(false);
		            // Connect to server
		        } 
		        else {
		        	connect.setText("Connect");
		    		nameField.setEditable(true);
		    		ipField.setEditable(true);
		    		portField.setEditable(true);
		        }
		    }
		});
	    send.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e1) {
		    	//Send messaage to server;
		    }    
		});
	}
	

   	public static void main(String[] args) {
	   	GWackClientGUI frame = new GWackClientGUI();
	    frame.setTitle("GWack -- GW Slack Simulator");
	    frame.setSize(600, 500);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true);
	}	
	
}