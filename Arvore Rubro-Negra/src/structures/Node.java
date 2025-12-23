package structures;

public class Node {
    int value;
    Node left, right, parent;
    Color color;

    public Node(int value) {
        this.value = value;
        this.color = Color.RED; // regra: os nós sempre nascem vermelhos
    }

    /**
     * métodos auxiliares para verificar a cor do nó
     */
    public boolean isRed() {
        return color == Color.RED;
    }

    public boolean isBlack() {
        return color == Color.BLACK;
    }
}

