echo "-[Clean]-"
Remove-Item -Recurse -Force -ErrorAction Ignore target
mkdir target

echo "-[Running native image]-"
native-image --no-fallback --verbose --initialize-at-build-time=clojure,hello_world -jar exhibit/uber.jar -H:Path=target -H:Name=hello-world

echo "-[Running produced image]"
.\target\hello-world.exe
