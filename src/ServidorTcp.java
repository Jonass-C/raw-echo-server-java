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
        ServerSocket server;
        ExecutorService executor;
        try {
            server = new ServerSocket(8080);
            logger.info("Servidor conectado na porta: " + server.getLocalPort());
            logger.info("Aguardando conexão do cliente...");

            executor = Executors.newFixedThreadPool(5);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("Iniciando desligamento gracioso do servidor...");
                try {
                    server.close();
                } catch (IOException e) {
                    logger.error("Erro ao fechar o server.");
                }
                executor.shutdown();
                logger.info("Servidor encerrado com segurança.");
            }));

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
