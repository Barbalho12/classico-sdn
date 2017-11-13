date >> test_log.txt && 
echo "Run CLASSICO" >> test_log.txt &&
sudo python classico.py &&
date >> test_log.txt && 
echo "Run MCAST" >> test_log.txt &&
sudo python mcast.py &&
date >> test_log.txt 