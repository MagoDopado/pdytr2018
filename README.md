# Programación Distribuida y Tiempo Real 2018

#### Universidad Nacional de La Plata - Facultad de Informática

Materia de 4to año.

El contenido del repositorio serán las prácticas y resoluciones de las prácticas.

#### SetUp Enviroment

Se agregó un `docker-compose` para facilitar el montado de disco y forwardeo de puertos.  

Los comandos de la práctica deben ser reemplazados:  
```zsh
docker run -itd -v {rutaACarpetaPractica}:/pdytr \
-p 5901:5901 -p 6901:6901 \
--name pdytr gmaron/pdytr:latest
```  
por:
```bash
docker-compose up -d
```  
y:
```bash
docker exec -it --user root pdytr bash
```  
por:  
```bash
docker-compose exec -u root compiler bash
```  


#### > How to install docker-compose

```console
sudo apt install docker-compose

sudo curl -L https://github.com/docker/compose/releases/download/1.22.0/docker-compose-$(uname -s)-$(uname -m) -o /usr/local/bin/docker-compose

docker-compose --version
```
Fuente https://docs.docker.com/compose/install/#install-compose
