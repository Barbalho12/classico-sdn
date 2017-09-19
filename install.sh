#Update
sudo apt update
sudo apt upgrade

#Get CLASSICO
git clone https://github.com/Barbalho12/classico-sdn.git

#Install Mininet
git clone git://github.com/mininet/mininet
./mininet/util/install.sh -nfv
#Test Mininet
sudo mn --test pingall

#Install dependency from Evalvid
sudo apt install ffmpeg
sudo apt install x264

sudo apt install subversion
svn co https://gpac.svn.sourceforge.net/svnroot/gpac/trunk/gpac gpac


