#!/bin/bash

H3_USER='uplab'
H3='192.168.2.115'
H3_DIRECTORY='workspace/classico-sdn/evalvid/files/*'

H1_USER='uplab'
H1='192.168.2.120'
H1_DIRECTORY='workspace/classico-sdn/evalvid/files/*'


LOCAL_USER='barbalho'
LOCAL_DIRECTORY='/home/barbalho/workspace/classico-sdn/evalvid/files'


scp $H3_USER@$H3:$H3_DIRECTORY $LOCAL_DIRECTORY



scp $H1_USER@$H1:$H1_DIRECTORY $LOCAL_DIRECTORY
