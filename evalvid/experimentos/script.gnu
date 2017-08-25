set encoding iso_8859_1 
set grid 
set key box width 0.5 height .05 opaque top left 
set xlabel 'Frame number' 
set ylabel 'Jitter difference (seconds)' 

set xrange [0:1000]
set yrange [0.0:0.2] 
# set yrange [-0.05:0.4]

plot 	'out_jitter.txt' using 1:(abs($2)) t 'Multicast' with linespoints lw 2 pt 7 ps 2 lc rgb '#0060ad' 
rep 	'out_jitter.txt' using 1:(abs($3)) t 'CLASSICO'  with linespoints lw 1 pt 5 ps 1.5 lc rgb '#cd1000'

set terminal png font arial 28 size 1600,1200
set output 'Jitter.png' 
# set terminal eps 
# set output 'resultado.eps' 
replot 

##############################################################################

set ylabel 'End-to-End Delay (seconds)' 

set xrange [0:1000]
set yrange [0.0:0.2]

plot 	'out_delay.txt' 	using 1:2 t 'Multicast' with points lw 1 pt 7 ps 2 lc rgb '#0060ad' 
rep 	'out_delay.txt' 	using 1:3 t 'CLASSICO' 	with points lw 1 pt 5 ps 1.5 lc rgb '#cd1000'

set terminal png font arial 28 size 1600,1200 # set terminal eps 
set output 'End-to-End Delay.png'  # set output 'resultado.eps' 

replot 


##############################################################################
set key box width 0.5 height .05 opaque top right
set xlabel 'Time (seconds)' 
set ylabel 'Throughput (Mbps)' 

set xrange [0:60]
set yrange [0.0:3]

plot 	'out_throughput.txt' 	using 1:(($2*8)/(1024*1024)) t 'Multicast' with linespoints lw 1 pt 7 ps 2 lc rgb '#0060ad' 
rep 	'out_throughput.txt' 	using 1:(($3*8)/(1024*1024)) t 'CLASSICO' with linespoints lw 1 pt 5 ps 1.5 lc rgb '#cd1000'

rep 	'out_throughput.txt' 	using 1:(($4*8)/(1024*1024)) t 'Submitted video' with points lw 2 pt 5 ps 2 lc "green"

set terminal png font arial 28 size 1600,1200 # set terminal eps 
set output 'Throughput.png'  # set output 'resultado.eps' 

replot 
