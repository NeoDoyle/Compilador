import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class ParseTreeFrame {
    private DefaultMutableTreeNode rootNode;

    // Constructor
    public ParseTreeFrame(String rootLabel) {
        this.rootNode = new DefaultMutableTreeNode(rootLabel);
    }

    // Agregar un nodo hijo
    public DefaultMutableTreeNode addNode(String label, DefaultMutableTreeNode parentNode) {
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(label);
        parentNode.add(newNode);
        return newNode;
    }

    // Obtener el nodo raíz
    public DefaultMutableTreeNode getRootNode() {
        return this.rootNode;
    }

    // Mostrar el árbol en un JFrame
    public void showTree() {
        JTree parseTree = new JTree(rootNode);
        JScrollPane treeScrollPane = new JScrollPane(parseTree);

        JFrame treeFrame = new JFrame("Parse Tree");
        treeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        treeFrame.setSize(500, 600);
        treeFrame.add(treeScrollPane);
        treeFrame.setVisible(true);
    }
}
