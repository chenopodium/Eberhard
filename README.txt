The purpose of this simulation is to show that a local realistic model can explain current experiments (at least up to 2019).
The detection efficiency is greater than listed in the Guistina 2015 experiment and others in that year, who are currently
the most important experiments in this field. 

References:
https://journals.aps.org/prl/supplemental/10.1103/PhysRevLett.115.250401/Supplemental_material_final.pdf
https://arxiv.org/ftp/arxiv/papers/1411/1411.6053.pdf
https://physics.aps.org/featured-article-pdf/10.1103/PhysRevLett.115.250401
https://pub.math.leidenuniv.nl/~gillrd/Peking/Peking_4.pdf
https://en.wikipedia.org/wiki/CHSH_inequality
https://plato.stanford.edu/entries/bell-theorem/
http://www.askingwhy.org/blog/first-puzzle-just-a-probability/puzzle-piece-6-disentangling-the-entanglement/

The model ist based on the paper by F. Wang https://arxiv.org/ftp/arxiv/papers/1411/1411.6053.pdf

Arguments:
-file filename: the file with random settings for A and B (see details below)
-seed seed: the random seed (a number like 12346). The default is 1234
-trials nr trials: the number of pairs that are generated (default is 100000) (This is plenty... larger values just make it slower)
-inequality: CH or Guistina.
             CH uses N11 + N12 + N21 - N22 - singleA - singleB (<0 is classical)
             Guistina uses N11(++) - N12(+0) - N21(0+) - N22(++) (<0 is classical)
-model: Wang or Trivial
             Trivial: trivial model using something similar to sin(delta) for measurement, just as a comparison to the other model
             Wang (default): F. Wang's model from the paper above

Examples:
java -jar simulation.jar  (all default values)
java -jar simulation.jar -file c:\settings.csv -seed 12345  -inequality CH

The file with settings should be a simple text file with one line for each pair, such as:
0,1
0,0
1,0
The first number is which angle to use for A (0=a1 or 1=a2), the second is which angle to use for B (0=b1 or 1=b2)

The results are written to a file summary.csv and also to a more detailed log.csv file with the input angles and counts for each run