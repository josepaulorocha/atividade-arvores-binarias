package app;

import structures.AVLTree;

public class Main {
    public static void main(String[] args) {
        AVLTree tree = new AVLTree();

        int[] values = {8,4,6,10,15,18,20,2,30,12};

        System.out.println("------- INSERÇÃO -------");
        for (int i : values) {
            tree.insert(i);
            System.out.printf("Inserindo %d:\n", i);
            tree.printTree();
            System.out.println();
        }

        System.out.println("\n------- REMOÇÃO -------");

        System.out.println("\nRemovendo 10:");
        tree.delete(10);
        tree.printTree();

        System.out.println("\n------- PESQUISA -------");
        System.out.println("\nPesquisando 15: " + tree.search(15));
        System.out.println("Pesquisando 10: " + tree.search(10));
    }
}
