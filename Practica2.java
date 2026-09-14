public class Practica2{

    public static void main(String[] args){

	ArbolB arbol = new ArbolB();
	System.out.println(arbol);
	System.out.println("Insertando más elementos");
	arbol.insertar(20);
	arbol.insertar(10);
	arbol.insertar(40);	
	System.out.println(arbol);
	System.out.println("Insertando 30");
	arbol.insertar(30);	
	System.out.println(arbol);
	System.out.println("Insertando más elementos");
	arbol.insertar(50);	
	System.out.println("Arbol\n" + arbol);
	arbol.insertar(60);	
	System.out.println("Arbol\n" + arbol);
	arbol.insertar(70);	
	System.out.println("Arbol\n" + arbol);
	arbol.insertar(80);	
	System.out.println("Arbol\n" + arbol);
	arbol.insertar(90);	
	System.out.println("Arbol\n" + arbol);
	arbol.insertar(100);	
	System.out.println("Arbol\n" + arbol);
	arbol.insertar(1);
	System.out.println("Arbol\n" + arbol);
	arbol.insertar(2);
	System.out.println("Arbol\n" + arbol);
	System.out.println("Buscando 20");
	arbol.buscar(20);
	System.out.println("Buscando 1");
	arbol.buscar(1);
	System.out.println("Imprimiendo por niveles");
	System.out.println(arbol.imprimirPorNiveles());
       	System.out.println("Eliminando 20");
        arbol.eliminar(20);
        System.out.println(arbol);
        System.out.println("Eliminando 10");
        arbol.eliminar(10);
        System.out.println(arbol);
        System.out.println("Eliminando 40");
        arbol.eliminar(40);
        System.out.println(arbol);
        System.out.println("Eliminando 50");
        arbol.eliminar(50);
        System.out.println(arbol);
        System.out.println("Eliminando 60");
        arbol.eliminar(60);
        System.out.println(arbol);
        System.out.println("Buscando 20");
        arbol.buscar(20);
        System.out.println("Buscando 70");
        arbol.buscar(70);
	System.out.println("Imprimiendo por niveles");
	System.out.println(arbol.imprimirPorNiveles());
    }
}
