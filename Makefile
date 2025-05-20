.PHONY: all build run

all: build run

build:
	mkdir -p build
	javac -d build -cp src src/test/TestMain.java

test:
	java -cp build test.TestMain ./public_test_cases/quicksort/quicksort.ir > out0.s
	java -cp build test.TestMain ./public_test_cases/quicksort/quicksort.ir n > out1.s

run:
	spim -keepstats -f out1.s < ./public_test_cases/quicksort/0.in
