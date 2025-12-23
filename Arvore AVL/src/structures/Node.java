package structures;

public class Node {
    int value;
    Node left, right, parent;
    int height;


    public Node(int value, Node parent) {
        this.value = value;
        this.parent = parent;
        this.height = 1;
    }

    // altura do filho esquerdo
    public int leftHeight() {
        return (left == null) ? 0 : left.height;
    }

    // altura do filho direito
    public int rightHeight() {
        return (right == null) ? 0 : right.height;
    }

    // fator de balanceamento
    public int balanceFactor() {
        return leftHeight() - rightHeight();
    }

    // atualiza a altura do nó
    public void updateHeight() {
        height = 1 + Math.max(leftHeight(), rightHeight());
    }

    // define o filho esquerdo
    public void setLeft(Node node) {
        left = node;
        if (node != null) {
            node.parent = this;
        }

        updateHeight();
    }

    // define o filho direito
    public void setRight(Node node) {
        right = node;
        if (node != null) {
            node.parent = this;
        }

        updateHeight();
    }
}
