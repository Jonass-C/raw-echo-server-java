import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorTcp {

    private static final SessaoCliente sessaoCliente = new SessaoCliente();
    private static final Logger logger = new Logger();

    public static void main(String[] args) throws IOException {
        iniciarServidor();
    }

    public static void iniciarServidor() throws IOException {
        try {
            ServerSocket server = new ServerSocket(8080);
            System.out.println("[" + logger.timestamp() + "] Servidor conectado na porta: " + server.getLocalPort());
            System.out.println("[" + logger.timestamp() + "] Aguardando conexão do cliente...");

            while (true) {
                Socket cliente = server.accept();
                SessaoCliente.iniciarSessaoCliente(cliente);
            }

        } catch (IOException e) {
            System.out.println("[" + logger.timestamp() + "] Não foi possível iniciar o servidor: " + e.getCause());
        }
    }
}
