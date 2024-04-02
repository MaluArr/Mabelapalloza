package Evento;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
//import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
//import java.net.NetworkInterface;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerUDP {


    //iniciando a main usando IOException pra tratar possíveis exceções
    public static void main(String[] args) throws IOException {
        boolean ativo = true;
        String mensagem = " ";
        String opcao = " ";
        String conteudo = " ";
        byte[] envio = new byte[1024];
        Scanner sc = new Scanner(System.in);
        //criando uma instancia da classe messagesUDP


        MulticastSocket serverSocket = new MulticastSocket();
        InetAddress grupo1 = InetAddress.getByName("224.0.0.1");
        InetAddress grupo2 = InetAddress.getByName("224.0.0.2");
        InetAddress grupo3 = InetAddress.getByName("224.0.0.3");
        InetAddress grupo4 = InetAddress.getByName("224.0.0.4");

        // Código pra receber a hashlist criada em client aqui no servidor
        MulticastSocket hashListSocket = new MulticastSocket(1201);
        InetAddress hashListGroup = InetAddress.getByName("224.0.0.5");
        hashListSocket.joinGroup(hashListGroup);

        byte[] receiveBuffer = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        hashListSocket.receive(receivePacket);
        String receivedData = new String(receivePacket.getData(), 0, receivePacket.getLength());

        // separando as linhas recebidas pra receber 2 elementos diferentes, nome e grupo
        String[] hashList = receivedData.split(",");
        HashMap<String, ArrayList<String>> groupUsersMap = new HashMap<>();
        for (String hash : hashList){
            String[] parts = hash.split(":");
            String groupName = parts[0];
            String users = parts[1];
            String[] userList = users.split(";");
            ArrayList<String> userListArray = new ArrayList<>();
            for (String user : userList) {
                userListArray.add(user);
            }
            groupUsersMap.put(groupName, userListArray);
        }

        // Print the received hash list
        System.out.println("Received Hash List:");
        for (Map.Entry<String, ArrayList<String>> entry : groupUsersMap.entrySet()) {
            System.out.println("Grupo: " + entry.getKey() + ", Usuários: " + entry.getValue());
        }

        hashListSocket.leaveGroup(hashListGroup);
        hashListSocket.close();

        while(ativo == true){
            System.out.print("[MabelaPalooza] Digite a opção desejada:");
            opcao = sc.nextLine();
            System.out.print("[MabelaPalooza] Digite o conteúdo da mensagem:");
            conteudo = sc.nextLine();
            System.out.println(opcao);

            //olhando somente a primeira posição do input do usuário pra saber o que
            //ele escolheu
            if(opcao.charAt(0) == '1') {

                LocalDateTime agora = LocalDateTime.now();
                DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
                String dataHoraFormatada = agora.format(formato);

                mensagem = "[" + dataHoraFormatada + "] Atrações e dias: " + "dia 13/09: 19hr-Chimbinha, 20hr-Imagine Dragons, 21hr-Adelle, 22hr-One Direction"
                +"dia 14/09: 19hr-Taylor Swift, 20hr-Kelvis Duran, 21hr-Stray Kids, 22hr-Matuê part TETO" + "dia 15/09: 19hr-Aviões do forro, 20hr-Raphaela Santos PART Melody, 21hr-Beyonce, 22hr-Alceu Valença";

                envio = mensagem.getBytes();

                DatagramPacket pacote = new DatagramPacket(envio, envio.length,grupo1, 1200);
                serverSocket.send(pacote);


            }else if(opcao.charAt(0) == '2') {

                LocalDateTime agora = LocalDateTime.now();
                DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
                String dataHoraFormatada = agora.format(formato);

                mensagem = "[" + dataHoraFormatada + "] Visualizar promoções: " + "Estadia no hotel IBIS com 30% de desconto"
                +"30 Galões brahma por 50 reais" + "Open de fofão burguer para os primeiros 100 ingressos";

                envio = mensagem.getBytes();

                DatagramPacket pacote = new DatagramPacket(envio, envio.length,grupo2, 1200);
                serverSocket.send(pacote);


            }else if(opcao.charAt(0) == '3') {

                LocalDateTime agora = LocalDateTime.now();
                DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
                String dataHoraFormatada = agora.format(formato);

                mensagem = "[" + dataHoraFormatada + "] Avisos gerais e regras: " + "1. É estritamente proibido o porte e uso de armas de qualquer tipo no evento"
                +"Será barrado qualquer cliente que traga bebida/comida de fora, exceção de água"+
                "pode levar capa de chuva e bolsas transparentes. guarda chuvas no entanto serão proibidos.";

                envio = mensagem.getBytes();

                DatagramPacket pacote = new DatagramPacket(envio, envio.length,grupo3, 1200);
                serverSocket.send(pacote);


            }else if(opcao.charAt(0) == '4') {

                LocalDateTime agora = LocalDateTime.now();
                DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
                String dataHoraFormatada = agora.format(formato);

                //AQUI QUE PRECISAM CONVERSAR ENTRE SI
                mensagem = "[" + dataHoraFormatada + "] Tirar dúvidas no chat geral: " + conteudo;

                envio = mensagem.getBytes();

                DatagramPacket pacote = new DatagramPacket(envio, envio.length,grupo4, 1200);
                serverSocket.send(pacote);

            } else if (opcao.charAt(0) == '5') {

                LocalDateTime agora = LocalDateTime.now();
                DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
                String dataHoraFormatada = agora.format(formato);


                mensagem = "[" + dataHoraFormatada + "] Todas as anteriores: " + conteudo;

                envio = mensagem.getBytes();
                DatagramPacket pacote1 = new DatagramPacket(envio, envio.length, grupo1, 1200);
                serverSocket.send(pacote1);

                envio = mensagem.getBytes();
                DatagramPacket pacote2 = new DatagramPacket(envio, envio.length, grupo2, 1200);
                serverSocket.send(pacote2);

                envio = mensagem.getBytes();
                DatagramPacket pacote3 = new DatagramPacket(envio, envio.length, grupo3, 1200);
                serverSocket.send(pacote3);

                envio = mensagem.getBytes();
                DatagramPacket pacote4 = new DatagramPacket(envio, envio.length, grupo4, 1200);
                serverSocket.send(pacote4);
            } else if (opcao.charAt(0) == '6') {
                System.out.println("[MabelaPalooza] Encerrando conexão...");
                mensagem = "MabelaPalooza Inativo!";
                envio = mensagem.getBytes();
                DatagramPacket pacote = new DatagramPacket(envio, envio.length, grupo1, 1200);
                serverSocket.send(pacote);
                ativo=false;
                break;
            } else {
                System.out.println("Opção inválida. Tente novamente.");
            }
        }
        ativo = false;
        System.out.print("[MabelaPalooza] Multicast Encerrado");
        serverSocket.close();
        sc.close();
    }
}



