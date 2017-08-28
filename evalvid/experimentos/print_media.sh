echo "---------------delay (ms)---------------"
echo 'Multicast' 'CLASSICO'
awk -F"\t" '{ \
   if($2){ col2+=$2; c2++} \
   if($3){ col3+=$3; c3++} \
   }  END{ print (col2/c2)*1000,'\t' , (col3/c3)*1000  }' out_delay.txt
echo ""
###################################################################
echo "---------------jitter (ms)---------------"
echo 'Multicast' 'CLASSICO'
awk -F"\t" '{ \
   if($2){ col2+=$2; c2++} \
   if($3){ col3+=$3; c3++} \
   }  END{ print (col2/c2)*1000,'\t', (col3/c3)*1000 }' out_jitter.txt
echo ""
###################################################################
echo "---------------throughputer (Mbps)---------------"
echo 'Multicast' 'CLASSICO' 'Submited'
awk -F"\t" '{ \
   if($2){ col2+=$2; c2++} \
   if($3){ col3+=$3; c3++} \
   if($4){ col4+=$4; c4++} \
   }  END{ print ((col2/c2)*8)/(1024*1024),'\t', ((col3/c3)*8)/(1024*1024),'\t', ((col4/c4)*8)/(1024*1024) }' out_throughput.txt
echo ""
###################################################################
echo "---------------ssim---------------"
echo 'Multicast' 'CLASSICO'
awk -F"\t" '{ \
   if($3){ col3+=$3; c3++} \
   if($4){ col4+=$4; c4++} \
   }  END{ print  (col3/c3),'\t', (col4/c4)}' out_ssim.txt
echo ""
###################################################################
echo "---------------vqm---------------"
echo 'Multicast' 'CLASSICO'
awk -F"\t" '{ \
   if($3){ col3+=$3; c3++} \
   if($4){ col4+=$4; c4++} \
   }  END{ print  (col3/c3),'\t', (col4/c4)}' out_vqm.txt
echo ""