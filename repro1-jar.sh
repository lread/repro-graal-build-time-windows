#!/usr/bin/env bash

set -eou pipefail

echo "-[Clean]-"
rm -rf target
mkdir target

echo "-[Running native image]-"
native-image --no-fallback --verbose --initialize-at-build-time=clojure,hello_world -jar exhibit/uber.jar -H:Name=target/hello-world

echo "-[Running produced image]"
target/hello-world
