paste <(awk '{print $1}' classico_automatic/delay_h3.txt ) <( 
awk '{print $1}' classico_automatic/delay_h3.txt ) <( 
awk 'NR>4{print $1}' mc_automatic/vqm_yyuv_sample_h3.csv ) <(
awk 'NR>4{print $1}' classico_automatic/vqm_yyuv_sample_h3.csv ) 