#!/bin/bash

HOME=../..

java -cp $HOME/dist/interop.jar:$HOME/dist/lib/JSON-java.jar:$HOME/dist/lib/Bruma.jar org.bireme.interop.Couch2Isis "$@"
