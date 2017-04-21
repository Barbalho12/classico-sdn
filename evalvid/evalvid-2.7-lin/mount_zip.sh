./etmp4 -F -x files/rd01 files/sdh12 files/st01 sample.mp4 h12 && 
ffmpeg -i h12.mp4 h12.yuv 
zip $1-$2m.zip h12.mp4 h12.yuv files/* && 
sudo ./clear.sh