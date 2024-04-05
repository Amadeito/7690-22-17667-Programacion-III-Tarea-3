package Funcion_Arbol_B;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class BTreeVisualizer {
    private BTree tree;
    private int counter;

    public BTreeVisualizer(BTree tree) {
        this.tree = tree;
        this.counter = 0;
    }

    public void visualize() {
        StringBuilder dotStringBuilder = new StringBuilder();
        dotStringBuilder.append("digraph G {\n");
        dotStringBuilder.append("node [shape=record];\n");
        if (tree.getRoot() != null) {
            visualizeNode(tree.getRoot(), dotStringBuilder);
        }
        dotStringBuilder.append("}");
        String dotString = dotStringBuilder.toString();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("btree.dot"))) {
            writer.write(dotString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("dot", "-Tpng", "btree.dot", "-o", "btree.png");
            Process process = processBuilder.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Árbol B visualizado en btree.png");
    }

    private void visualizeNode(BTreeNode node, StringBuilder dotStringBuilder) {
        int currentNode = counter++;
        dotStringBuilder.append(currentNode).append(" [label=\"");
        for (int i = 0; i < node.numKeys; i++) {
            dotStringBuilder.append("<").append(i).append(">").append(node.keys[i]);
            if (i < node.numKeys - 1) {
                dotStringBuilder.append(" | ");
            }
        }
        dotStringBuilder.append("\"];\n");

        for (int i = 0; i <= node.numKeys; i++) {
            if (node.children[i] != null) {
                int childNode = counter++;
                dotStringBuilder.append(currentNode).append(":").append(i).append(" -> ").append(childNode).append(";\n");
                visualizeNode(node.children[i], dotStringBuilder);
            }
        }
    }
}
