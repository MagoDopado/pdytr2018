**Steps for test tp3 module**

```bash
# compiler and client
cd /pdytr/Practicas/Practica3/tp3/src

# create target folder
mkdir ../target

# compiler or client
javac -d ../target ./*.java

# compiler
java -cp ../target/ StartRemoteObject

# client
java -cp ../target/ AskRemote compiler


```
