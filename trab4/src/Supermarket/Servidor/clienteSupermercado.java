package Supermarket.Servidor;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class clienteSupermercado implements Runnable {

    private Socket cliente;
    private BufferedReader entradaMsg;
    private ObjectInputStream entradaObject;
    private PrintStream saidaMsg;
    private ObjectOutputStream saidaObject;

    private String id;
    private int senha;

    @Override
    public void run() {
        menu();
    }

    public clienteSupermercado(Socket cliente) {
        this.cliente = cliente;
    }

    public void menu() {
        try {
            //recebe strings do cliente
            entradaMsg = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

            int opcao;
            //1 - cadastrar novo usuario
            //2 - login
            //3 - listar produtos
            //4 - requisitar produtos
            //5 - compra
            //0 - sair

            while(true) {
                //recebe o que o cliente deseja fazer
                opcao = Integer.parseInt(entradaMsg.readLine());

                switch (opcao) {
                    case 1:
                        cadastrarUsuario();
                        break;
                    case 2:
                        login();
                        break;
                    case 3:
                        listarProdutos();
                        break;
                    case 4:
                        requisitarProduto();
                        break;
                    case 5:
                        Comprar();
                        break;
                    case 0:
                        exit();
                        break;
                }
            }
        }
        catch (IOException e) {
        }
    }
    
    synchronized private void Comprar() {
        try {
            entradaMsg = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            saidaMsg = new PrintStream(cliente.getOutputStream());  
            
            String nome = entradaMsg.readLine();
           
            int qtd = Integer.parseInt(entradaMsg.readLine());
            
            Iterator teste = Servidor.produtos.iterator();
            while(teste.hasNext()){
                Produto p = (Produto)teste.next();
                if(p.getNome().equals(nome)) {                    
                    if(p.getQuantidade() >= qtd){
                        int a = p.getQuantidade() - qtd;
                        p.setQuantidade(a);
                        saidaMsg.println("true");
                    }
                }
            }
            saidaMsg.println("false");
            
            if(entradaMsg.readLine().equals("true")){
                teste = Servidor.usuarios.iterator();                    
                while(teste.hasNext()){
                    Usuario p = (Usuario)teste.next();
                    if(p.getID().equals(id)) {
                        PrintStream buffer = new PrintStream("notificar.csv");
                                
                        buffer.print(p.getEmail() + ",");
                        buffer.println(nome);                          

                        buffer.close();
                                              
                        break;                        
                    }
                }
            }
        }
        catch (IOException e) {
            System.out.println("Erro: enviar compra do usuario");
        }
    }

    private void cadastrarUsuario() {
        try {
            entradaObject = new ObjectInputStream(cliente.getInputStream());

            //recebe do cliente objeto usuario
            Usuario u = (Usuario) entradaObject.readObject();

            //adiciona a lista de usuarios
            Servidor.usuarios.add(u);

            //envia true para o cliente
            saidaMsg = new PrintStream(cliente.getOutputStream());
            saidaMsg.println("true");
        }
        catch (IOException e) {
            System.out.println("Erro: cadastrar usuario");
        }
        catch (ClassNotFoundException e) {
            System.out.println("Erro: cadastrar usuario");
        }
    }

    private void login() {
        try {
            entradaMsg = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            saidaMsg = new PrintStream(cliente.getOutputStream());

            //recebe do cliente id e senha
            String id = entradaMsg.readLine();
            this.id = id;

            int senha = Integer.parseInt(entradaMsg.readLine());
            this.senha = senha;
            
            Iterator teste = Servidor.usuarios.iterator();
                    
            while(teste.hasNext()){
                Usuario p = (Usuario)teste.next();
                if(p.getID().equals(id)) {
                    if(p.getSenha() == senha) {
                        if(!p.getConnected()) {
                            saidaMsg.println("true");
                            p.setConnected(true);
                        }
                        break;
                    }
                }
            }

            saidaMsg.println("false");
        }
        catch (IOException e) {
            System.out.println("Erro: cadastrar usuario");
        }
    }
    
    private void requisitarProduto() {
        try {
            entradaMsg = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            saidaMsg = new PrintStream(cliente.getOutputStream());  
            
            String nome = entradaMsg.readLine();
                      
            saidaMsg.println("true");
            
            Iterator teste = Servidor.usuarios.iterator();   
            
            while(teste.hasNext()){
                Usuario p = (Usuario)teste.next();
                if(p.getID().equals(id)) {
                    PrintStream buffer = new PrintStream("notificar.csv");
                                
                    buffer.print(p.getEmail() + ",");
                    buffer.println(nome);                          

                    buffer.close();
                                              
                    break;                        
                }
            }

        }
        catch (IOException e) {
            System.out.println("Erro: enviar requisitar produto");
        }
    }
    
    private void listarProdutos() {
        try {
            saidaObject = new ObjectOutputStream(cliente.getOutputStream());
            saidaObject.writeObject(Servidor.produtos);
        }
        catch (IOException e) {
            System.out.println("Erro: enviar lista produtos");
        }
    }

    private void exit() {
        try {
            cliente.close();
            
            Iterator teste = Servidor.usuarios.iterator();
                    
            while(teste.hasNext()){
                Usuario p = (Usuario)teste.next();
                if(p.getID().equals(id))
                    if(p.getSenha() == senha)
                            p.setConnected(false);                
            }

        }
        catch (IOException e) {
            System.out.println("Erro: exit");
        }
    }
}
