package io.github.lucasduete.sd.protocol.client;

import io.github.lucasduete.sd.protocol.node.Protocol;
import io.github.lucasduete.sd.protocol.node.models.Message;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Loader {

    public static void main(String[] args) throws Exception {

        Protocol facade = new Protocol();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Digite o caminho para o seu arquivo de mensagens");
        String my = scanner.next();

        System.out.println("Digite o caminho para o arquivo de mensagens de seu amigo");
        String other = scanner.next();

        facade.startChat(my, other);
        System.out.printf("\n\n------CHAT INICIADO------\n\n");

        AtomicInteger count = new AtomicInteger(0);

        new Thread(() -> {
            while (true) {
                try {

                    List<Message> receive = facade.receive();

                    if (receive.size() > count.get()) {
                        Message message = receive.get(receive.size() - 1);

                        if (message.getEntregue() == null)
                            System.out.println("Parceiro: " + message.getBody());
                    }
                    count.set(receive.size());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();

        Message message;

        while (true) {
            message = new Message(scanner.next());
            System.out.println("Digite sua msg: ");
            System.out.println("Voce: " + message.getBody());
            facade.send(message);
        }

//        facade.disconnect();
    }

}