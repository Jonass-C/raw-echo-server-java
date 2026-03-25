import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServidorTcp {

    private static final ServidorLog logger = new ServidorLog();

    public static void main(String[] args) {
        iniciarServidor();
    }

    public static void iniciarServidor() {
        logger.setupLogger();
        try {
            ServerSocket server = new ServerSocket(8080);
            logger.info("Servidor conectado na porta: " + server.getLocalPort());
            logger.info("Aguardando conexão do cliente...");

            ExecutorService executor = Executors.newFixedThreadPool(2); // fixa 2 para testes
            while (true) {
                Socket cliente = server.accept();
                SessaoCliente sessaoCliente = new SessaoCliente(cliente);
                executor.execute(sessaoCliente);
            }
        } catch (IOException e) {
            logger.warning("Não foi possível iniciar o servidor: " + e.getCause());
        }
    }
}
