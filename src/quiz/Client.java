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
    private JTextField textField = new JTextField(10);
    private JButton button = new JButton("Category");
    private JButton button2 = new JButton("Send answer");
    private String text;

    Client(String serverAddress, int port) {
        try {
            clientConnection = new Socket(serverAddress, port);
            input = new BufferedReader(new InputStreamReader(clientConnection.getInputStream()));
            output = new PrintWriter(clientConnection.getOutputStream(), true);
            setLayout(new FlowLayout());

            add(textField);
            add(button);
            add(button2);
            button.addActionListener(this);
            button2.addActionListener(this);
            textField.addActionListener(this);

            setSize(300,300);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(3);
            setVisible(true);

            startGame();
        } catch (IOException e) {
            System.out.println("Could not connect to server");
        }
    }

    private void sendCategory(String category) {
        output.println("CATEGORY " + category);
    }

    public void startGame() throws IOException {
        String fromServer;

        fromServer = input.readLine();
        if (fromServer.startsWith("WELCOME")) {
            System.out.println("Welcome");
        }

        while (true) {
            fromServer = input.readLine();
            if (fromServer.startsWith("WAIT")) {
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
            } else if (fromServer.startsWith("CHOOSECATEGORY")) {
                System.out.println("Please choose a category");
            } else if (fromServer.startsWith("STARTROUND")) {
                output.println("STARTROUND");
            } else if (fromServer.startsWith("MESSAGE")) {
                System.out.println(fromServer.substring(8));
            } else if (fromServer.startsWith("ENDGAME")) {
                System.out.println("Game has ended");
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button)
        {
            // textField.getAction();
            text = "CATEGORY ";
            text += textField.getText();
            System.out.println(text);
            output.println(text);
        }
        if(e.getSource() == textField){

        }
        if (e.getSource() == button2) {
            text = "ANSWER ";
            text += textField.getText();
            output.println(text);
        }
    }

    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 4444);
    }
}
