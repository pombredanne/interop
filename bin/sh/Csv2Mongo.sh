#!/bin/bash

HOME=../..

java -cp $HOME/dist/interop.jar:$HOME/dist/lib/JSON-java.jar:$HOME/dist/lib/opencsv-2.3.jar:$HOME/dist/lib/mongo-java-driver-2.12.3.jar org.bireme.interop.Csv2Mongo "$@"
