#!/bin/sh

DIR=$(
  cd -P -- "$(dirname -- "$0")" && pwd -P
)

if [ -z "$1" ]
then
  echo "Usage: $0 { all | document... }"
elif [ "$1" = "all" ]
then
  ant -f "${DIR}/../build.xml" build
else
  ant -f "${DIR}/../build.xml" -Dtargetdocs=$1 build
fi
