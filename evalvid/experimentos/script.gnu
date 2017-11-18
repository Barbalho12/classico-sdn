set encoding iso_8859_1 
set grid 
set key box width 0.5 height .05 opaque top right
set xlabel 'Tempo do experimento (segundos)' 
set ylabel 'Jitter (ms)' 

set xrange [0:60]
set yrange [0.0:150.0]
set ytics 30.0

plot 	'out_jitter.txt' using ($1/24):(abs($2)*1000) t 'Multicast SDN' with linespoints lw 2 pt 2 ps 1.5 lc rgb '#0060ad' 
rep 	'out_jitter.txt' using ($1/24):(abs($3)*1000) t 'CLASSICO'  with linespoints lw 1 pt 2 ps 1.5 lc rgb '#cd1000'

set terminal png font arial 28 size 1600,1200
set output 'Jitter.png' 
replot 

##############################################################################
set key box width 0.5 height .05 opaque top right
set xlabel 'Tempo do experimento (segundos)' 
set ylabel 'Latência (ms)' 

set xrange [0:60]
set yrange [0.0:150.0]
set ytics 30.0

plot 	'out_delay.txt' 	using ($1/24):($2*1000) t 'Multicast SDN' with linespoints lw 1 pt 2 ps 1.5 lc rgb '#0060ad' 
rep 	'out_delay.txt' 	using ($1/24):($3*1000) t 'CLASSICO' 	with linespoints lw 1 pt 2 ps 1.5 lc rgb '#cd1000'

set terminal png font arial 28 size 1600,1200 # set terminal eps 
set output 'Latência.png'  # set output 'resultado.eps' 

replot 



##############################################################################
set key box width 0.5 height .05 opaque bottom right
set xlabel 'Tempo do experimento (segundos)' 
set ylabel 'SSIM' 

set xrange [0:60]
set yrange [0.85:1.0]
set ytics 0.02

plot 	'out_ssim.txt' 	using ($1/24):3 t 'Multicast SDN' with linespoints lw 1 pt 2 ps 3 lc rgb '#0060ad' 
rep 	'out_ssim.txt' 	using ($1/24):4 t 'CLASSICO' with linespoints lw 1 pt 2 ps 1.5 lc rgb '#cd1000'


set terminal png font arial 28 size 1600,1200 # set terminal eps 
set output 'ssim.png'  # set output 'resultado.eps' 

replot 

##############################################################################
set key box width 0.5 height .05 opaque top left
set xlabel 'Tempo do experimento (segundos)' 
set ylabel 'VQM' 

set xrange [0:60]
set yrange [0:15]
set ytics 2

plot 	'out_vqm.txt' 	using ($1/24):3 t 'Multicast SDN' with linespoints lw 1 pt 2 ps 3 lc rgb '#0060ad' 
rep 	'out_vqm.txt' 	using ($1/24):4 t 'CLASSICO' with linespoints lw 1 pt 2 ps 1.5 lc rgb '#cd1000'

set terminal png font arial 28 size 1600,1200 # set terminal eps 
set output 'vqm.png'  

replot 



##############################################################################
set key box width 0.5 height .05 opaque top right
set xlabel 'Tempo do experimento (segundos)' 
set ylabel 'Vazão de dados (Mbps)' 

set xrange [0:60]
set yrange [0.0:4]
set ytics 0.5

plot 'out_throughput.txt' using 1:(($2*8)/(1024*1024)) t 'Multicast SDN' with linespoints lw 1 pt 2 ps 1.5 lc rgb '#0060ad' 
rep 'out_throughput.txt' using 1:(($3*8)/(1024*1024)) t 'CLASSICO' with linespoints lw 1 pt 2 ps 1.5 lc rgb '#cd1000'
rep 'out_throughput.txt' using 1:(($4*8)/(1024*1024)) t 'Vídeo Submetido' with linespoints lw 2 pt 2 ps 1.5 lc "green"

# CHANGE IN FLOWS
# set label 2 'Change in flows'         at 20,1.6   front nopoint tc lt -1
# set arrow from 25,1.54 to 20,1.25 filled back lw 3 lc rgb "black"


set terminal png font arial 28 size 1600,1200 # set terminal eps 
set output 'Vasão de dados.png'  # set output 'resultado.eps' 

replot 

##############################################################################
set key box width 0.5 height .05 opaque top right
set xlabel 'Tempo do experimento (segundos)' 
set ylabel 'Throughput (Mbps)' 

set xrange [0:60]
set yrange [0.0:4]
set ytics 0.5

plot 'out_throughput.txt' using 1:(($2*8)/(1024*1024)) t 'Multicast SDN' with linespoints lw 1 pt 2 ps 1.5 lc rgb '#0060ad' 
rep 'out_throughput.txt' using 1:(($3*8)/(1024*1024)) t 'CLASSICO' with linespoints lw 1 pt 2 ps 1.5 lc rgb '#cd1000'
rep 'out_throughput.txt' using 1:(($4*8)/(1024*1024)) t 'Vídeo Submetido' with linespoints lw 2 pt 2 ps 1.5 lc "green"

# CHANGE IN FLOWS
# set label 2 'Change in flows'         at 20,1.6   front nopoint tc lt -1
# set arrow from 25,1.54 to 20,1.25 filled back lw 3 lc rgb "black"


set terminal png font arial 28 size 1600,1200 # set terminal eps 
set output 'Throughput.png'  # set output 'resultado.eps' 

replot 


##############################################################################
set xlabel 'Requisição (Cliente de Conteúdo)' 
set ylabel 'Tempo de ativação da sessão (ms)' 

set xrange [1:10]
set yrange [0.0:1000]
set ytics 200

plot 	'mc/files/log.txt' 	using ($1-1):($2*1000) t 'Multicast SDN' with linespoints lw 3 pt 2 ps 1.5 lc rgb '#0060ad' 
rep 	'classico/files/log.txt' 	using ($1-1):($2*1000) t 'CLASSICO' 	with linespoints lw 3 pt 2 ps 1.5 lc rgb '#cd1000'

set terminal png font arial 28 size 1600,1200 # set terminal eps 
set output 'Tempo de ativação da sessão.png'  # set output 'resultado.eps' 

replot 
##############################################################################
set key box width 0.5 height .05 opaque top right
set xlabel 'Tempo do experimento (segundos)' 
set ylabel 'Vazão de Controle (Mbps)'

set xrange [0:60]
set yrange [0.0:50]
set ytics 5

plot 'open_flow_time.txt' 	using ($3*8/(1024*1024)) t 'CLASSICO' 	with lines lw 4 lc rgb '#cd1000'
rep 'open_flow_time.txt' 	using ($5*8/(1024*1024)) t 'Multicast SDN' with lines lw 4 lc rgb '#0060ad' 

set terminal png font arial 28 size 1600,1200
set output 'Vazão de Controle.png' 
replot 

##############################################################################
set encoding iso_8859_1 
set grid 
set key box width 0.5 height .05 opaque top right
set xlabel 'Tempo do experimento (segundos)' 
set ylabel 'Sobrecarga de Controle (qtd pacotes)' 

set xrange [0:60]
set yrange [0.0:*]
set ytics 1000
set style fill solid 1.00 border 0


plot '< sort open_flow_mcast_time.txt | uniq -c' using 1 t 'Multicast SDN' with histograms  linecolor rgb "#cccccc"
rep '< sort open_flow_classico_time.txt | uniq -c' using 1 t 'CLASSICO'  with histograms linecolor rgb "#000000"

set terminal png font arial 28 size 1600,1200
set output 'histograma_controle.png' 
replot 