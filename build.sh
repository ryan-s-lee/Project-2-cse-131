
#!/bin/bash

# Write a script to build your backend in this file, install any necessary dependencies
# (As required by your chosen backend language)
#
mkdir -p build
javac -d build -cp src src/test/TestMain.java
