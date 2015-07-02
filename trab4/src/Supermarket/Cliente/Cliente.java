package Supermarket.Cliente;

import java.io.IOException;
import java.lang.String;
import java.lang.System;
import java.net.Socket;

public class Cliente {

    public static void main(String[] args) {
        try {
            Socket cliente = new Socket("127.0.0.1", 12345);
            System.out.println("Conexao aceita");

            Supermercado supermercado = new Supermercado(cliente);

            supermercado.menu1();

        }
        catch(IOException e) {
            System.out.println("Erro: conexao cliente");
        }
    }
}
