package Supermarket.Servidor;

import java.util.*;

public class servidorSupermercado implements Runnable {

    private Scanner entrada;

    @Override
    public void run() {
        menu();
    }

    public void menu() {
        System.out.println("Selecione uma opcao:");
        System.out.println("1 - Cadastrar produto");
        System.out.println("2 - Listar produtos");
        System.out.println("3 - Listar usuarios");
        System.out.println("4 - Atualizar estoque");        
        System.out.println("0 - Sair");

        entrada = new Scanner(System.in);
        int i = entrada.nextInt();

        switch (i) {
            case 1:
                cadastrarProduto();
                break;
            case 2:
                listarProdutos();
                break;
            case 3:
                listarUsuarios();
                break;
            case 4:
                atualizarProdutos();
                break;
            case 0:
                Servidor.sair();
                break;
        }

    }

    public void cadastrarProduto() {
        entrada = new Scanner(System.in);

        System.out.print("Produto: ");
        String nome = entrada.nextLine();

        System.out.print("Preco (xx,xx): ");
        double preco = entrada.nextDouble();

        entrada.nextLine(); //limpar sujeira teclado
        System.out.print("Validade (dd/mm/aaaa): ");
        String validade = entrada.nextLine();

        System.out.print("Fornecedor: ");
        String fornecedor = entrada.nextLine();

        System.out.print("Quantidade: ");
        int quantidade = entrada.nextInt();

        Produto p = new Produto(nome, preco, validade, fornecedor);
        p.setQuantidade(quantidade);

        Servidor.produtos.add(p);

        menu();
    }

    public void listarProdutos() {
        System.out.println("Produto(s) em estoque:");
        Servidor.produtos
                .stream()
                .filter(p -> p.getQuantidade() != 0)
                .forEach(p -> System.out.println(p.getNome() + " R$" + p.getPreco() + " " +
                            p.getValidade() + " " + p.getFornecedor() + " " + p.getQuantidade()));

        System.out.println("\nProduto(s) esgotado(s):");
        Servidor.produtos
                .stream()
                .filter(p -> p.getQuantidade() == 0)
                .forEach(p -> System.out.println(p.getNome() + " R$" + p.getPreco() + " " +
                            p.getValidade() + " " + p.getFornecedor()));

        System.out.println();
        menu();
    }

    public void listarUsuarios() {
        System.out.println("Usuarios cadastrados:");
        Servidor.usuarios
                .stream()
                .forEach(u -> System.out.println(u.getNome() + " " + u.getEndereco() + " " +
                            u.getTelefone() + " " + u.getEmail()));

        System.out.println();
        menu();
    }
    
    public void atualizarProdutos() {
        entrada = new Scanner(System.in);
        
        System.out.print("Qual produto será atualizado: ");      
        String nome = entrada.nextLine();
        
        System.out.print("Quantidade: ");
        int qtd = entrada.nextInt();
        
               
        Iterator teste = Servidor.produtos.iterator();
                    
            while(teste.hasNext()){
                Produto p = (Produto)teste.next();        
                if(p.getNome().equals(nome)){
                    int a = p.getQuantidade() + qtd;
                    p.setQuantidade(a);
                    break;
                }
            }
        menu();
    }


}
