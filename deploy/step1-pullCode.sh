#!/bin/bash
git --version;
cd /root;
rm -rf $3;
pwd;
git clone -b $1 $2;



# The $1 = branvh     $2 = choneUrl    $3 = CI-CLOUD

