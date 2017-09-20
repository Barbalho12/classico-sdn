#Update
sudo apt update && 
sudo apt upgrade &&

#Get CLASSICO
sudo apt install git && 
# git clone https://github.com/Barbalho12/classico-sdn.git

#Install Mininet
cd ~/temp &&
git clone git://github.com/mininet/mininet &&
./mininet/util/install.sh -nfv &&
#Test Mininet
sudo mn --test pingall &&

#Install dependency from Evalvid
sudo apt install ffmpeg &&
sudo apt install x264 &&

sudo apt install subversion &&
svn co https://gpac.svn.sourceforge.net/svnroot/gpac/trunk/gpac gpac &&

#Install Ant
sudo apt install ant &&

#JAVA https://www.digitalocean.com/community/tutorials/how-to-install-java-with-apt-get-on-ubuntu-16-04
sudo add-apt-repository ppa:webupd8team/java &&
sudo apt-get update &&

sudo apt-get install oracle-java8-installer 

#Add JAVA_HOME like sudo su
# sudo echo 'JAVA_HOME="/usr/lib/jvm/java-8-oracle"' >> /etc/environment
# source /etc/environment
