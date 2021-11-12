#!/usr/bin/env bash

set -eou pipefail

echo "-[Clean]-"
rm -rf target

echo "-[Creating jar file]-"
clojure -T:build uber-jar

echo "-[Running native image]-"
native-image --no-fallback --verbose --initialize-at-build-time=clojure,hello_world -jar target/uber.jar -H:Path=target -H:Name=hello-world

echo "-[Running produced image]"
target/hello-world
