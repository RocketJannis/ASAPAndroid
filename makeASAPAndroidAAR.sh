#!/bin/bash

rm -f ASAPAndroid.aar
cd app/build/outputs/aar
jar -xf app-release.aar
rm -rf libs
rm -f app-release.aar
echo "could also change manifest file..."
jar -cf ASAPAndroid.aar *
mv ASAPAndroid.aar ../../../../ASAPAndroid.aar
cd ../../..