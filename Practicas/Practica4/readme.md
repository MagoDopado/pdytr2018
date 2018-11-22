### Ejercicio 1

Client y Compiler:
cd /pdytr/Practicas/Practica4/exercise1

Compilar con:
javac -classpath ../lib/* -d target/ src/*.java

Ejecutar con:
java -cp ../lib/*:target jade.Boot -agents report:Reporter,containers:Containers

Agregar containers con:
java -cp ../lib/*:target jade.Boot -container -host compiler

### Ejercicio 2

Client y Compiler:
cd /pdytr/Practicas/Practica4/exercise2

Compilar con:
javac -classpath ../lib/* -d target/ src/*.java

Ejecutar con:
java -cp ../lib/*:target jade.Boot -agents adder:Adder
