awk 'NR==1 { A=$1} $1 ~ /^[0-9]+([.][0-9]+)$/ { print int($1-A)}' $1/files/open_flow_$2.txt > open_flow_$2_time.txt
# paste <(awk 'NR==1 { A=$1} $1 ~ /^[0-9]+([.][0-9]+)$/ { print int($1-A)}' classico/files/open_flow_classico.txt ) <( 
# awk 'NR==1 { A=$1} $1 ~ /^[0-9]+([.][0-9]+)$/ { print int($1-A)}' mc/files/open_flow_mcast.txt ) 
