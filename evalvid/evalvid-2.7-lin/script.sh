

git clone git://github.com/mininet/mininet
./mininet/util/install.sh -nfv
#teste
sudo mn --test pingall




sudo apt install ffmpeg
sudo apt install x264

#http://www.offminor.de/downloads.html
dpkg -i vooya-1.7-amd64.deb


sudo apt install subversion
svn co https://gpac.svn.sourceforge.net/svnroot/gpac/trunk/gpac gpac


wget http://www2.tkn.tu-berlin.de/research/evalvid/cif/akiyo_cif.264
ffmpeg -i akiyo_cif.264 akiyo_cif.yuv 
x264 -I 30 -B 64 --fps 30 -o a01.264 --input-res 352x288 akiyo_cif.yuv
MP4Box -hint -mtu 1024 -fps 30 -add a01.264 a01.mp4
ffmpeg -i a01.mp4 a01_ref.yuv

sudo -S mn --mac --controller=remote,ip=127.0.0.1,port=6653 --switch ovsk,protocols=OpenFlow13 --topo tree,2 --link=tc,bw=1 

./conversion.sh sample 25 1024 1280 720


ffmpeg -i kong_fu_panda.mkv kong_fu_panda_test.yuv
x264 -I 25 -B 1024 --fps 25 -o kong_fu_panda_test.264 --input-res 1280x720 kong_fu_panda_test.yuv
MP4Box -hint -mtu 1024 -fps 25 -add kong_fu_panda_test.264 kong_fu_panda_test.mp4

#./conversion.sh sample fps kbps X Y
#./conversion.sh sample 25 1024 1280 720
ffmpeg -i $1.mp4 $1.yuv &&
x264 -I $2 -B $3 --fps $2 -o $1.264 --input-res $4x$5 $1.yuv &&
MP4Box -hint -mtu $3 -fps $2 -add $1.264 $1_ref.mp4 &&
ffmpeg -i $1_ref.mp4 $1_ref.yuv &&
rm sample.264

#tx
./speedometer h4-eth0 t
#rx
./speedometer h4-eth0 r


./etmp4 -F -x files/rd01 files/sdh12 files/st01 sample.mp4 h12 && 
ffmpeg -i h12.mp4 h12.yuv && 
zip typical-11m.zip h12.mp4 h12.yuv files/* && 
sudo ./clear.sh