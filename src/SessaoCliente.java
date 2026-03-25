import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SessaoCliente implements Runnable {

    private static final Logger logger = new Logger();
    private final Socket cliente;

    public SessaoCliente(Socket cliente) {
        this.cliente = cliente;
    }

    public void run() {
        String ipCliente = cliente.getInetAddress().getHostAddress();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            PrintWriter print = new PrintWriter(cliente.getOutputStream(), true);

            System.out.println("[" + logger.timestamp() + "] Nova conexão realizada!");
            System.out.println("[" + logger.timestamp() + "] Cliente conectado no IP: " + ipCliente);
            print.println("[" + logger.timestamp() + "] Olá! Seja bem-vindo ao servidor: " + cliente.getLocalPort());

            processarRequisicao(ipCliente, reader, print);

        } catch (IOException e) {
            System.out.println("[" + logger.timestamp() + "] Cliente " + ipCliente + " desconectou de forma abrupta.");
        } finally {
            try {
                cliente.close();
            } catch (IOException e) {
                System.out.println("[" + logger.timestamp() + "] Erro ao fechar o socket do cliente.");
            }
        }
    }

    private void processarRequisicao(String ipCliente, BufferedReader reader, PrintWriter print) throws IOException {
        boolean condicao = true;
        String inputTerminal;
        while (condicao && (inputTerminal = reader.readLine()) != null) {
            System.out.println("[" + logger.timestamp() + "] Requisição do cliente " + ipCliente + ": " + inputTerminal);

            String[] opcoes = {"sair", "exit"};
            for (String str : opcoes) {
                if (inputTerminal.equalsIgnoreCase(str)) {
                    condicao = false;
                    System.out.println("[" + logger.timestamp() + "] Cliente " + ipCliente + " se desconectou.");
                    print.println("[" + logger.timestamp() + "] Desconectando do servidor.");
                    break;
                }
            }
            if (condicao) {
                print.println("[" + logger.timestamp() + "] Requisição aceita pelo servidor.");
            }
        }
        if (condicao) {
            System.out.println("[" + logger.timestamp() + "] Cliente " + ipCliente + " desconectou de forma abrupta.");
        }
    }

}
