import java.util.ArrayList;

public class ArbolB {

    private int m;
    private Nodo raiz;

    private class Nodo {

        private ArrayList<Integer> llaves;
        private ArrayList<Nodo> hijos;
        private Nodo padre;
        private boolean esHoja;

        public Nodo() {
            llaves = new ArrayList<>();
            hijos = new ArrayList<>();
            padre = null;
            esHoja = true;
        }

        @Override
        public String toString() {
            return llaves.toString();
        }
    }

    public ArbolB() {
        m = 4;
        raiz = new Nodo();
    }

    private Nodo buscarNodo(int llave) {
        Nodo nodo = raiz;
        while (!nodo.esHoja) {
            if (nodo.llaves.contains(llave)) {
                return nodo;
            }
            int i = 0;
            while (i < nodo.llaves.size() && llave > nodo.llaves.get(i)) {
                i++;
            }
            nodo = nodo.hijos.get(i);
        }
        if (nodo.llaves.contains(llave)) {
            return nodo;
        }
        return null;
    }

    public void insertar(int llave) {
        if (buscarNodo(llave) != null) {
            return;
        }
        Nodo nodo = raiz;
        while (!nodo.esHoja) {
            int i = 0;
            while (i < nodo.llaves.size() && llave > nodo.llaves.get(i)) {
                i++;
            }
            nodo = nodo.hijos.get(i);
        }
        nodo.llaves.add(llave);
        nodo.llaves.sort(null);
        if (nodo.llaves.size() == m) {
            dividir(nodo);
        }
    }

    private void dividir(Nodo nodo) {
        int promovida = nodo.llaves.get(2);
        Nodo izquierdo = new Nodo();
        Nodo derecho = new Nodo();
        izquierdo.esHoja = nodo.esHoja;
        derecho.esHoja = nodo.esHoja;
        izquierdo.llaves.add(nodo.llaves.get(0));
        izquierdo.llaves.add(nodo.llaves.get(1));
        derecho.llaves.add(nodo.llaves.get(3));
        if (!nodo.esHoja) {
            izquierdo.hijos.add(nodo.hijos.get(0));
            izquierdo.hijos.add(nodo.hijos.get(1));
            izquierdo.hijos.add(nodo.hijos.get(2));
            derecho.hijos.add(nodo.hijos.get(3));
            derecho.hijos.add(nodo.hijos.get(4));
            for (Nodo hijo : izquierdo.hijos) {
                hijo.padre = izquierdo;
            }
            for (Nodo hijo : derecho.hijos) {
                hijo.padre = derecho;
            }
        }
        if (nodo.padre == null) {
            nodo.llaves.clear();
            nodo.hijos.clear();
            nodo.llaves.add(promovida);
            nodo.hijos.add(izquierdo);
            nodo.hijos.add(derecho);
            nodo.esHoja = false;
            izquierdo.padre = nodo;
            derecho.padre = nodo;
        } else {
            Nodo padre = nodo.padre;
            int i = padre.hijos.indexOf(nodo);
            padre.hijos.remove(i);
            padre.hijos.add(i, izquierdo);
            padre.hijos.add(i + 1, derecho);
            padre.llaves.add(promovida);
            padre.llaves.sort(null);
            izquierdo.padre = padre;
            derecho.padre = padre;
            if (padre.llaves.size() == m) {
                dividir(padre);
            }
        }
    }

    public void buscar(int llave) {
        if (buscarNodo(llave) != null) {
            System.out.println("llave -> " + llave);
        } else {
            System.out.println("NOT_FOUND");
        }
    }

