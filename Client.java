import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Client extends JFrame {
    Socket socket;

    // for reading and writting
    BufferedReader br;
    PrintWriter out;
    // Declare components
    private JLabel heading = new JLabel("Client Window");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    // Conctructor
    public Client() {
        try {
            System.out.println("sneding request to server...");
            socket = new Socket("127.0.0.1", 7987);
            System.out.println("Connection established..");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // // to write the data
            out = new PrintWriter(socket.getOutputStream());
            // // function to read and write
            createGUI();
            handleEvents();
            startReading();
            // startWritting();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                if (e.getKeyCode() == 10) {
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me :" + contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }

        });

    }

    private void createGUI() {
        this.setTitle("Client Messenger Window");
        this.setSize(500, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        // coding for components
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        // heading.setIcon(new ImageIcon("chaapplogo.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        messageArea.setEditable(false);

        // frame layout
        this.setLayout(new BorderLayout());

        // adding components to frame
        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    public void startReading() {
        // thread will read the data (with lambda expression)
        Runnable r1 = () -> {
            System.out.println("reader started...");
            try {
                while (true) {

                    String msg = br.readLine();
                    if (msg.equals("abort")) {
                        System.out.println("Server aborted the chat");
                        JOptionPane.showMessageDialog(this, "Server terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    // System.out.println("Server : " + msg);
                    messageArea.append("Server : " + msg + "\n");

                }
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println("connection aborted");
            }

        };
        new Thread(r1).start();

    }

    // this will be of no use after we have created the gui
    public void startWritting() {
        // thread will take data from the user and send it to client
        Runnable r2 = () -> {
            System.out.println(" writer started...");
            try {
                while (!socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();
                    if (content.equals("abort")) {
                        socket.close();
                        break;
                    }
                }
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println("connection aborted");
            }
        };
        new Thread(r2).start();

    }

    public static void main(String h[]) {
        System.out.println("this is client...");
        new Client();
    }
}
