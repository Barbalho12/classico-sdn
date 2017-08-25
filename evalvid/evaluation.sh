#!/bin/bash

#Extrai estatistícas
./etmp4 -F -x files/rd01 files/sd$1 files/st01 sample.mp4 $1 &&
# Cria diretório para arquivos
mkdir $2 && 
# Copia para diretório vídeo reconstruído, arquivos gerados pelo evalvid, e arquivos de estatísticas
cp $1.mp4 files/* *.txt $2 &&
#Limpa os dados que não serão mais utilizados
sudo ./clear.sh

