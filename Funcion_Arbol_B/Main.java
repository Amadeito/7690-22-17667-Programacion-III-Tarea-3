package Funcion_Arbol_B;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese el grado del árbol B: ");
        int grado = scanner.nextInt();

        BTree tree = new BTree(grado);

        int opcion;
        do {
            System.out.println("\nMenú:");
            System.out.println("1. Insertar elemento");
            System.out.println("2. Eliminar elemento");
            System.out.println("3. Buscar clave");
            System.out.println("4. Visualizar árbol");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            
            switch (opcion) {
                case 1:
                    // Insertar elemento
                    System.out.print("Ingrese la clave a insertar: ");
                    int claveInsertar = scanner.nextInt();
                    tree.insert(claveInsertar);
                    System.out.println("Clave " + claveInsertar + " insertada en el árbol.");
                    break;
                case 2:
                    // Eliminar elemento
                    System.out.print("Ingrese la clave a eliminar: ");
                    int claveEliminar = scanner.nextInt();
                    tree.remove(claveEliminar);
                    System.out.println("Clave " + claveEliminar + " eliminada del árbol.");
                    break;
                case 3:
                    // Buscar clave
                    System.out.print("Ingrese la clave a buscar: ");
                    int claveBuscar = scanner.nextInt();
                    boolean encontrado = tree.search(claveBuscar);
                    if (encontrado) {
                        System.out.println("La clave " + claveBuscar + " está presente en el árbol.");
                    } else {
                        System.out.println("La clave " + claveBuscar + " no está presente en el árbol.");
                    }
                    break;
                case 4:
                    // Visualizar árbol
                    BTreeVisualizer visualizer = new BTreeVisualizer(tree);
                    visualizer.visualize();
                    break;
                case 0:
                    // Salir del programa
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, seleccione una opción válida.");
                    break;
            }
        } while (opcion != 0);
        
        scanner.close();
    }
}
