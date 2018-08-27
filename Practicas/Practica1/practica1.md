#### 1 - Identifique similitudes y diferencias entre los sockets en C y en Java.

Similitudes

* Poseen caracteristicas y funciones suficientes para cumplir el modelo de Cliente / Servidor.
* La lectura funciona de la misma manera. Apenas llega algún dato al servidor, se libera (el buffer/stream) para continuar con la ejecución.

Diferencias

* Java hace uso de sockets de manera de alto nivel C, en cambio, tiene funciones y manejos primitivos de sockets.
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



#### 5 - Defina qué es un servidor con estado (stateful server) y qué es un servidor sin estado (stateless server)
