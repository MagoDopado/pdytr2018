### Ejercicio 1

Client y Compiler:
cd /pdytr/Practicas/Practica4/exercise1

Compilar con:
javac -classpath ../lib/* -d target/ src/*.java

Ejecutar con:
java -cp ../lib/*:target jade.Boot -agents "report:Reporter;containers:Containers"

Agregar containers con:
java -cp ../lib/*:target jade.Boot -container -host compiler

### Ejercicio 2

Client y Compiler:
cd /pdytr/Practicas/Practica4/exercise2
mkdir target

Compilar con:
javac -classpath ../lib/* -d target/ src/*.java

Ejecutar con:
java -cp ../lib/*:target jade.Boot -agents "toti:Totalizer(numeros)"

Agregar containers con:
java -cp ../lib/*:target jade.Boot -container -host compiler


### Ejercicio 3
Client y Compiler:
cd /pdytr/Practicas/Practica4/exercise3
mkdir target
head -c 1M </dev/urandom > client/trash.txt
head -c 1M </dev/urandom > server/tresh.txt

Compilar con:
javac -classpath ../lib/* -d target/ src/*.java

Ejecutar con:
java -cp ../lib/*:target jade.Boot -agents ""

(Se puede usar cualquier container como ultimo argumento)
Cliente a servidor:
java -cp ../lib/*:target jade.Boot -container -host compiler -agents "rwAgent:RWAgent(true, "client/trash.txt", "Main-Container")"

Servidor a cliente:
java -cp ../lib/*:target jade.Boot -container -host compiler -agents "rwAgent:RWAgent(false, "server/tresh.txt", "Main-Container")"
