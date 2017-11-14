paste <(awk '{print $1}' classico/delay_h2.txt ) <( 
awk '{print $1}' classico/delay_h2.txt ) <( 
awk 'NR>4{print $1}' mc/vqm_yyuv_sample_h2.csv ) <(
awk 'NR>4{print $1}' classico/vqm_yyuv_sample_h2.csv ) 