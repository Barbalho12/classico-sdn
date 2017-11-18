set encoding iso_8859_1 
set grid 
set key box width 0.5 height .05 opaque top right
set xlabel 'Tempo do experimento (segundos)' 
set ylabel 'Sobrecarga de Controle (qtd Pacotes)' 

set xrange [0:60]
set style fill solid 1.00 border 0


plot '< sort open_flow_mcast_time.txt | uniq -c' using 1 t 'Multicast SDN' with histograms  linecolor rgb "#cccccc"
rep '< sort open_flow_classico_time.txt | uniq -c' using 1 t 'CLASSICO'  with histograms linecolor rgb "#000000"

set terminal png font arial 28 size 1600,1200
set output 'histograma_controle.png' 
replot 