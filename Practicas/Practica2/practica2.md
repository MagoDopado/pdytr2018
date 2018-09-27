<h1><center>PDyTR 2018</center></h1>
<h1><center>Informe 2</center></h1>

</br></br></br></br></br></br></br></br></br></br></br></br></br></br></br></br></br></br></br></br></br></br></br></br></br></br></br></br></br></br></br></br></br></br></br></br></br></br></br></br></br></br></br></br></br></br></br></br>
### Integrantes:

#### Aparicio Natalia, Legajo 12667/7  

#### Eusebi Cirano, Legajo 12469/2  

#### [Repository](https://github.com/MagoDopado/pdytr2018)

</br></br></br></br></br></br>



#### 1 Para los ejemplos de RPC proporcionados (\*.tar, analizar en el orden dado a los nombres de los archivos):

##### a.- Mostrar cómo serían los mismos procedimientos si fueran locales, es decir haciendo el proceso inverso del realizado en la clase de explicación de RPC.

En caso de ser locales, los métodos estarían implementados todos en un mismo ejecutable, donde se llama a la rutina RPC se llamaría la rutina implementada localmente. No serían necesarios los rpcgen files, stubs ni xdr.

##### b.- Ejecutar los procesos y mostrar la salida obtenida (del "cliente" y del "servidor") en cada uno de los casos.  


Ejemplo: **1-simple**
```
root@pdytr:/pdytr/Practicas/Practica2/Ejercicio1/1-simple# ./server
Got request: adding 10, 5
Got request: subtracting 10, 5

--

root@client:/pdytr/Practicas/Practica2/Ejercicio1/1-simple# ./client compiler 10 50
10 + 50 = 60
10 - 50 = -40
```

Ejemplo: **2-ui**
```
root@pdytr:/pdytr/Practicas/Practica2/Ejercicio1/2-u1# useradd -m -s /bin/bash "dockercito"
root@pdytr:/pdytr/Practicas/Practica2/Ejercicio1/2-u1# ./server

--

root@client:/pdytr/Practicas/Practica2/Ejercicio1/2-u1# ./client compiler dockercito
Name dockercito, UID is 1000

root@client:/pdytr/Practicas/Practica2/Ejercicio1/2-u1# ./client compiler root
Name root, UID is 0

root@client:/pdytr/Practicas/Practica2/Ejercicio1/2-u1# ./client compiler pdytr
Name pdytr, UID is -1
```

Ejemplo: **3-array**
```
root@pdytr:/pdytr/Practicas/Practica2/Ejercicio1/3-array# ./vadd_service
Got request: adding 3 numbers
Got request: adding 4 numbers

--

root@client:/pdytr/Practicas/Practica2/Ejercicio1/3-array# ./vadd_client compiler 1 2 3
1 + 2 + 3 = 6

root@client:/pdytr/Practicas/Practica2/Ejercicio1/3-array# ./vadd_client compiler 1 2 3 4
1 + 2 + 3 + 4 = 10

```

Ejemplo: **4-list**
```
root@pdytr:/pdytr/Practicas/Practica2/Ejercicio1/4-list# ./server

-
root@client:/pdytr/Practicas/Practica2/Ejercicio1/4-list# ./client compiler 1 2 5 10
1 2 5 10
Sum is 18

```


##### c.- Mostrar experimentos donde se produzcan errores de conectividad del lado del cliente y del lado del servidor. Si es necesario realice cambios mínimos para, por ejemplo incluir sleep() o exit(), de forma tal que no se reciban comunicaciones o no haya receptor para las comunicaciones. Verifique con UDP y con TCP.

A partir de agregar sleeps al cliente no se detectan errores o interrupciones en las conexiones.  
Si se agregan sleeps al servidor en cambio, verificamos que los clientes tienen un timeout, configurable a partir del método `clnt_control()` y en caso de exeder el timeout el cliente envía un mensaje de error.  
Si se agregan `exit()` al servidor el cliente envía un mensaje de error y la comunicación se corta.

Tanto en TCP como en UDP se detectan los mismos resultados.


#### 2 Describir/analizar las opciones
##### a.- -N
Utilizado para aceptar multiples argumentos y argumentos por valor en las rutinas RPC.
##### b.- -M y -A
-M sirve para habilitar el modo Multithread-safe, donde el valor de retorno es pasado como argumento a la rutina del servidor en vez de utilizar variables estáticas (no thread-safe).
-A habilita el modo Multithread-safe-auto que genera un thread por cada request nueva.

Lo que se debe tener en cuenta del lado del servidor es que al habilitar las funciones Multithread, se debe hacer la sincronización correspondiente para que todas las rutinas sean thread-safe.  

Si se decide agregar multiples threads del lado del cliente (a travez de cualquier implementación standard ej: pthreads/openMP), es el usuario el que debe asegurar la seguridad del mismo ya que RPC no influye de ninguna manera en este flujo de ejecución.  

#### 3 Analizar la _transparencia_ de RPC en cuanto al manejo de parámetros de los procedimientos remotos. Considerar lo que sucede en caso de los valores de retorno. Puede aprovechar los ejemplos provistos.  

RPC se encarga de mantener al usuario abstracto de los llamados remotos. No hay diferencia entre un llamado a función local y uno remoto en cuanto al código (a excepción de la inicialización y configuración del cliente). Del mismo modo, el servidor recibe argumentos como si fueran de un llamado local, por lo que muy facilmente se pueden portar aplicaciones monolíticas en funcionamiento a RPC extrayendo las rutinas a distribuir y generando el archivo de descripción.

#### 4 Con la finalidad de contar con una versión muy restringida de un sistema de archivos remoto, en el
