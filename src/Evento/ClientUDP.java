package Evento;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;



public class ClientUDP {

    private static final String TERMINATE = "Exit";
    private static String name;
    private static volatile boolean finished = false;
    private static final int MAX_LEN = 1000;

    //criando hashmap p viabilizar o armazenamento do nome dos usuarios
    //em cada canal
    private static HashMap<String, ArrayList<String>> groupUsersMap = new HashMap<>();

    public static void main(String[] args) throws IOException {

        //criando uma socket associada ao processo do client UDP
        MulticastSocket publicoSocket = new MulticastSocket(1200);


        System.out.println("1. Ver atrações e dias \r\n" + "2. Visualizar promoções\r\n"
                + "3. Avisos gerais e regras\r\n" + "4. Tirar dúvidas no chat geral\r\n"
                + "5. Todos os anteriores\r\n" + "6. Terminar conexão\r\n"
                + "Selecione a opção desejada (1/2/3/4/5/6)");

        Scanner sc = new Scanner(System.in);
        //salvando na variável opção a escolha do usuário
        String opcao = sc.nextLine();

        //atribuindo um IP ao usuario, usando o 224.0.0.1 porque é para os dispositivos de uma mesma rede local
        InetAddress ipUsuario1 = InetAddress.getByName("224.0.0.1");
        //determinando a socket de cada escolha (grupos) selecionado dentro de um mesmo IP, o ipUsuario.
        InetSocketAddress grupo1 = new InetSocketAddress(ipUsuario1, 1200);
        NetworkInterface netInt1 = NetworkInterface.getByInetAddress(ipUsuario1);

        InetAddress ipUsuario2 = InetAddress.getByName("224.0.0.2");
        InetSocketAddress grupo2 = new InetSocketAddress(ipUsuario2, 1200);
        NetworkInterface netInt2 = NetworkInterface.getByInetAddress(ipUsuario2);

        InetAddress ipUsuario3 = InetAddress.getByName("224.0.0.3");
        InetSocketAddress grupo3 = new InetSocketAddress(ipUsuario3, 1200);
        NetworkInterface netInt3 = NetworkInterface.getByInetAddress(ipUsuario3);

        InetAddress ipUsuario4 = InetAddress.getByName("224.0.0.4");
        InetSocketAddress grupo4 = new InetSocketAddress(ipUsuario4, 1200);
        NetworkInterface netInt4 = NetworkInterface.getByInetAddress(ipUsuario4);



        System.out.println("Insira seu nome:");
        Scanner nameScanner = new Scanner(System.in);
        name = nameScanner.nextLine();
        nameScanner.close();


        sc.close();



        //adicionando os nomes dos grupos no mapa
        groupUsersMap.put("Grupo 1", new ArrayList<>());
        groupUsersMap.put("Grupo 2", new ArrayList<>());
        groupUsersMap.put("Grupo 3", new ArrayList<>());
        groupUsersMap.put("Grupo 4", new ArrayList<>());

        //criando if else pra lidar com a seleção do usuario
        if (opcao.charAt(0) == '1') {
            // ver a explicação certinha, tipo solicitar pra ativar aporta atraves da socket
            //multicast desse processo
            publicoSocket.joinGroup(grupo1, netInt1);

            //iniciando uma interface runnable pra receber a entrada do grupo 1
            //e assim iniciar uma thread pra ele
            Runnable receber = new messagesUDP(publicoSocket, "Grupo 1");
            //criando thread objects da clsse runnable messagesUDP

            Thread thread = new Thread(receber);
            thread.start();

        } else if (opcao.charAt(0) == '2') {
            publicoSocket.joinGroup(grupo2, netInt2);


            Runnable receber = new 	Evento.messagesUDP(publicoSocket, "Grupo 2");
            Thread thread = new Thread(receber);
            thread.start();

        } else if (opcao.charAt(0) == '3') {
            publicoSocket.joinGroup(grupo3, netInt3);


            Runnable receber = new Evento.messagesUDP(publicoSocket, "Grupo 3");
            Thread thread = new Thread(receber);
            thread.start();

        } else if (opcao.charAt(0) == '4') {
            publicoSocket.joinGroup(grupo4, netInt4);
            Runnable receber = new Evento.messagesUDP(publicoSocket, "Grupo 4");
            Thread thread = new Thread(receber);
            thread.start();

        } else if (opcao.charAt(0) == '5') {
            publicoSocket.joinGroup(grupo1, netInt1);
            publicoSocket.joinGroup(grupo2, netInt2);
            publicoSocket.joinGroup(grupo3, netInt3);
            publicoSocket.joinGroup(grupo4, netInt4);


            startChat(publicoSocket, grupo1);
            startChat(publicoSocket, grupo2);
            startChat(publicoSocket, grupo3);
            startChat(publicoSocket, grupo4);


            Runnable receberGrupo1 = new Evento.messagesUDP(publicoSocket, "Grupo 1");
            Runnable receberGrupo2 = new Evento.messagesUDP(publicoSocket, "Grupo 2");
            Runnable receberGrupo3 = new Evento.messagesUDP(publicoSocket, "Grupo 3");
            Runnable receberGrupo4 = new Evento.messagesUDP(publicoSocket, "Grupo 4");


            //inicializando as threads que serão utilizadas tendo em vista que não
            //foram inicializadas de forma individual previamente
            Thread threadGrupo1 = new Thread(receberGrupo1);
            Thread threadGrupo2 = new Thread(receberGrupo2);
            Thread threadGrupo3 = new Thread(receberGrupo3);
            Thread threadGrupo4 = new Thread(receberGrupo4);

            threadGrupo1.start();
            threadGrupo2.start();
            threadGrupo3.start();
            threadGrupo4.start();

        } else if (opcao.charAt(0) == '6') {
            System.out.println("Encerrando conexão...");
            finished = true;
            publicoSocket.close();
        }
    }

