### Ejercicio 1

Compilar con:
javac -classpath ../lib/* -d target/ src/*.java

Ejecutar con:
java -cp ../lib/*:target jade.Boot -agents report:Reporter

Agregar containers con
java -cp ../lib/*:target jade.Boot -container -host compiler
