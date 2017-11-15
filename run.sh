cd evalvid && ./clear.sh && cd .. &&
sudo rm -f -R evalvid/experimentos/mc/* &&
sudo rm -f -R evalvid/experimentos/classico/* && 
date >> test_log.txt && 
echo "Run CLASSICO" >> test_log.txt &&
sudo python classico.py &&
cd evalvid && ./eval.sh h2 classico && ./clear.sh && cd .. &&

date >> test_log.txt &&

echo "Run MCAST" >> test_log.txt &&
sudo python mcast.py &&
cd evalvid && ./eval.sh h2 mc && cd .. &&
date >> test_log.txt 
cd evalvid/experimentos && ./print.sh && zip -r test_$1.zip *.png mc/* classico/*