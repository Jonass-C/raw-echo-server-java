import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class SessaoCliente implements Runnable {

    private static final ServidorLog logger = new ServidorLog();
    private final Socket cliente;

    public SessaoCliente(Socket cliente) {
        this.cliente = cliente;
    }

    public void run() {
        String ipCliente = cliente.getInetAddress().getHostAddress();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            PrintWriter print = new PrintWriter(cliente.getOutputStream(), true);

            logger.info("Nova conexão realizada!");
            logger.info("Cliente conectado no IP: " + ipCliente);
            print.println("Olá! Seja bem-vindo ao servidor: " + cliente.getLocalPort());

            cliente.setSoTimeout(60000);
            processarRequisicao(ipCliente, reader, print);

        } catch (SocketTimeoutException e) {
            logger.warning("Cliente " + ipCliente + " desconectado por inatividade!");
        } catch (IOException e) {
            logger.warning("Cliente " + ipCliente + " desconectou de forma abrupta.");
        } finally {
            try {
                cliente.close();
            } catch (IOException e) {
                logger.error("Erro ao fechar o socket do cliente.");
            }
        }
    }

    private void processarRequisicao(String ipCliente, BufferedReader reader, PrintWriter print) throws IOException {
        boolean condicao = true;
        String inputTerminal;
        while (condicao && (inputTerminal = reader.readLine()) != null) {
            logger.info("Requisição do cliente " + ipCliente + ": " + inputTerminal);

            String[] opcoes = {"sair", "exit"};
            for (String str : opcoes) {
                if (inputTerminal.equalsIgnoreCase(str)) {
                    condicao = false;
                    logger.info("Cliente " + ipCliente + " se desconectou.");
                    print.println("Desconectando do servidor.");
                    break;
                }
            }
            if (condicao) {
                print.println("Requisição aceita pelo servidor.");
            }
        }
        if (condicao) {
            logger.warning("Cliente " + ipCliente + " desconectou de forma abrupta.");
        }
    }

}
