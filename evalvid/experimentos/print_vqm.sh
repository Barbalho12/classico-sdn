paste <(awk '{print $1}' classico_automatic/delay_h2.txt ) <( 
awk '{print $1}' classico_automatic/delay_h2.txt ) <( 
awk 'NR>4{print $1}' mc_automatic/vqm_yyuv_sample_h2.csv ) <(
awk 'NR>4{print $1}' classico_automatic/vqm_yyuv_sample_h2.csv ) 