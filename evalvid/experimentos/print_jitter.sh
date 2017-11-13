#Pega o jitter de todos os experimentos
paste <(awk '{print $1}' classico/delay_h2.txt ) <( 
awk '{if ($2 == "0") print $4 - $5; else print " "}' mc/delay_h2.txt ) <( 
awk '{if ($2 == "0") print $4 - $5; else print " "}' classico/delay_h2.txt ) 