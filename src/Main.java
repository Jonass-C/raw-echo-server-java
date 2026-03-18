import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        iniciarServidor();
    }

    private static void iniciarServidor() throws IOException {
        try {
            ServerSocket server = new ServerSocket(8080);
            System.out.println("[" + timestamp() + "] Servidor conectado na porta: " + server.getLocalPort());
            System.out.println("[" + timestamp() + "] Aguardando conexão do cliente...");

            while (true) {
                Socket cliente = server.accept();
                iniciarSessaoCliente(cliente);
            }

        } catch (IOException e) {
            System.out.println("[" + timestamp() + "] Não foi possível iniciar o servidor: " + e.getCause());
        }
    }

    private static void iniciarSessaoCliente(Socket cliente) {
        String ipCliente = cliente.getInetAddress().getHostAddress();

        Thread threadConexoes = new Thread(() -> {
            try {
                Scanner scan = new Scanner(cliente.getInputStream());
                PrintWriter print = new PrintWriter(cliente.getOutputStream(), true);

                System.out.println("[" + timestamp() + "] Nova conexão realizada!");
                System.out.println("[" + timestamp() + "] Cliente conectado no IP: " + ipCliente);
                print.println("[" + timestamp() + "] Olá! Seja bem-vindo ao servidor: " + cliente.getLocalPort());

                processarRequisicao(ipCliente, scan, print);

            } catch (IOException e) {
                System.out.println("[" + timestamp() + "] Cliente " + ipCliente + " desconectou de forma abrupta.");
            } finally {
                try {
                    cliente.close();
                } catch (IOException e) {
                    System.out.println("[" + timestamp() + "] Erro ao fechar o socket do cliente.");
                }
            }
        });
        threadConexoes.start();
    }

    private static void processarRequisicao(String ipCliente, Scanner scan, PrintWriter print) {
        boolean condicao = true;
        while (condicao) {
            String inputTerminal = scan.nextLine();
            System.out.println("[" + timestamp() + "] Requisição do cliente " + ipCliente + ": " + inputTerminal);

            String[] opcoes = {"sair", "exit"};
            for (String str : opcoes) {
                if (inputTerminal.equalsIgnoreCase(str)) {
                    condicao = false;
                    System.out.println("[" + timestamp() + "] Cliente " + ipCliente + " se desconectou.");
                    print.println("[" + timestamp() + "] Desconectando do servidor.");
                    break;
                }
            }
            if (condicao) {
                print.println("[" + timestamp() + "] Requisição aceita pelo servidor.");
            }
        }
    }

    private static String timestamp() {
        LocalDateTime timestamp = LocalDateTime.now();
        DateTimeFormatter formatado = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return timestamp.format(formatado);
    }

}