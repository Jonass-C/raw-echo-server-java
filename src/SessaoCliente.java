import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SessaoCliente {

    private static final Logger logger = new Logger();

    public static void iniciarSessaoCliente(Socket cliente) {
        String ipCliente = cliente.getInetAddress().getHostAddress();

        Thread threadConexoes = new Thread(() -> {
            try {
                Scanner scan = new Scanner(cliente.getInputStream());
                PrintWriter print = new PrintWriter(cliente.getOutputStream(), true);

                System.out.println("[" + logger.timestamp() + "] Nova conexão realizada!");
                System.out.println("[" + logger.timestamp() + "] Cliente conectado no IP: " + ipCliente);
                print.println("[" + logger.timestamp() + "] Olá! Seja bem-vindo ao servidor: " + cliente.getLocalPort());

                processarRequisicao(ipCliente, scan, print);

            } catch (IOException e) {
                System.out.println("[" + logger.timestamp() + "] Cliente " + ipCliente + " desconectou de forma abrupta.");
            } finally {
                try {
                    cliente.close();
                } catch (IOException e) {
                    System.out.println("[" + logger.timestamp() + "] Erro ao fechar o socket do cliente.");
                }
            }
        });
        threadConexoes.start();
    }

    public static void processarRequisicao(String ipCliente, Scanner scan, PrintWriter print) {
        boolean condicao = true;
        while (condicao && scan.hasNextLine()) {
            String inputTerminal = scan.nextLine();
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
