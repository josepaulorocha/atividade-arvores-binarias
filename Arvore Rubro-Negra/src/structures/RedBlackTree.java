package structures;

public class RedBlackTree {

    /**
     * nó NIL que representa as folhas nulas
     * por definição, é sempre preto
      */
    final Node NIL;

    Node root;

    public RedBlackTree() {
        NIL = new Node(0);
        NIL.color = Color.BLACK;
        root = NIL;
    }

    // ------- INSERÇÃO -------
    public void insert(int value) {
        Node z = new Node(value);

        // todo nó novo começa apontando para o NIL
        z.left = z.right = z.parent = NIL;

        Node y = NIL; // pai do novo nó
        Node x = root; // inicia na raiz

        // busca a posição
        while (x != NIL) {
            y = x;

            if (z.value < x.value) {
                x = x.left;
            }
            else if (z.value > x.value) {
                x = x.right;
            }
            else {
                return; // não aceita valores duplicados
            }
        }

        // define o pai do novo nó
        z.parent = y;

        if (y == NIL) {
            root = z;
        }
        else if (z.value < y.value) {
            y.left = z;
        }
        else {
            y.right = z;
        }

        // garante as propriedades da árvore rubro-negra
        insertFixup(z);
    }

    private void insertFixup(Node z) {
        while (z.parent.isRed()) {
            if (z.parent == z.parent.parent.left) {
                Node y = z.parent.parent.right; // tio

                if (y.isRed()) { // caso 1: tio vermelho
                    z.parent.color = Color.BLACK;
                    y.color = Color.BLACK;

                    z.parent.parent.color = Color.RED;
                    z = z.parent.parent;
                } else {
                    // caso 2: z é filho direito
                    if (z == z.parent.right) {
                        z = z.parent;
                        leftRotate(z);
                    }
                    // caso 3: z é filho esquerdo
                    z.parent.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    rightRotate(z.parent.parent);
                }

            } else { // espelho do caso acima
                Node y = z.parent.parent.left;

                if (y.isRed()) {
                    z.parent.color = Color.BLACK;
                    y.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rightRotate(z);
                    }
                    // caso 3 simétrico
                    z.parent.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    leftRotate(z.parent.parent);
                }
            }
        }

        // garante que a raiz seja sempre preta
        root.color = Color.BLACK;
    }

    // ------- REMOÇÃO -------
    public void delete(int value) {
        // localização do nó a ser removido
        Node z = locateNode(value);

        if (z == NIL) return;

        Node x, y = z;

        // guarda a cor original do nó
        Color yOriginalColor = y.color;

        // caso 1: z não tem filho esquerdo
        if (z.left == NIL) {
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == NIL) { // caso 2: z não tem filho direito
            x = z.left;
            transplant(z, z.left);
        } else { // caso 3: z tem dois filhos
            // substituto: menor nó da subárvore da direita
            y = leftMost(z.right);
            yOriginalColor = y.color;
            x = y.right;

            if (y.parent == z) {
                x.parent = y;
            } else {
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }

            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }

        // se o nó removido for preto, chamamos a correção
        if (yOriginalColor == Color.BLACK) {
            deleteFixup(x);
        }
    }

    private void deleteFixup(Node x) {
        while (x != root && x.isBlack()) {
            if (x == x.parent.left) {
                Node w = x.parent.right; // irmão

                // caso 1: irmão vermelho
                if (w.isRed()) { // caso 1
                    w.color = Color.BLACK;
                    x.parent.color = Color.RED;
                    leftRotate(x.parent);
                    w = x.parent.right;
                }

                // caso 2: irmão preto com dois filhos pretos
                if (w.left.isBlack() && w.right.isBlack()) { // caso 2
                    w.color = Color.RED;
                    x = x.parent;
                } else {
                    // caso 3: irmão preto com filho interno vermelho
                    if (w.right.isBlack()) { // caso 3
                        w.left.color = Color.BLACK;
                        w.color = Color.RED;
                        rightRotate(w);
                        w = x.parent.right;
                    }
                    // caso 4: irmão preto com filho externo vermelho
                    w.color = x.parent.color;
                    x.parent.color = Color.BLACK;
                    w.right.color = Color.BLACK;
                    leftRotate(x.parent);
                    x = root;
                }

            } else { // lado direito
                Node w = x.parent.left;

                if (w.isRed()) {
                    w.color = Color.BLACK;
                    x.parent.color = Color.RED;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }

                if (w.right.isBlack() && w.left.isBlack()) {
                    w.color = Color.RED;
                    x = x.parent;
                } else {
                    if (w.left.isBlack()) {
                        w.right.color = Color.BLACK;
                        w.color = Color.RED;
                        leftRotate(w);
                        w = x.parent.left;
                    }

                    w.color = x.parent.color;
                    x.parent.color = Color.BLACK;
                    w.left.color = Color.BLACK;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }

        // remove o "preto extra"
        x.color = Color.BLACK;
    }

    // substitui u por v na árvore
    private void transplant(Node u, Node v) {
        if (u.parent == NIL) {
            root = v;
        }
        else if (u == u.parent.left) {
            u.parent.left = v;
        }
        else {
            u.parent.right = v;
        }

        v.parent = u.parent;
    }

    // ------- ROTAÇÕES -------
    private void leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;

        if (y.left != NIL) {
            y.left.parent = x;
        }

        y.parent = x.parent;

        if (x.parent == NIL) {
            root = y;
        }
        else if (x == x.parent.left) {
            x.parent.left = y;
        }
        else {
            x.parent.right = y;
        }

        y.left = x;
        x.parent = y;
    }

    private void rightRotate(Node y) {
        Node x = y.left;
        y.left = x.right;

        if (x.right != NIL) {
            x.right.parent = y;
        }
        x.parent = y.parent;

        if (y.parent == NIL) {
            root = x;
        }
        else if (y == y.parent.right) {
            y.parent.right = x;
        }
        else {
            y.parent.left = x;
        }

        x.right = y;
        y.parent = x;
    }

    // -------- BUSCA --------
    public Node locateNode(int value) {
        Node current = root;
        while (current != NIL && value != current.value) {
            current = (value < current.value) ? current.left : current.right;
        }
        return current;
    }

    public boolean search(int value) {
        return locateNode(value) != NIL;
    }

    // retorna o menor valor dentro de uma subárvore
    public Node leftMost(Node node) {
        while (node.left != NIL) {
            node = node.left;
        }

        return node;
    }

    public void printTree() {
        printTree(root, "", true);
    }

    private void printTree(Node node, String prefix, boolean isLeft) {
        if (node != NIL) {
            String colorStr = node.isRed() ? "[R]" : "[B]";
            System.out.println(prefix + (isLeft ? "├── " : "└── ") + node.value + colorStr);
            printTree(node.left, prefix + (isLeft ? "│   " : "    "), true);
            printTree(node.right, prefix + (isLeft ? "│   " : "    "), false);
        }
    }
}