package structures;

public class AVLTree {
    Node root;

    // ------- INSERÇÃO -------
    public void insert(int value) {
        root = insert(root, value, null);
    }

    private Node insert(Node node, int value, Node parent) {
        if (node == null) {
            return new Node(value, parent);
        }

        if (value < node.value) {
            node.setLeft(insert(node.left, value, node));
        } else if (value > node.value) {
            node.setRight(insert(node.right, value, node));
        } else {
            return node; // não permite valores duplicados
        }

        // após inserir, rebalanceia o nó atual
        return rebalance(node);
    }

    // ------- REMOÇÃO -------
    public void delete(int value) {
        root = delete(root, value);
    }

    private Node delete(Node node, int value) {
        if (node == null) {
            return null;
        }

        if (value < node.value) {
            node.setLeft(delete(node.left, value));
        } else if (value > node.value) {
            node.setRight(delete(node.right, value));
        } else {
            // caso 1: nó folha ou com apenas um filho
            if (node.left == null || node.right == null) {
                Node aux = (node.left != null) ? node.left : node.right;

                if (aux == null) {
                    node = null;
                } else {
                    aux.parent = node.parent;
                    node = aux;
                }
            } else {
                // caso 2: nó com dois filhos
                Node successor = leftMost(node.right);
                node.value = successor.value;
                node.setRight(delete(node.right, successor.value));
            }
        }

        // encerra se o nó virou nulo após a remoção
        if (node == null) {
            return null;
        }

        // rebalanceia após a remoção
        return rebalance(node);
    }

    // ------- ROTAÇÕES -------
    private Node rotateRight(Node a) {
        // rotação simples à direita
        Node b = a.left;
        a.setLeft(b.right);
        b.setRight(a);

        return b;
    }

    private Node rotateLeft(Node a) {
        // rotação simples à esquerda
        Node b = a.right;
        a.setRight(b.left);
        b.setLeft(a);

        return b;
    }

    private Node rebalance(Node node) {
        // atualiza a altura do nó
        node.updateHeight();

        int balance = node.balanceFactor();

        // rotação simples à direita
        if (balance > 1 && node.left.balanceFactor() >= 0) {
            return rotateRight(node);
        }

        // rotação dupla esquerda-direita
        if (balance > 1 && node.left.balanceFactor() < 0) {
            node.setLeft(rotateLeft(node.left));
            return rotateRight(node);
        }

        // rotação simples à esquerda
        if (balance < -1 && node.right.balanceFactor() <= 0) {
            return rotateLeft(node);
        }

        // rotação dupla direita-esquerda
        if (balance < -1 && node.right.balanceFactor() > 0) {
            node.setRight(rotateRight(node.right));
            return rotateLeft(node);
        }

        return node;
    }

    // ------- BUSCA -------
    public boolean search(int value) {
        return locateNode(value) != null;
    }

    public Node locateNode(int value) {
        Node current = root;

        while (current != null) {
            if (value == current.value) {
                return current;
            }

            if (value < current.value) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        return null;
    }

    // retorna o menor valor dentro de uma subárvore
    public Node leftMost(Node node) {
        while (node.left != null) {
            node = node.left;
        }

        return node;
    }

    public void printTree() {
        printTree(root, "", true);
    }

    public void printTree(Node node, String prefix, boolean isLeft) {
        if (node != null) {
            if (isLeft) {
                System.out.println(prefix + "├── " + node.value);
                printTree(node.left, prefix + "│   ", true);
                printTree(node.right, prefix + "│   ", false);
            } else {
                System.out.println(prefix + "└── " + node.value);
                printTree(node.left, prefix + "    ", true);
                printTree(node.right, prefix + "    ", false);
            }
        }
    }
}
