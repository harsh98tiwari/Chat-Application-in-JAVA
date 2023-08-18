import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

class Server {
    ServerSocket server;
    Socket socket;

    // for reading and writting
    BufferedReader br;
    PrintWriter out;

    // constructor of our class
    public Server() {
        try {
            server = new ServerSocket(7987);
            System.out.println("Server is ready for connection...waiting...");
            socket = server.accept();
            // to read the data
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // to write the data
            out = new PrintWriter(socket.getOutputStream());
            // function to read and write
            startReading();
            startWritting();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void startReading() {
        // thread will read the data (with lambda expression)
        Runnable r1 = () -> {
            System.out.println("reader started...");
            try {
                while (true) {

                    String msg = br.readLine();
                    if (msg.equals("abort")) {
                        System.out.println("Client aborted the chat");
                        socket.close();
                        break;
                    }
                    System.out.println("Client : " + msg);
                }
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println("connection aborted");
            }

        };
        new Thread(r1).start();

    }

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

    public static void main(String f[]) {
        System.out.println("this is server class and it is goint to start...");
        new Server();
    }

}