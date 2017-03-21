./etmp4 -F -x files/rd01 files/sdh2 files/st01 sample.mp4 h2 &&
./etmp4 -F -x files/rd01 files/sdh3 files/st01 sample.mp4 h3 &&
./etmp4 -F -x files/rd01 files/sdh4 files/st01 sample.mp4 h4 &&
ffmpeg -i h2.mp4 h2.yuv &&
ffmpeg -i h3.mp4 h3.yuv &&
ffmpeg -i h4.mp4 h4.yuv 
