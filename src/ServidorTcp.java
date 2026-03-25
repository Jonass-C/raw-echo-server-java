import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServidorTcp {

    private static final Logger logger = new Logger();

    public static void main(String[] args) throws IOException {
        iniciarServidor();
    }

    public static void iniciarServidor() throws IOException {
        try {
            ServerSocket server = new ServerSocket(8080);
            System.out.println("[" + logger.timestamp() + "] Servidor conectado na porta: " + server.getLocalPort());
            System.out.println("[" + logger.timestamp() + "] Aguardando conexão do cliente...");

            ExecutorService executor = Executors.newFixedThreadPool(2); // fixa 2 para testes
            while (true) {
                Socket cliente = server.accept();
                SessaoCliente sessaoCliente = new SessaoCliente(cliente);
                executor.execute(sessaoCliente);
            }
        } catch (IOException e) {
            System.out.println("[" + logger.timestamp() + "] Não foi possível iniciar o servidor: " + e.getCause());
        }
    }
}
