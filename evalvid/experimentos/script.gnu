set encoding iso_8859_1 
set grid 
set key box width 0.5 height .05 opaque top left 
set xlabel 'Experimental Time (seconds)' 
set ylabel 'Jitter (milliseconds)' 

set xrange [0:60]
#set yrange [0.0:0.2] 
set yrange [0.0:200]

plot 	'out_jitter.txt' using ($1/24):(abs($2)*1000) t 'Multicast SDN' with linespoints lw 2 pt 7 ps 2 lc rgb '#0060ad' 
rep 	'out_jitter.txt' using ($1/24):(abs($3)*1000) t 'CLASSICO'  with linespoints lw 1 pt 5 ps 1.5 lc rgb '#cd1000'

set terminal png font arial 28 size 1600,1200
set output 'Jitter.png' 
# set terminal eps 
# set output 'resultado.eps' 
replot 

##############################################################################
set xlabel 'Experimental Time (seconds)' 
set ylabel 'End-to-End Delay (milliseconds)' 

set xrange [0:60]
# set yrange [0.0:0.2]
set yrange [0.0:200]

plot 	'out_delay.txt' 	using ($1/24):($2*1000) t 'Multicast SDN' with linespoints lw 1 pt 7 ps 2 lc rgb '#0060ad' 
rep 	'out_delay.txt' 	using ($1/24):($3*1000) t 'CLASSICO' 	with linespoints lw 1 pt 5 ps 1.5 lc rgb '#cd1000'

set terminal png font arial 28 size 1600,1200 # set terminal eps 
set output 'End-to-End Delay.png'  # set output 'resultado.eps' 

replot 



##############################################################################
#set key box width 0.5 height .05 opaque bottom right
#set xlabel 'Experimental Time (seconds)' 
#set ylabel 'SSIM' 

#set xrange [0:60]
#set yrange [0.85:1.0]

#plot 	'out_ssim.txt' 	using ($1/24):3 t 'Multicast SDN' with linespoints lw 1 pt 7 ps 2 lc rgb '#0060ad' 
#rep 	'out_ssim.txt' 	using ($1/24):4 t 'CLASSICO' with linespoints lw 1 pt 5 ps 1.5 lc rgb '#cd1000'


#set terminal png font arial 28 size 1600,1200 # set terminal eps 
#set output 'ssim.png'  # set output 'resultado.eps' 

#replot 

##############################################################################
#set key box width 0.5 height .05 opaque top left
#set xlabel 'Experimental Time (seconds)' 
#set ylabel 'VQM' 

#set xrange [0:60]
#set yrange [0:15]

#plot 	'out_vqm.txt' 	using ($1/24):3 t 'Multicast SDN' with linespoints lw 1 pt 7 ps 2 lc rgb '#0060ad' 
#rep 	'out_vqm.txt' 	using ($1/24):4 t 'CLASSICO' with linespoints lw 1 pt 5 ps 1.5 lc rgb '#cd1000'
#
#set terminal png font arial 28 size 1600,1200 # set terminal eps 
#set output 'vqm.png'  

#replot 



##############################################################################
set key box width 0.5 height .05 opaque top right
set xlabel 'Experimental Time (seconds)' 
set ylabel 'Throughput (Mbps)' 

set xrange [0:60]
set yrange [0.0:4]

plot 'out_throughput.txt' using 1:(($2*8)/(1024*1024)) t 'Multicast SDN' with linespoints lw 1 pt 7 ps 2 lc rgb '#0060ad' 
rep 'out_throughput.txt' using 1:(($3*8)/(1024*1024)) t 'CLASSICO' with linespoints lw 1 pt 5 ps 1.5 lc rgb '#cd1000'
#rep 'out_throughput.txt' using 1:(($4*8)/(1024*1024)) t 'Submitted video' with points lw 2 pt 10 ps 2 lc "green"

# CHANGE IN FLOWS
# set label 2 'Change in flows'         at 20,1.6   front nopoint tc lt -1
# set arrow from 25,1.54 to 20,1.25 filled back lw 3 lc rgb "black"


set terminal png font arial 28 size 1600,1200 # set terminal eps 
set output 'Throughput.png'  # set output 'resultado.eps' 

replot 


##############################################################################
set xlabel 'Requisition of content in order' 
set ylabel 'Session Setup Time (ms)' 

set xrange [1:10]
set yrange [0.0:1000]

plot 	'mc/files/log.txt' 	using ($1-1):($2*1000) t 'Multicast SDN' with linespoints lw 1 pt 7 ps 2 lc rgb '#0060ad' 
rep 	'classico/files/log.txt' 	using ($1-1):($2*1000) t 'CLASSICO' 	with linespoints lw 1 pt 5 ps 1.5 lc rgb '#cd1000'

set terminal png font arial 28 size 1600,1200 # set terminal eps 
set output 'Session Time Setup.png'  # set output 'resultado.eps' 

replot 


