# repro-graal-build-time-windows

An attempt to reproduce an issue on that only occurs on Windows when using GraalVM native-image in conjunction with clj-easy/graal-build-time.

## Background
The GraalVM team has deprecated the use of the global `--initiatize-at-build-time` and has scheduled it for removal in GraalVM v22.

The Clojure community uses the global `--initialize-at-build-time` to ensure that Clojure classes are initialized appropriately.

The Clojure community has come up with `clj-easy\graal-build-time` as a migration path away from this deprecated option.  This solution uses `org.graalvm.nativeimage.hosted.Feature` to scan for Clojure classes and then register their associated packages via `org.graalvm.nativeimage.hosted.RuntimeClassInitializationRuntimeClassInitialization.initializeAtBuildTime`.

## Issue
When I adopted this migration path, but discovered it was failing for me on the Windows platform.
I have no known issues on macOS or Linux platforms.

I have narrowed down a straightforward way to trigger the failure.
Ask native-image to build code that includes and runs Clojure tests.

## Reproduction
These reproduction includes Clojure code.
Hopefully, this will be digestable by the GraalVM team should we need to share this repro with them.
We'll cross that bridge if/when we come to it.

### Tooling
Using current Clojure: v1.10.3
And current GraalVM Community Edition: v21.2.0

### Sanity: run test program via Clojure

Running:
```
clojure -M -m hello-world.main
```
Produces:
```
Hello, running tests in sample namespace.

Testing hello-world.sample-test

Ran 1 tests containing 1 assertions.
0 failures, 0 errors.
All done.
```

### Baseline: use global --initialize-at-build-time
This should pass for all platforms.

### Repro #1: use clj-easy/graal-build-time
I expect this to fail for Windows.

### Repro #2: use specific --initialize-at-build-time
This, theoretically, expresses the same thing as repro #1 and should fail on Windows.
