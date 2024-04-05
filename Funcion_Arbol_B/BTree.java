package Funcion_Arbol_B;

import java.util.ArrayList;
import java.util.List;

// Clase que representa un árbol B
class BTree {
    private BTreeNode root; // Raíz del árbol
    private int degree; // Grado del árbol

    // Constructor
    public BTree(int degree) {
        this.degree = degree;
        setRoot(null);
    }

    // Método para buscar una clave en el árbol
    public boolean search(int key) {
        return (getRoot() == null) ? false : getRoot().search(key);
    }

    // Método para agregar una clave al árbol
    public void insert(int key) {
        if (getRoot() == null) {
            setRoot(new BTreeNode(degree, true));
            getRoot().keys[0] = key;
            getRoot().numKeys = 1;
        } else {
            if (getRoot().numKeys == 2 * degree - 1) {
                BTreeNode newRoot = new BTreeNode(degree, false);
                newRoot.children[0] = getRoot();
                newRoot.splitChild(0, getRoot());
                int i = 0;
                if (newRoot.keys[0] < key) {
                    i++;
                }
                newRoot.children[i].insertNonFull(key);
                setRoot(newRoot);
            } else {
                getRoot().insertNonFull(key);
            }
        }
    }

    // Método para eliminar una clave del árbol
    public void remove(int key) {
        if (getRoot() == null) {
            System.out.println("El árbol está vacío");
            return;
        }
        getRoot().remove(key);
        if (getRoot().numKeys == 0) {
            if (getRoot().leaf) {
                setRoot(null);
            } else {
                setRoot(getRoot().children[0]);
            }
        }
    }

	public BTreeNode getRoot() {
		return root;
	}

	public void setRoot(BTreeNode root) {
		this.root = root;
	}
}
