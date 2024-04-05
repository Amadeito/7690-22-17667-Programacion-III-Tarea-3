package Funcion_Arbol_B;

class BTreeNode {
    int[] keys; // Arreglo para almacenar las claves
    int degree; // Grado del árbol
    BTreeNode[] children; // Arreglo para los hijos del nodo
    int numKeys; // Número actual de claves en el nodo
    boolean leaf; // Indica si el nodo es una hoja

    // Constructor
    public BTreeNode(int degree, boolean leaf) {
        this.degree = degree;
        this.leaf = leaf;
        keys = new int[2 * degree - 1];
        children = new BTreeNode[2 * degree];
        numKeys = 0;
    }

    // Método para buscar una clave en el nodo
    public boolean search(int key) {
        int index = 0;
        while (index < numKeys && key > keys[index]) {
            index++;
        }
        if (index < numKeys && keys[index] == key) {
            return true;
        }
        return (leaf) ? false : children[index].search(key);
    }

    // Método para insertar una clave en el nodo
    public void insertNonFull(int key) {
        int i = numKeys - 1;
        if (leaf) {
            while (i >= 0 && key < keys[i]) {
                keys[i + 1] = keys[i];
                i--;
            }
            keys[i + 1] = key;
            numKeys++;
        } else {
            while (i >= 0 && key < keys[i]) {
                i--;
            }
            if (children[i + 1].numKeys == 2 * degree - 1) {
                splitChild(i + 1, children[i + 1]);
                if (key > keys[i + 1]) {
                    i++;
                }
            }
            children[i + 1].insertNonFull(key);
        }
    }

    // Métodos para dividir un hijo del nodo
    public void splitChild(int index, BTreeNode y) {
        BTreeNode z = new BTreeNode(y.degree, y.leaf);
        z.numKeys = degree - 1;
        for (int j = 0; j < degree - 1; j++) {
            z.keys[j] = y.keys[j + degree];
        }
        if (!y.leaf) {
            for (int j = 0; j < degree; j++) {
                z.children[j] = y.children[j + degree];
            }
        }
        y.numKeys = degree - 1;
        for (int j = numKeys; j >= index + 1; j--) {
            children[j + 1] = children[j];
        }
        children[index + 1] = z;
        for (int j = numKeys - 1; j >= index; j--) {
            keys[j + 1] = keys[j];
        }
        keys[index] = y.keys[degree - 1];
        numKeys++;
    }

    // Método para eliminar una clave del nodo
    public void remove(int key) {
        int index = searchKey(key);
        if (index < numKeys && keys[index] == key) {
            if (leaf) {
                removeFromLeaf(index);
            } else {
                removeFromNonLeaf(index);
            }
        } else {
            if (leaf) {
                System.out.println("La clave " + key + " no está presente en el árbol");
                return;
            }
            boolean flag = (index == numKeys);
            if (children[index].numKeys < degree) {
                fill(index);
            }
            if (flag && index > numKeys) {
                children[index - 1].remove(key);
            } else {
                children[index].remove(key);
            }
        }
    }

    // Método para buscar una clave en el nodo
    public int searchKey(int key) {
        int index = 0;
        while (index < numKeys && key > keys[index]) {
            ++index;
        }
        return index;
    }

    // Método para eliminar una clave de un nodo hoja
    public void removeFromLeaf(int index) {
        for (int i = index + 1; i < numKeys; ++i) {
            keys[i - 1] = keys[i];
        }
        numKeys--;
    }

    // Método para eliminar una clave de un nodo no hoja
    public void removeFromNonLeaf(int index) {
        int key = keys[index];
        if (children[index].numKeys >= degree) {
            int pred = getPred(index);
            keys[index] = pred;
            children[index].remove(pred);
        } else if (children[index + 1].numKeys >= degree) {
            int succ = getSucc(index);
            keys[index] = succ;
            children[index + 1].remove(succ);
        } else {
            merge(index);
            children[index].remove(key);
        }
    }

    // Método para obtener el predecesor de una clave en un nodo no hoja
    public int getPred(int index) {
        BTreeNode cur = children[index];
        while (!cur.leaf) {
            cur = cur.children[cur.numKeys];
        }
        return cur.keys[cur.numKeys - 1];
    }

    // Método para obtener el sucesor de una clave en un nodo no hoja
    public int getSucc(int index) {
        BTreeNode cur = children[index + 1];
        while (!cur.leaf) {
            cur = cur.children[0];
        }
        return cur.keys[0];
    }

    // Método para llenar el hijo en la posición 'index' si tiene menos de 'degree' claves
    public void fill(int index) {
        if (index != 0 && children[index - 1].numKeys >= degree) {
            borrowFromPrev(index);
        } else if (index != numKeys && children[index + 1].numKeys >= degree) {
            borrowFromNext(index);
        } else {
            if (index != numKeys) {
                merge(index);
            } else {
                merge(index - 1);
            }
        }
    }

    // Método para pedir prestado una clave del hijo en la posición 'index-1'
    public void borrowFromPrev(int index) {
        BTreeNode child = children[index];
        BTreeNode sibling = children[index - 1];
        for (int i = child.numKeys - 1; i >= 0; --i) {
            child.keys[i + 1] = child.keys[i];
        }
        if (!child.leaf) {
            for (int i = child.numKeys; i >= 0; --i) {
                child.children[i + 1] = child.children[i];
            }
        }
        child.keys[0] = keys[index - 1];
        if (!leaf) {
            child.children[0] = sibling.children[sibling.numKeys];
        }
        keys[index - 1] = sibling.keys[sibling.numKeys - 1];
        child.numKeys++;
        sibling.numKeys--;
    }

    // Método para pedir prestado una clave del hijo en la posición 'index+1'
    public void borrowFromNext(int index) {
        BTreeNode child = children[index];
        BTreeNode sibling = children[index + 1];
        child.keys[child.numKeys] = keys[index];
        if (!child.leaf) {
            child.children[child.numKeys + 1] = sibling.children[0];
        }
        keys[index] = sibling.keys[0];
        for (int i = 1; i < sibling.numKeys; ++i) {
            sibling.keys[i - 1] = sibling.keys[i];
        }
        if (!sibling.leaf) {
            for (int i = 1; i <= sibling.numKeys; ++i) {
                sibling.children[i - 1] = sibling.children[i];
            }
        }
        child.numKeys++;
        sibling.numKeys--;
    }

    // Método para fusionar el hijo en la posición 'index' con su hermano
    public void merge(int index) {
        BTreeNode child = children[index];
        BTreeNode sibling = children[index + 1];
        child.keys[degree - 1] = keys[index];
        for (int i = 0; i < sibling.numKeys; ++i) {
            child.keys[i + degree] = sibling.keys[i];
        }
        if (!child.leaf) {
            for (int i = 0; i <= sibling.numKeys; ++i) {
                child.children[i + degree] = sibling.children[i];
            }
        }
        for (int i = index + 1; i < numKeys; ++i) {
            keys[i - 1] = keys[i];
        }
        for (int i = index + 2; i <= numKeys; ++i) {
            children[i - 1] = children[i];
        }
        child.numKeys += sibling.numKeys + 1;
        numKeys--;
    }
}