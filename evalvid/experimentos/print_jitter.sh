#Pega o jitter de todos os experimentos
paste <(awk '{print $1}' classico_automatic/delay_h3.txt ) <( 
awk '{if ($2 == "0") print $4 - $5; else print " "}' mc_automatic/delay_h3.txt ) <( 
awk '{if ($2 == "0") print $4 - $5; else print " "}' classico_automatic/delay_h3.txt ) 