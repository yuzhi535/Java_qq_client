import java.io.IOException;
import java.net.Socket;

public class Client {
    Client(String url, int port) {
        try {
            Socket socket = new Socket(url, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