    public void eliminar(int llave) {
        Nodo nodo = buscarNodo(llave);
        if (nodo == null) {
            return;
        }
        if (nodo.esHoja) {
            nodo.llaves.remove(Integer.valueOf(llave));
            reparar(nodo);
        } else {
            int i = nodo.llaves.indexOf(llave);
            Nodo izquierdo = nodo.hijos.get(i);
            Nodo derecho = nodo.hijos.get(i + 1);
            if (izquierdo.llaves.size() > 1) {
                Nodo aux = izquierdo;
                while (!aux.esHoja) {
                    aux = aux.hijos.get(aux.hijos.size() - 1);
                }
                int anterior = aux.llaves.get(aux.llaves.size() - 1);
                nodo.llaves.set(i, anterior);
                aux.llaves.remove(aux.llaves.size() - 1);
                reparar(aux);
            } else if (derecho.llaves.size() > 1) {
                Nodo aux = derecho;
                while (!aux.esHoja) {
                    aux = aux.hijos.get(0);
                }
                int siguiente = aux.llaves.get(0);
                nodo.llaves.set(i, siguiente);
                aux.llaves.remove(0);
                reparar(aux);
            } else {
                fusionar(nodo, i);
                eliminar(llave);
            }
        }
    }

    private void fusionar(Nodo padre, int i) {
        Nodo izquierdo = padre.hijos.get(i);
        Nodo derecho = padre.hijos.get(i + 1);
        izquierdo.llaves.add(padre.llaves.get(i));
        izquierdo.llaves.addAll(derecho.llaves);
        izquierdo.llaves.sort(null);
        if (!derecho.esHoja) {
            for (Nodo hijo : derecho.hijos) {
                hijo.padre = izquierdo;
                izquierdo.hijos.add(hijo);
            }
        }
        padre.llaves.remove(i);
        padre.hijos.remove(i + 1);
    }

    private void reparar(Nodo nodo) {
        if (nodo == raiz) {
            if (nodo.llaves.isEmpty() && nodo.hijos.size() == 1) {
                raiz = nodo.hijos.get(0);
                raiz.padre = null;
            }
            return;
        }
        if (!nodo.llaves.isEmpty()) {
            return;
        }
        Nodo padre = nodo.padre;
        int i = padre.hijos.indexOf(nodo);
        if (i > 0) {
            Nodo izquierdo = padre.hijos.get(i - 1);
            if (izquierdo.llaves.size() > 1) {
                nodo.llaves.add(padre.llaves.get(i - 1));
                int llave = izquierdo.llaves.remove(
                    izquierdo.llaves.size() - 1
                );
                padre.llaves.set(i - 1, llave);
                if (!izquierdo.esHoja) {
                    Nodo hijo = izquierdo.hijos.remove(
                        izquierdo.hijos.size() - 1
                    );
                    nodo.hijos.add(0, hijo);
                    hijo.padre = nodo;
                }
                return;
            }
        }
        if (i < padre.hijos.size() - 1) {
            Nodo derecho = padre.hijos.get(i + 1);
            if (derecho.llaves.size() > 1) {
                nodo.llaves.add(padre.llaves.get(i));
                int llave = derecho.llaves.remove(0);
                padre.llaves.set(i, llave);
                if (!derecho.esHoja) {
                    Nodo hijo = derecho.hijos.remove(0);
                    nodo.hijos.add(hijo);
                    hijo.padre = nodo;
                }
                return;
            }
        }
        if (i > 0) {
            fusionar(padre, i - 1);
        } else {
            fusionar(padre, i);
        }
        reparar(padre);
    }

    public String imprimirPorNiveles(){
	String s = "";
	int i = 0;
	ArrayList<Nodo> nivel = new ArrayList<>();
	nivel.add(raiz);
	while(!nivel.isEmpty()){
	    ArrayList<Nodo> siguiente = new ArrayList<>();
	    s += "Nivel " + String.valueOf(i) + ": ";
	    for(Nodo t: nivel){
		if(!t.hijos.isEmpty())
		    siguiente.addAll(t.hijos);
		s += t.toString() + "  ";
	    }
	    s += "\n";
	    nivel = siguiente;
	    i++;
	}
	return s;
    }

    @Override
    public String toString() {
        String texto = "";
        ArrayList<Nodo> nivel = new ArrayList<>();
        nivel.add(raiz);
        while (!nivel.isEmpty()) {
            ArrayList<Nodo> siguiente = new ArrayList<>();
            for (Nodo nodo : nivel) {
                texto += nodo + " ";
                siguiente.addAll(nodo.hijos);
            }
            texto += "\n";
            nivel = siguiente;
        }
        return texto;
    }
}
