package Supermarket.Cliente;

import java.io.IOException;
import java.lang.String;
import java.lang.System;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) {
        try {
            Scanner s = new Scanner(System.in);
            
            System.out.print("IP: ");
            String ip = s.nextLine();
            
            System.out.print("Porta: ");
            int porta = s.nextInt();
            
            Socket cliente = new Socket(ip, porta);
            System.out.println("Conexao aceita");

            Supermercado supermercado = new Supermercado(cliente);

            supermercado.menu1();

        }
        catch(IOException e) {
            System.out.println("Erro: conexao cliente");
        }
    }
}
