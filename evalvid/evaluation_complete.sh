
./etmp4 -F -x files/rd01 files/sd$1 files/st01 sample.mp4 $1 &&
ffmpeg -i $1.mp4 $1.yuv &&
zip $2.zip $1.mp4 $1.yuv files/* *.txt && 
sudo ./clear.sh