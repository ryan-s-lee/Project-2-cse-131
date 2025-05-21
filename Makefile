.PHONY: all build run

all: build test

build:
	./build.sh

test:
	./run.sh ./public_test_cases/quicksort/quicksort.ir --naive
	# ./run.sh ./public_test_cases/quicksort/quicksort.ir --symbolic

spim:
	spim -keepstats -f out.s < ./public_test_cases/quicksort/5.in
