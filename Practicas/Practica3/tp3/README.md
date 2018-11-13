**Steps for test tp3 module**

```bash
# compiler and client
cd /pdytr/Practicas/Practica3/tp3

# create target folder
mkdir target

# compiler or client
cd ./target
javac -d . ../src/*.java

# compiler
rmiregistry &
java -cp . StartRemote

# client
java -cp ../target/ AskRemote compiler


```
