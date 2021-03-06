The purpose of this simulation is to check if a local realistic model can explain Bell type experiments.
If the detection efficiency is greater than in the experiments (such as Guistina 2015), then it means
a LHV is still possible. If the experiment has better detection efficiency than the critical efficiency 
(please see https://arxiv.org/ftp/arxiv/papers/1411/1411.6053.pdf), then a LHV can no longer explain it.

Updates:
- add a skewed random generator to see the effect on the inequalities when the random generator
  is not perfectly fair.
  
References:
https://www.slideshare.net/gill1109/yet-another-statistical-analysis-of-the-data-of-the-loophole-free-experiments-of-2015-revised
https://pdfs.semanticscholar.org/8864/c5214a30a7acd8d186f53e8991cd8bc88f84.pdf
https://journals.aps.org/prl/supplemental/10.1103/PhysRevLett.115.250401/Supplemental_material_final.pdf
https://arxiv.org/ftp/arxiv/papers/1411/1411.6053.pdf
https://physics.aps.org/featured-article-pdf/10.1103/PhysRevLett.115.250401
https://pub.math.leidenuniv.nl/~gillrd/Peking/Peking_4.pdf
https://en.wikipedia.org/wiki/CHSH_inequality
https://plato.stanford.edu/entries/bell-theorem/
https://pdfs.semanticscholar.org/d990/dd3286dfca88f1814ac27d0226b52a17909c.pdf
http://www.askingwhy.org/blog/first-puzzle-just-a-probability/puzzle-piece-6-disentangling-the-entanglement/

The model ist based on the paper by F. Wang https://arxiv.org/ftp/arxiv/papers/1411/1411.6053.pdf

Arguments:
-file filename: the file with random settings for A and B (see details below)
-seed seed: the random seed (a number like 12346). The default is 1234
-trials nr trials: the number of pairs that are generated (default is 100000) (This is plenty... larger values just make it slower)
-mode: CONTINUE or RESTART
             RESTART: (default) Clear all data and start from scratch
             CONTINUE: loads the last run with all data and settings, and continues with the specified nr of trials

-inequality: CH, Guistina or CHSH (S)
             CH uses N11 + N12 + N21 - N22 - singleA - singleB (<0 is classical)
             Guistina uses N11(++) - N12(+0) - N21(0+) - N22(++) (<0 is classical)
             CHSC uses c11 - c12 + c21 +c22 (<2 is classical)
-model: Wang or Trivial
             Trivial: trivial model using something similar to sin(delta) for measurement, just as a comparison to the other model
             Wang (default): F. Wang's model from the paper above
-rand: Fair or Skewed (default) : The kind of random generator to use
             Fair: an honest random generator that creates uniform random values
             Skewed: a skewed random genrator that favors some values in the first half of the trial
Examples:
java -jar simulation.jar  (all default values)
java -jar simulation.jar -rand fair
java -jar simulation.jar -file c:\settings.csv -seed 12345  -inequality CH

The file with settings should be a simple text file with one line for each pair, such as:
0,1
0,0
1,0
The first number is which angle to use for A (0=a1 or 1=a2), the second is which angle to use for B (0=b1 or 1=b2)

The results are written to a file summary.csv and also to a more detailed log.csv file with the input angles and counts for each run