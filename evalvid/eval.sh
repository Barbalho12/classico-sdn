#!/bin/bash
./etmp4 -F -x files/rd01 files/sd$1 files/st01 sample.mp4 $1 &&
sudo rm -R -f experimentos/$2/* &&
mkdir experimentos/$2/files
cp -R files/* experimentos/$2/files &&
mv $1.mp4 *.txt experimentos/$2 &&
sudo ./clear.sh
