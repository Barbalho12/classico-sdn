./print_delay.sh > out_delay.txt &&
./print_jitter.sh > out_jitter.txt &&
./print_ssim.sh > out_ssim.txt
./print_vqm.sh > out_vqm.txt
./print_throughput.sh > out_throughput.txt &&
# ./print_openflow_packets.sh > open_flow_time.txt &&
# ./print_openflow_packets.sh classico classico &&
# ./print_openflow_packets.sh mc mcast &&
# python print_control_packets.py > open_flow_time.txt &&
python teste.py > open_flow_time.txt &&

gnuplot script.gnu