This simulation uses the model by F. Wang
https://arxiv.org/ftp/arxiv/papers/1411/1411.6053.pdf
Arguments:
-file filename: the file with random settings for A and B (see details below)
-seed seed: the random seed (a number like 12346). The default is 1234
-trials nr trials: the number of pairs that are generated (default is 100000) (This is plenty... larger values just make it slower)
-inequality: CH or Guistina.
             CH uses N11 + N12 + N21 - N22 - singleA - singleB (<0 is classical)
             Guistina uses N11++ - N12+0 - N210+ - N22++  (<0 is classical)

Example: java -jar simulation.jar -file c:\myrandomnumbers.txt -seed 12345  -inequality CH

The file should be a simple text file with one line for each pair, such as 1,2
The first number is which angle to use for A (1 or 2), the second is which angle to use for B (1 or 2)

The results are written to a file summary.csv and also to a more detailed log.csv file with the input angles and counts for each run