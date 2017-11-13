#Pega o delay de todos os experimentos
paste <(awk '{print $1}' classico/delay_h2.txt ) <( 
awk '{if ($2 == "0") print $3; else print " "}' mc/delay_h2.txt ) <( 
awk '{if ($2 == "0") print $3; else print " "}' classico/delay_h2.txt ) 