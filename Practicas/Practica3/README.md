**Steps for test tp3 module**

**exercise 1**

```bash
# compiler and client
cd /pdytr/Practicas/Practica3/tp3

# create target folder
mkdir target

# compiler or client
cd ./target
javac -d . ../src/*.java

# compiler
rmiregistry & echo $! >pid
java -cp . StartRemote

# client
java -cp ../target/ AskRemote compiler


```

**exercise 4**

```bash
# compiler and client
cd /pdytr/Practicas/Practica3/exercise4

# create target folder
mkdir target
cd ./target

# compiler or client
javac -d . ../src/*.java

# compiler
rmiregistry & echo $! >pid
java -cp . StartServer

# client
echo "Hello Nan and Git!" >> text.txt
java -cp . Client compiler read text.txt

java -cp . ClientCopier compiler text.txt

```

**exercise 5**

```bash
# compiler and client
cd /pdytr/Practicas/Practica3/exercise4

# create target folder
mkdir target
cd ./target

# compiler or client
javac -d . ../src/*.java

# compiler
rmiregistry & echo $! >pid
java -cp . Server

# client
java -cp . Client compiler message

# Para recrear los bindings
pkill -F pid

```
