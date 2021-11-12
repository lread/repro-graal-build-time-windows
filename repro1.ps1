echo "-[Clean]-"
Remove-Item -Recurse -Force -ErrorAction Ignore target

echo "-[Creating jar file]-"
clojure -T:build uber-jar

echo "-[Running native image]-"
native-image --no-fallback --verbose --initialize-at-build-time=clojure,hello_world -jar target/uber.jar  -H:Path=target -H:Name=hello-world

echo "-[Running produced image]"
.\target\hello-world.exe
