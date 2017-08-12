#./conversion.sh sample fps kbps X Y
#./conversion.sh sample 24 1024 1920 1080
ffmpeg -i $1.mp4 $1.yuv &&
x264 -I $2 -B $3 --fps $2 -o $1.264 --input-res $4x$5 $1.yuv &&
MP4Box -hint -mtu $3 -fps $2 -add $1.264 $1_ref.mp4 &&
ffmpeg -i $1_ref.mp4 $1_ref.yuv &&
rm sample.264


# ffmpeg -i sample.mp4 sample.yuv
# x264 -I 25 -B 1024 --fps 25 -o sample.264 --input-res 1920x1080 sample.yuv
# MP4Box -hint -mtu 1024 -fps 24 -add sample.264 sample_ref.mp4
# ffmpeg -i sample_ref.mp4 sample_ref.yuv
# rm sample.264