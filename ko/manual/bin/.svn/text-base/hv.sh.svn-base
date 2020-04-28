#!/bin/sh

DIR=$(
  cd -P -- "$(dirname -- "$0")" && pwd -P
)

if [ -z "$1" ]
then
  echo "Usage: $0 document"
else
  firefox "${DIR}/../docs/html/$1/index.html" &
fi