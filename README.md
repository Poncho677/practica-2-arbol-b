# Practica-2 :smile:
# Equipo: 2 y un infiltrado :shushing_face:
## Integrantes:
* Cruz Escobar Aarón
* Góngora Barroso Alfonso
* Quirino Roman Emmanuel
## 1. Lenguaje utilizado
Java
## 2. Instrucciones para ejecutar el programa
```bash
javac *.java
java Practica2
```
## 3. Instrucciones para ejecutar los casos de prueba

## 4. Explicación breve de la representación de un nodo

## 5. Explicación de qué significa m = 4 y por qué cada nodo admite máximo tres llaves

## 6. Explicación de cómo se decide qué hijo seguir durante una búsqueda

## 7. Explicación de qué ocurre cuando un nodo alcanza cuatro llaves

Cuando un nodo alcanza cuatro llaves (`nodo.llaves.size() == m`, donde `m = 4`), se produce un **desbordamiento**. En ese momento, el nodo deja de cumplir el invariante que establece que un nodo puede tener como máximo `m - 1 = 3` llaves.

Para resolver esta situación, el árbol ejecuta un **split** (división) inmediatamente después de la inserción. El proceso es el siguiente:

1. Se identifica la llave mediana del nodo desbordado. Con la convención de esta práctica, para un nodo con cuatro llaves ordenadas `[k1, k2, k3, k4]`, la llave promovida es `k3` (la tercera llave).
2. Se crean dos nuevos nodos:
   - Nodo izquierdo: conserva las llaves menores que la mediana, es decir, `[k1, k2]`.
   - Nodo derecho: conserva las llaves mayores que la mediana, es decir, `[k4]`.
3. La llave mediana `k3` se promueve al nodo padre.
4. Si el nodo desbordado era un nodo interno, sus hijos también se redistribuyen entre los dos nuevos nodos de acuerdo con los intervalos definidos por las llaves.
5. Si el nodo desbordado era la raíz, se crea una nueva raíz que contiene la llave promovida y cuyos hijos son los dos nodos resultantes. Esto aumenta la altura del árbol en uno.
6. Si el nodo desbordado tenía un padre, la llave promovida se inserta en el padre. Si el padre también alcanza cuatro llaves, el proceso de split se propaga hacia arriba recursivamente.

En el código, este comportamiento se implementa en el método `dividir(Nodo nodo)`, que se llama desde `insertar` cuando `nodo.llaves.size() == m`. Dentro de `dividir`, si el nodo tiene padre, se inserta la llave promovida en el padre y, si este también se desborda, se llama recursivamente a `dividir(padre)`.

## 8. Explicación de la convención de promoción usada en la práctica

Cuando un nodo alcanza cuatro llaves, se divide en dos nodos y se promueve la tercera llave al nodo padre. Por ejemplo, si las llaves son `10, 20, 30, 40`, se promueve `30`; las llaves `10, 20` quedan en el nodo izquierdo y `40` en el nodo derecho.

Si el nodo que se divide es la raíz, la llave promovida se convierte en la nueva raíz. Si no es la raíz, la llave se agrega al padre y, si este también alcanza cuatro llaves, se vuelve a dividir.

## 9. Explicación breve de redistribución y fusión

Si un hermano adyacente (primero el izquierdo, luego el derecho, según las convenciones de desempate) tiene más de una llave (es decir, más que el mínimo `q = 1`), puede ceder una llave. El movimiento no es directo entre hermanos, sino que pasa por el padre:

1. Una llave del hermano sube al padre, ocupando el lugar del separador.
2. La llave separadora del padre baja al nodo con subocupación.

Si el hermano es un nodo interno, también se transfiere un hijo para mantener la relación `r + 1` hijos para `r` llaves.

En el código, esto se implementa en el método `reparar(Nodo nodo)`, en los bloques que verifican `izquierdo.llaves.size() > 1` o `derecho.llaves.size() > 1`.

### Fusión

Si ningún hermano puede prestar (ambos tienen exactamente una llave), se realiza una fusión. La fusión combina:

- El nodo con subocupación.
- La llave separadora del padre.
- El hermano (izquierdo si existe, si no el derecho).

El resultado es un único nodo que contiene todas las llaves y hijos. La llave separadora se elimina del padre, lo que puede provocar que el padre también quede con subocupación. En ese caso, la reparación se propaga hacia arriba recursivamente.

Si la raíz queda vacía y tiene un único hijo, ese hijo se convierte en la nueva raíz, disminuyendo la altura del árbol.

En el código, la fusión se implementa en el método `fusionar(Nodo padre, int i)`, y la propagación se maneja al final de `reparar` con la llamada recursiva `reparar(padre)`.

## 10. Respuesta a las preguntas marcadas en esta guía
