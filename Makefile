.PHONY: all build run

all: build run

build:
	./build.sh

test:
	./run.sh ./public_test_cases/quicksort/quicksort.ir --naive
	# ./run.sh ./public_test_cases/quicksort/quicksort.ir --symbolic

spim:
	spim -keepstats -f out1.s < ./public_test_cases/quicksort/0.in
