#!/usr/bin/

# Rodar:
#   sh plot.sh <nome do arquivo de saída>

# caracter que delimita o csv (padão: vírgula)
DELIMITER=','

if [ -z "$1" ]
  then
    echo "Nome do arquivo de saída faltando"
else
    gnuplot -e "set datafile separator '$DELIMITER'; set terminal png; set output '$1' ; plot '1eteste.csv' with lines"
fi
