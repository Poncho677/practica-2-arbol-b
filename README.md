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

Al ejecutar la clase `Practica2.java`, el `main` crea una instancia de
`ArbolB` y ejecuta, en orden, los siguientes casos:
 
1. Imprime un árbol vacío:
```
   [ ]
```
2. Inserta 10, 20, 40 e imprime el árbol:
```
   [10, 20, 40]
```
3. Inserta 30. Esto provoca un desbordamiento: el nodo se divide, la raíz
   se queda con la llave `30` y sus hijos son `[10, 20]` y `[40]`:
```
   [30]
   [10, 20] [40]
```
4. Sigue insertando 50, 60, 70, 80, 90, 100 (imprimiendo el árbol después
   de cada inserción), hasta llegar a:
```
   [60]
   [10, 30] [90]
   [1, 2] [20] [40, 50] [70, 80] [100]
```
5. Busca `20` → imprime `llave -> 20`.
6. Busca `1` (no pertenece al árbol) → imprime `NOT_FOUND`.
7. Imprime el árbol por niveles con `imprimirPorNiveles()`:
```
   Nivel 0: [60]
   Nivel 1: [10, 30]  [90]
   Nivel 2: [1, 2]  [20]  [40, 50]  [70, 80]  [100]
```
8. Elimina `20`. Como `20` estaba en una hoja y su hermano tenía más de
   `q = ⌊m/2⌋ - 1 = 1` llave, le presta una (`2`) a través del padre; el
   `10` original en el padre pasa a la hoja subocupada y el árbol queda:
```
   [60]
   [2, 30] [90]
   [1] [10] [40, 50] [70, 80] [100]
```
9. Elimina `10`: como su hoja queda subocupada y el hermano tampoco tiene
   de sobra en ese caso, se repite una redistribución/fusión análoga.
   Luego elimina `40`, que sí requiere una fusión real de dos hermanos:
```
   Eliminando 10
   [60]
   [2, 40] [90]
   [1] [30] [50] [70, 80] [100]
 
   Eliminando 40
   [60]
   [2] [90]
   [1] [30, 50] [70, 80] [100]
```
10. Elimina `50`: su hoja tiene más de una llave, así que simplemente se
    quita `50` de la lista sin necesitar reparación:
```
    [60]
    [2] [90]
    [1] [30] [70, 80] [100]
```
11. Elimina `60`, que es la llave de la raíz. La raíz termina vacía y se
    reemplaza por el resultado de la reparación, reduciendo la altura del
    árbol:
```
    [2, 70, 90]
    [1] [30] [80] [100]
```
12. Busca `20` → `NOT_FOUND`. Busca `70` → `llave -> 70`.
13. Imprime por niveles el resultado final:
```
    Nivel 0: [2, 70, 90]
    Nivel 1: [1]  [30]  [80]  [100]
```

## 4. Explicación breve de la representación de un nodo

Un nodo es un elemento del árbol que almacena llaves ordenadas y, cuando es interno, referencias a sus hijos.

- Un nodo puede tener como máximo `m` hijos y `m - 1` llaves.
- Un nodo interno con `r` llaves tiene exactamente `r + 1` hijos.
- Un nodo hoja no tiene hijos, solo llaves.

En el código, cada nodo se representa con:

- `ArrayList<Integer> llaves`: las llaves ordenadas del nodo.
- `ArrayList<Nodo> hijos`: las referencias a sus hijos (vacía si es hoja).
- `Nodo padre`: referencia al nodo padre (útil para la reparación).
- `boolean esHoja`: indica si el nodo es hoja o interno.
- 
## 5. Explicación de qué significa m = 4 y por qué cada nodo admite máximo tres llaves

`m` es el orden del árbol B: ningún nodo puede tener más de `m` hijos ni más de `m - 1` llaves. En esta práctica `m = 4`, por lo tanto:

- Máximo de hijos por nodo: `m = 4`.
- Máximo de llaves por nodo: `m - 1 = 3`.
- Mínimo de llaves en nodos distintos de la raíz: `q = ⌊m/2⌋ - 1 = 1`.

Esto no significa que todo nodo tenga 4 hijos y 3 llaves: es solo el límite. Un nodo interno con `r` llaves tiene `r + 1` hijos, y una hoja tiene 0 hijos.

## 6. Explicación de cómo se decide qué hijo seguir durante una búsqueda

En cada nodo se comparan las llaves en orden para encontrar la primera posición `i` tal que `x <= llaves[i]`. Con esa posición se decide:

1. Si `llaves[i] == x`, la llave está en el nodo actual y la búsqueda termina con éxito.
2. Si `x < llaves[i]`, entonces `x` pertenece al intervalo anterior a `llaves[i]` y se baja por `hijos[i]`.
3. Si `x` es mayor que todas las llaves del nodo, se baja por el último hijo, `hijos[r]` (donde `r` es el número de llaves).
4. Si el nodo es hoja y no se encontró la llave, se regresa `NOT_FOUND`.

Ejemplo: si el nodo es `[15 | 30 | 60]` y buscamos `45`, el primer índice con `45 <= llaves[i]` es `i = 2` (porque `45 < 60`), así que se baja por `hijos[2]`, es decir, el hijo entre 30 y 60.

En el código, esto se implementa en `buscarNodo` con el bucle:

```java
int i = 0;
while (i < nodo.llaves.size() && llave > nodo.llaves.get(i)) {
    i++;
}
nodo = nodo.hijos.get(i);
```

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

### Redistribución

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

### ¿Por qué al insertar una llave nueva no podemos decidir el hijo únicamente comparando con la primera llave del nodo?

Porque un nodo puede guardar hasta 3 llaves, lo que genera hasta 4 caminos posibles. Si solo se compara la nueva llave con el primer número del nodo, únicamente se sabrá si es menor y debe ir al primer camino. Si resulta ser mayor, no hay forma de saber por cuál de los otros tres caminos ir sin revisar las demás llaves del nodo.

### ¿Por qué una búsqueda no debe recorrer todos los hijos de un nodo?

Porque los números dentro de cada nodo ya están ordenados y funcionan de tal forma que señalan la dirección exacta. Al comparar el número que se busca contra el nodo, se sabe con seguridad por cuál único hijo bajar. Revisar los demás caminos es innecesario porque, gracias al orden de la estructura, ya se sabe que ahí no va a estar.
