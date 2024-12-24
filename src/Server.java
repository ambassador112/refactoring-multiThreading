import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT = 8080;
    private static final int THREAD_POOL_SIZE = 64;

    private final ExecutorService threadPool;

    public Server() {
        threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер работает по порту " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Подключен новый клиент: " + clientSocket.getInetAddress());

                threadPool.submit(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Ошибка на сервере: " + e.getMessage());
            e.printStackTrace();
        } finally {
            shutdown();
        }
    }

    private void handleClient(Socket clientSocket) {
        try (clientSocket) {
            System.out.println("Работа с клиентом: " + clientSocket.getInetAddress());

        } catch (IOException e) {
            System.err.println("Клиент для обработки ошибок: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void shutdown() {
        System.out.println("Завершение работы сервера...");
        threadPool.shutdown();
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}
