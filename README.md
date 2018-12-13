# Sistemas Multiagentes - Time de futebol Robocup 2D
## Team IK

Team IK, é um projeto de sistemas multiagentes voltado para jogos de futebol.
Implementado em Java com a utilizção da lib "simplesoccerlib" do Prof. Pablo Sampaio

Nosso time visa ter qualquer tipo de comportamento para vários jogadores.
Adotando funcionamento com Máquinas de Estados, estaremos realizando a inclusão de otimização e behaviour trees.

## Simple Soccer Lib
A biblioteca simplifica o uso do simulador, oferecendo:
  
  • percepções globais e perfeitas das posições dos jogadores e da bola;
  
  • ações de mais alto nível, muitas delas aceitando vetores (pares ordenados) para indicar direção, ou posição alvo, etc;
  
  • facilidades para desenvolver um time, conectá-lo e rodá-lo no servidor. 

As percepções são perfeitas. Porém, algumas ações podem ter resultados imperfeitos (exemplo: a ação de rotação “turn” pode girar demais ou de menos);

## Projeto

O programa controla 7 jogadores, sendo 6 na linha e 1 goleiro.

Cada jogador roda de forma autônoma e descentralizada como uma thread separada.

Existem mecanismos simples de comunicação extra-simulador entre eles, sem processamento central (além do processamento dos agentes). 

