package Supermarket.Cliente;

import Supermarket.Servidor.Servidor;
import Supermarket.Servidor.Usuario;
import Supermarket.Servidor.Produto;

import java.io.*;
import java.net.Socket;
import java.util.*;

//saida
//1 - cadastrar novo usuario
//2 - login
//3 - listar produtos
//4 - requisitar produtos
//5 - compra
//0 - sair

public class Supermercado {
    private Socket cliente;
    private boolean exit = false;
    private Scanner entradaTeclado;

    private BufferedReader entradaMsg;
    private ObjectInputStream entradaObject;
    private PrintStream saidaMsg;
    private ObjectOutputStream saidaObject;

    Supermercado(Socket cliente) {
        this.cliente = cliente;
    }

    public void menu1() {
        entradaTeclado = new Scanner(System.in);
        int opcao;

        while(!exit) {
            System.out.println("Selecione uma opcao:");
            System.out.println("1 - Cadastrar usuario");
            System.out.println("2 - Login");
            System.out.println("0 - Sair");

            opcao = entradaTeclado.nextInt();

            entradaTeclado.nextLine();  //limpar sujeira teclado

            switch (opcao) {
                case 1:
                    cadastrarUsuario();
                    break;
                case 2:
                    login();
                    break;
                case 0:
                    exit();
                    break;
            }
        }
    }

    public void cadastrarUsuario() {
        try {
            //envio para servidor 1 = cadastrar usuario
            saidaMsg = new PrintStream(cliente.getOutputStream());
            saidaMsg.println("1");

            entradaTeclado = new Scanner(System.in);

            System.out.print("Nome: ");
            String nome = entradaTeclado.nextLine();

            System.out.print("Endereco: ");
            String endereco = entradaTeclado.nextLine();

            System.out.print("Telefone: ");
            int telefone = entradaTeclado.nextInt();

            entradaTeclado.nextLine(); //limpar sujeira teclado

            System.out.print("Email: ");
            String email = entradaTeclado.nextLine();

            System.out.print("ID: ");
            String ID = entradaTeclado.nextLine();

            System.out.print("Senha: ");
            int senha = entradaTeclado.nextInt();

            Usuario u = new Usuario(nome, endereco, telefone, email, ID);
            u.setSenha(senha);

            //envio para servidor objeto usuario
            saidaObject = new ObjectOutputStream(cliente.getOutputStream());
            saidaObject.writeObject(u);

            //esperar true ou false do servidor
            entradaMsg = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            if(entradaMsg.readLine().equals("true"))
                System.out.println("Usuario cadastrado");
            else
                System.out.println("Erro ao cadastrar usuario");

        }
        catch (IOException e) {
            System.out.println("Erro: enviar objeto usuario");
        }
    }

    private void login() {
        try {
            entradaTeclado = new Scanner(System.in);
            entradaMsg = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            saidaMsg = new PrintStream(cliente.getOutputStream());

            //enviar para servidor 2 = fazer login
            saidaMsg.println("2");

            //recebe id do cliente e envia para servidor
            System.out.print("ID: ");
            String id = entradaTeclado.nextLine();
            saidaMsg.println(id);

            //recebe senha do cliente e envia para servidor
            System.out.print("senha: ");
            int senha = entradaTeclado.nextInt();
            saidaMsg.println(senha);

            //espera true ou false do servidor
            if(entradaMsg.readLine().equals("true"))
                menu2();
            else
                System.out.println("ID/senha invalida ou cliente ja conectado ao servidor!");

        }
        catch (IOException e) {
            System.out.println("Erro: login");
        }
    }
    
    public void listarProdutos() {
        try {
            //envio para servidor 3 = listar produtos
            saidaMsg = new PrintStream(cliente.getOutputStream());
            saidaMsg.println("3");

            //recebo do servidor lista produtos
            entradaObject = new ObjectInputStream(cliente.getInputStream());
            List<Produto> produtos = (ArrayList) entradaObject.readObject();

            System.out.println("Produto(s) em estoque:");
            produtos
                    .stream()
                    .filter(p -> p.getQuantidade() != 0)
                    .forEach(p -> System.out.println(p.getNome() + " R$" + p.getPreco() + " " +
                            p.getValidade() + " " + p.getFornecedor() + " " + p.getQuantidade()));

            System.out.println("\nProduto(s) esgotado(s):");
            produtos
                    .stream()
                    .filter(p -> p.getQuantidade() == 0)
                    .forEach(p -> System.out.println(p.getNome() + " R$" + p.getPreco() + " " +
                            p.getValidade() + " " + p.getFornecedor()));

            System.out.println();
            menu2();
        }
        catch (IOException e) {
            System.out.println("Erro: Listar produtos");
        }
        catch (ClassNotFoundException e) {
            System.out.println("Erro: cadastrar usuario");
        }
    }   

    private void menu2() {      
        entradaTeclado = new Scanner(System.in);
        int opcao;

        while(!exit) {
            System.out.println("Selecione uma opcao:");
            System.out.println("1 - Lista produto");
            System.out.println("2 - Requisitar produto");
            System.out.println("3 - Comprar");
            System.out.println("0 - Sair");

            opcao = entradaTeclado.nextInt();

            entradaTeclado.nextLine();  //limpar sujeira teclado

            switch (opcao) {
                case 1:
                    listarProdutos();
                    break;
                case 2:
                    requisitarProduto();
                    break;
                case 3:
                    Comprar();
                    break;
                case 0:
                    exit();
                    break;
            }
        }
        
    }
    
    private void Comprar() {
        try {
            entradaTeclado = new Scanner(System.in);
            entradaMsg = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            saidaMsg = new PrintStream(cliente.getOutputStream());
            saidaMsg.println("5");
           
            System.out.println("Nome do produto: ");
            String nome = entradaTeclado.nextLine();
            saidaMsg.println(nome);

            System.out.println("Quantidade: ");
            int qtd = entradaTeclado.nextInt();

            //enviar para servidor 2 = fazer login
            
            saidaMsg.println(qtd);
            
            //espera true ou false do servidor
            if(entradaMsg.readLine().equals("true")){
                System.out.println("Compra realizada com sucesso!");
                menu2();
            }
            else{
                System.out.println("Produto/Quantidade indisponível");
                System.out.println("Gostaria de receber uma notificação quando o produto estiver disponivel ? sim/nao"); 
                entradaTeclado.nextLine();  //limpar sujeira teclado
                String resposta = entradaTeclado.nextLine();
                
                if(resposta.equals("sim")){
                    saidaMsg.println("true");                    
                }else
                    saidaMsg.println("false");                 
            }
        }
        catch (IOException e) {
            System.out.println("Erro: enviar compra do usuario");
        }
    }
    
    private void requisitarProduto() {
        try {
            entradaTeclado = new Scanner(System.in);
            entradaMsg = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            saidaMsg = new PrintStream(cliente.getOutputStream());
            saidaMsg.println("4");
           
            System.out.println("Nome do produto: ");
            String nome = entradaTeclado.nextLine();
            saidaMsg.println(nome);
            
            //espera true ou false do servidor
            if(entradaMsg.readLine().equals("true")){
                System.out.println("Requisicao realizada!");
                menu2();
            }
            else
                System.out.println("Erro!");                
        }
        catch (IOException e) {
            System.out.println("Erro: enviar compra do usuario");
        }
    }

    private void exit() {
        exit = true;
        try {
            saidaMsg = new PrintStream(cliente.getOutputStream());
            saidaMsg.println('0');
        }
        catch (IOException e) {
            System.out.println("Erro: exit");
        }
    }
}
