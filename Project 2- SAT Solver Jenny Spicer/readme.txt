Jenny Spicer / March 2024 / Project 2: SAT Solver

SAT = satiability

----------------------------------------
To test and run:
You can test these jars using commands on a terminal window. For example, to see how the WalkSAT algorithm performs on the cnf_2.sat example problem, open a terminal window, go to the folder "Project 2", and execute the command:

java -jar sat.jar -a solvers/walksat.jar -p benchmarks/cnf_2.sat
A success will look like this:
Solver:     WalkSAT
Problem:    cnf_2
Result:     success
Operations: 2
Time (ms):  0
Solution:   (and A B)

To test mine, export only the SATSolver.java file (not Test.java!!) as jmspicer.jar into the solvers folder, then go to the project folder from the terminal and run: java -jar sat.jar -a solvers/jmspicer.jar -p benchmarks/cnf_2.sat

There is more diagnostic information that can be printed as an html file!
Append a -o and <HTMLFileName> at the end of the command to see: 1.) how long each problem took to solve, 2.) the solution size, and 3.) the number of operations it took to get the answer.
For example, to compare walksat and brute against cnf_1 and cnf_2 and get the full diagnostic information, you could write: 

java -jar sat.jar -a solvers/walksat.jar solvers/brute.jar -p benchmarks/cnf_1.sat benchmarks/cnf_2.sat -o test.html
----------------------------------------
To test with Test.java: just run it in Eclipse
