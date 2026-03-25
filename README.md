# 🖥️ Raw Echo Server Java

Um servidor TCP cru (Raw Sockets) construído com Java puro, sem utilização de frameworks externos.  
Este projeto foi desenvolvido a partir da curiosidade sobre fundamentos de redes e concorrência (Multithreading).

## 🚀 Tecnologias e Conceitos Aplicados

* **Java Core (SE):** Utilização pura da linguagem.
* **Sockets TCP/IP:** Conexões de rede via `Socket` e `ServerSocket`.
* **Thread Pool (ExecutorService):** Arquitetura escalável para processamento de múltiplos clientes simultâneos (evitando o anti-pattern de uma Thread por conexão).
* **Streams de I/O:** Leitura e escrita de dados utilizando `BufferedReader` e `PrintWriter`.
* **Observabilidade (JUL):** Sistema de logs customizado gravando em arquivo e console, utilizando `java.util.logging`.
* **Graceful Shutdown:** Implementação de `Shutdown Hooks` para interceptar sinais do Sistema Operacional (SIGINT) e encerrar conexões, liberar portas e desligar o Thread Pool com segurança.
* **Resiliência de Rede:** Defesa contra ociosidade utilizando *Timeouts* e tratamento defensivo contra quebras de conexão pelo cliente.

## ⚙️ Arquitetura do Sistema

O servidor escuta na porta `8080`. Ao receber uma nova conexão, o `ServerSocket` delega a sessão do cliente (`SessaoCliente`) para um `ExecutorService` (Thread Pool).

Se o número de clientes exceder o limite do Pool, os novos clientes são colocados em uma fila de espera segura no nível do Sistema Operacional até que uma Thread seja liberada. Clientes ociosos por mais de 60 segundos são automaticamente desconectados pelo servidor.

## 🛠️ Como Executar

### Pré-requisitos
* Java Development Kit (JDK) 11 ou superior.
* Um terminal com suporte a clientes TCP (como `telnet` ou `netcat`).

### Passo a Passo

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/Jonass-C/raw-echo-server-java.git
    cd raw-echo-server-java
    ```

2.  **Compile o projeto:**
    O comando abaixo compila todos os arquivos `.java` da pasta `src` e coloca os binários em uma pasta chamada `out`.
    ```bash
    javac src/*.java -d out/
    ```

3.  **Inicie o servidor:**
    Execute o programa a partir da raiz do projeto, indicando as classes na pasta `out` (flag `-cp` de classpath).
    ```bash
    java -cp out/ ServidorTcp
    ```

4.  **Conecte um cliente (em outro terminal):**
    ```bash
    telnet localhost 8080
    ```

5.  **Interação:**
    * Digite qualquer mensagem para enviá-la ao servidor.
    * Digite `sair` ou `exit` para encerrar a conexão de forma limpa.
    * No terminal do servidor, pressione `Ctrl+C` para observar o *Graceful Shutdown* em ação.

## 📜 Logs
O servidor gera automaticamente um arquivo chamado `servidor.log` na raiz do diretório. Ele funciona em modo *Append*, preservando o histórico de eventos de execuções anteriores.