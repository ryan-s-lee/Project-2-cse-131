.PHONY: all build run

all: build run

build:
	mkdir -p build
	javac -d build -cp src src/test/TestMain.java

test:
	java -cp build test.TestMain ./public_test_cases/quicksort/quicksort.ir
