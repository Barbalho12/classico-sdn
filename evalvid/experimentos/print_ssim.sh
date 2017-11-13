paste <(awk '{print $1}' classico/delay_h2.txt ) <( 
awk '{print $1}' classico/delay_h2.txt ) <( 
awk 'NR>4{print $1}' mc/ssim_yyuv_sample_h2.csv ) <(
awk 'NR>4{print $1}' classico/ssim_yyuv_sample_h2.csv ) 


# paste <(awk 'NR>4{print $1}' mc_automatic/ssim_yyuv_sample_h3.csv ) <( 
# awk 'NR>4{print $1}' classico_automatic/ssim_yyuv_sample_h3.csv ) 