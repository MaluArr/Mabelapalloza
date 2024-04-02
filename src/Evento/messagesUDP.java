package Evento;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;


public class messagesUDP implements Runnable {
    public MulticastSocket messageSocket;
    public String nomeGrupo;

    //construtor
    public messagesUDP(MulticastSocket messageSocket, String nomeGrupo) {
        this.messageSocket = messageSocket;
        this.nomeGrupo = nomeGrupo;
    }



    @Override
    //sobrescrevendo o void run da runnable pra poder implementar ela aqui
    public void run() {
        try {
            messagesUDP();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void messagesUDP() throws IOException {
        while (true) {
            System.out.println("[Cliente] Esperando por mensagem Multicast do grupo...");

            //criando um array "buffer" pra armazenar os dados recebidos do datagrampacket
            byte[] buffer = new byte[1024];
            //criando o datagram packet que é um pacote de dados UDP pegando o buffer e seu tamanho
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            //recebendo o pacote de dados UDP pelo socket
            //o programa só seguira se o datagram packet for recebido
            messageSocket.receive(packet);

            //convertendo de byte p string
            //get data retorna bytes recebidos e lenght retorna o comprimento dos dados
            //em seguida printamos a mensagem recebida
            String msg = new String(packet.getData(), 0, packet.getLength());
            System.out.println("[Cliente] Mensagem recebida do " + nomeGrupo + ": " + msg);
        }
    }
}