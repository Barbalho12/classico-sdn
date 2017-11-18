#!/usr/bin/env python
# -*- coding: utf-8 -*-


def is_number(s):
    try:
        float(s)
        return True
    except ValueError:
        pass
	return False

def file_in(file):
	arq = open(file, 'r')
	texto = arq.readlines()
	numbers = []
	sizers = []
	first = -1.0
	for linha in texto :
		

		value = linha.split(" ")[0]
		if(is_number(value)):
			size = (linha.split(" ")[16])[:-2]
			if(first < 0):
				first = float(value)
			numbers.append( int(float(value) - first))
			sizers.append(int(size))
	arq.close()
	# print("----------------")
	lista = []
	for x in range(0, 61):
		lista.append((0,0))


	# print("----------------")
	for n in numbers :
		if(n >= 0 and n <= 60):
			lista[n] = (lista[n][0]+1, lista[n][1]+sizers[n])

	return lista


classico = file_in('classico/files/open_flow_classico.txt')
mcast = file_in('mc/files/open_flow_mcast.txt')

# print len(classico)
# print len(mcast)
# print("----------------")
for i in range(0, 61):
	print str(i)+"\t"+str(classico[i][0])+"\t"+str(classico[i][1])+"\t"+str(mcast[i][0])+"\t"+str(mcast[i][1])

