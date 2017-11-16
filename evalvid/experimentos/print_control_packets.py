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
	first = -1.0
	for linha in texto :
		

		value = linha.split(" ")[0];
		if(is_number(value)):
			if(first < 0):
				first = float(value)
			numbers.append( int(float(value) - first))
	arq.close()
	# print("----------------")
	lista = []
	for x in range(0, 61):
		lista.append(0)


	# print("----------------")
	for n in numbers :
		if(n >= 0 and n <= 60):
			lista[n] = lista[n]+1

	return lista


classico = file_in('classico/files/open_flow_classico.txt')
mcast = file_in('mc/files/open_flow_mcast.txt')

# print len(classico)
# print len(mcast)
# print("----------------")
for i in range(0, 61):
	print str(i)+"\t"+str(classico[i])+"\t"+str(mcast[i])