    private static void startChat(MulticastSocket socket, InetSocketAddress group) {
        try {
            //criando uma interface runnable para viabilizar a leitura de mensagens
            Runnable readMessages = new Runnable() {
                //sobrescrevendo a classe run da runnable
                public void run() {
                    try {
                        while (!finished) {
                            //criando espaço na memória pra guardar o datagram packet
                            byte[] buffer = new byte[MAX_LEN];
                            //criando o datagram e passandoos argumentos que ele precisa: buffer, tamanho
                            //e a socket do grupo escolhido
                            DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group.getAddress(), group.getPort());
                            socket.receive(datagram);
                            //passando de byte pra string
                            String message = new String(buffer, 0, datagram.getLength(), "UTF-8");

                            System.out.println(message);

                        }
                    } catch (IOException e) {
                        //informando que a socket solicitada não foi aberta
                        System.out.println("Socket está fechado!");
                    }
                }
            };

            // inicializando a thread responsável pela leitura dos inputs/mensagens
            Thread readThread = new Thread(readMessages);
            readThread.start();

            //inicializando  scanner para ler as mensagens em questão que serão recebidas na readThread
            Scanner sc = new Scanner(System.in);
            System.out.println("Digite sua mensagem: \n");
            while (true) {
                String message = sc.nextLine();
                if (message.equalsIgnoreCase(TERMINATE)) {
                    finished = true;
                    //saindo da socket do grupo selecionado e fechando a socket
                    socket.leaveGroup(group.getAddress());
                    socket.close();
                    break;
                }

                //definindo localdatetime pra ficar na formatação q setamos p mensagens do servidor
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
                String formattedDateTime = now.format(formatter);


                // setando que message vai ser composta pelo nome do cliente e pela mensagem inserida previamente
                message = "[" + formattedDateTime + "] " + name + ": " + message;
                byte[] buffer = message.getBytes();
                DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group.getAddress(), group.getPort());
                socket.send(datagram);
            }
            sc.close();
        } catch (IOException e) {
            System.out.println("Erro ao ler/escrever do/para o socket");
            e.printStackTrace();
        }
    }

}