package quiz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends JFrame implements ActionListener {
    private Socket clientConnection;
    private BufferedReader input;
    private PrintWriter output;
    JTextField textField = new JTextField(10);
    JButton button= new JButton("button");
    String text;
    Client(String serverAddress, int port) {
        try {
            clientConnection = new Socket(serverAddress, port);
            input = new BufferedReader(new InputStreamReader(clientConnection.getInputStream()));
            output = new PrintWriter(clientConnection.getOutputStream(), true);
            setLayout(new FlowLayout());

            add(textField);
            add(button);
            button.addActionListener(this);
            textField.addActionListener(this);

            setSize(300,300);
            setDefaultCloseOperation(3);
            setVisible(true);

            startGame();
        } catch (IOException e) {
            System.out.println("Could not connect to server");
        }
    }

    public void startGame() throws IOException {
        String fromServer;

        fromServer = input.readLine();
        if (fromServer.startsWith("WELCOME")) {
            System.out.println("Welcome");
        }

        while (true) {
            fromServer = input.readLine();
            if (fromServer.startsWith("WAITING")) {
                System.out.println("Waiting on other player");
            } else if (fromServer.startsWith("QUESTION")) {
                fromServer = fromServer.substring(9);
                System.out.println("Question: " + fromServer);
            } else if (fromServer.startsWith("RESULT")) {
                fromServer = fromServer.substring(7);
                if (fromServer.equalsIgnoreCase("TRUE")) {
                    System.out.println("Correct answer");
                } else if (fromServer.equalsIgnoreCase("FALSE")) {
                    System.out.println("Incorrect answer");
                }
            } else if (fromServer.startsWith("MESSAGE")) {
                System.out.println(fromServer.substring(8));
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 4444);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button)
        {
           // textField.getAction();
            text = "MESSAGE ";
            text += textField.getText();
            System.out.println(text);
            output.println(text);
        }
        if(e.getSource() == textField){

        }
    }
}
