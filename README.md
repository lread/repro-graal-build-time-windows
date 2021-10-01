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


### Tasks

We are using babashka tasks to launch reproduction scenarios.

Relevant tasks:
```
sanity        Simple sanity test, run our program using Clojure
sanity-java   Simple sanity test, run our program from compiled classes in jar
baseline      Build and run native image using global --initialize-at-build-time
gbt           Build and run native image using graal-build-time without global --initialize-at-build-time. This one fails on Windows.
specific-iabt Try to replicate graal-build-time work by expressing as specific --initialize-at-build-time. Also fails on Windows.
```

The task that fails on Windows are `gbt` and `specific-iabt`.

I added `specific-iabt` to see what would happen if we removed graal-build-time from the equation and instead simply specified the same packages to initialize via `--initialize-at-build-time=` that graal-build-time would have done for us.
The result look the same as `gbt`.
Which is good, the problem is not triggered by the way graal-build-time is doing its work.

### GitHub Actions

Have setup GitHub Actions to run each scenario under macOS, ubuntu and Windows.
We should see failure for Windows for `gbt`.
We also see a failure for `specific-iabt`.
