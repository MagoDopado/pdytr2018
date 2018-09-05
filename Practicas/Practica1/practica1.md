#### 1 - Identifique similitudes y diferencias entre los sockets en C y en Java.

Similitudes

* Poseen características y funciones suficientes para cumplir el modelo de Cliente / Servidor.
* Ambos tienen el concepto de socket de espera y socket de emisión.
* Los sockets son siempre tanto de emisión como receción (full duplex)
* Se permite elegir el protocolo (TCP/UDP)
* La lectura funciona de la misma manera. Apenas llega algún dato al receptor, se libera (el buffer/stream) para continuar con la ejecución.


Diferencias

* Java hace uso de sockets de manera de alto nivel y C, en cambio, tiene funciones y manejos primitivos de sockets.
* El acceso al socket es a traves de un Stream en Java, y a traves de buffers en C.


#### 2 - Preguntas

Inciso a)
En los ejemplos de la práctica podemos ver como se usan los sockets para una comunicación. El client que manda un mensaje y una vez mandado, termina la comunicación y el server que espera a recibir un mensaje, lo imprime y termina también.
Un modelo Cliente/Servidor tiene ciertas caracteristicas a cumplir:

Pasos de conexión:
  * Inicialización
  * Envió/recepción de peticiones
  * Finalización

Características del Server
  * Inicialmente pasivo (ejemplo: esperando una query )
  * Está escuchando (en algún puerto), listo para responder una petición por un client.
  * Al recibir una petición, la procesa y devuelve una response

Características del Client
  * Tiene un rol activo
  * Envía peticiones al Server
  * Al enviar una petición, espera la respuesta.

Inciso b)


#### 3 - ¿Por qué en C se puede usar la misma variable tanto para leer de teclado como para enviar por un sockets? ¿Esto sería relevante para las aplicaciones c/s?



#### 4 - ¿Podría implementar un servidor de archivos remotos utilizando sockets? Describa brevemente la interfaz y los detalles que considere más importantes del diseño. 



#### 5 - Defina qué es un servidor con estado (stateful server) y qué es un servidor sin estado (stateless server)

**Statefull server**  
El servidor guarda algún estado (información) de un request a otro. De esta manera, el cliente puede mandar menos información por cada request. Este tipo de servidores suelen ser mas simples.


**Stateless server**  

El servidor no guarda el estado de ningún cliente, cada request es independiente de la previa. Normalmente los clientes de servidores sin estado de deben autenticar por cada request. Aunque requiera la una misma base de información por cada requests, es más robusto a perdidas de conexiones. Ante algún error, no invalida ningún archivo del servidor.
