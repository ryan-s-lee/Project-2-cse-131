build-mi:
	javac -d build -cp src src/main/java/mips/MIPSInterpreter.java

run:
	java -cp build main.java.mips.MIPSInterpreter ../out.s < ../public_test_cases/quicksort/0.in

debug:
	java -cp build main.java.mips.MIPSInterpreter --debug --in ../public_test_cases/quicksort/0.in ../out.s
