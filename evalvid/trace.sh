#!/bin/bash
#
# Passar o endereço IP do destino como parâmetro
./mp4trace -f -s $1 12345 sample.mp4 > files/st01 # (frames)
