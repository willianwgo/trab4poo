package Supermarket.Servidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Servidor {
    public static List<Produto> produtos;
    public static List<Usuario> usuarios;

    public static void main(String[] args) {
        //cria lista de produtos e usuarios
        produtos = new ArrayList<Produto>();
        usuarios = new ArrayList<Usuario>();
        
        //carrega todos os produtos e usuarios para memoria
        carregarProdutos();
        carregarUsuarios();
        
        // Notifica os usuarios se o produto desejado esta disponivel
        notificaUsuario();

        try {
            System.out.print("Qual porta o servidor sera aberto? ");
            
            Scanner s = new Scanner(System.in);
            int porta = s.nextInt();
            
            ServerSocket server = new ServerSocket(porta);
            System.out.println("Servidor aberto");
            
            //objeto supermercado com tarefas relacionadas ao servidor
            servidorSupermercado supermercado = new servidorSupermercado();

            //inicia thread para as tarefas relacionadas ao servidor
            Thread threadServidor = new Thread(supermercado);
            threadServidor.start();

            while (true) {
                Socket cliente = server.accept();
                //objeto supermercado para tarefas relacionadas ao cliente
                clienteSupermercado cs = new clienteSupermercado(cliente);
                Thread threadCliente = new Thread(cs);
                threadCliente.start();
            }
        }
        catch (IOException e) {}

    }

    public static void carregarProdutos() {
        try {
            InputStream is = new FileInputStream("produtos.csv");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader buffer = new BufferedReader(isr);

            StringTokenizer st;
            Produto p;

            String str = buffer.readLine();

            while(str != null) {
                str = str + ",";
                st = new StringTokenizer(str, ",");

                p = new Produto(st.nextToken(), Double.parseDouble(st.nextToken()), st.nextToken(), st.nextToken());
                p.setQuantidade(Integer.parseInt(st.nextToken()));

                Servidor.produtos.add(p);

                str = buffer.readLine();
            }

            buffer.close();
        }
        catch (IOException e) {System.out.println("Erro: Carregar produto!");}
    }

    public static void carregarUsuarios() {
        try {
            InputStream is = new FileInputStream("usuarios.csv");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader buffer = new BufferedReader(isr);

            StringTokenizer st;
            Usuario u;

            String str = buffer.readLine();

            while(str != null) {
                str = str + ",";
                st = new StringTokenizer(str, ",");

                u = new Usuario(st.nextToken(), st.nextToken(), Integer.parseInt(st.nextToken()),
                                st.nextToken(), st.nextToken());
                u.setSenha(Integer.parseInt(st.nextToken()));

                Servidor.usuarios.add(u);

                str = buffer.readLine();
            }

            buffer.close();
        }
        catch (IOException e) {System.out.println("Erro: Carregar usuario!");}
    }

    public static void salvarProdutos() {
        try {
            PrintStream buffer = new PrintStream("produtos.csv");

               
            Iterator teste = Servidor.produtos.iterator();
                    
            while(teste.hasNext()){
                Produto p = (Produto)teste.next();        
                buffer.print(p.getProduto());
            }

            buffer.close();
        }
        catch (IOException e) {System.out.println("Erro: Salvar produto!");}
    }

    public static void salvarUsuarios() {
        try {
            PrintStream buffer = new PrintStream("usuarios.csv");

            Iterator teste = Servidor.usuarios.iterator();
                    
            while(teste.hasNext()){
                Usuario p = (Usuario)teste.next();
                buffer.print(p.getUsuario());
            }

            buffer.close();
        }
        catch (IOException e) {System.out.println("Erro: Salvar usuario!");}
    }
    
    public static void notificaUsuario() {
        try {
            InputStream is = new FileInputStream("notificar.csv");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader buffer = new BufferedReader(isr);

            StringTokenizer st;
            String nome;
            String email;

            String str = buffer.readLine();

            while(str != null) {
                str = str + ",";
                st = new StringTokenizer(str, ",");

                email = st.nextToken(); //nome produto indisponivel
                nome = st.nextToken();
                
                Iterator teste = Servidor.produtos.iterator();
                    
                while(teste.hasNext()){
                    Produto p = (Produto)teste.next();        
                    if(p.getNome().equals(nome)){
                        if(p.getQuantidade() != 0){
                            JavaMailApp.sendEmail(nome, email);
                        }
                    }
                }
                
                str = buffer.readLine();
            }

            buffer.close();
        }
        catch (IOException e) {System.out.println("Erro: Enviar email!");}
    }

    public static void sair() {
        System.out.println("Servidor fechado!");
        salvarProdutos();
        salvarUsuarios();
        System.exit(0);
    }

}
