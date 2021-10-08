# repro-graal-build-time-windows

An attempt to reproduce an issue on that only occurs on Windows when using GraalVM native-image in conjunction with clj-easy/graal-build-time.

## Background
The GraalVM team has deprecated the use of the global `--initiatize-at-build-time` and has scheduled it for removal in GraalVM v22.

The Clojure community uses the global `--initialize-at-build-time` to ensure that Clojure classes are initialized appropriately.

The Clojure community has come up with `clj-easy\graal-build-time` as a migration path away from this deprecated option.  This solution uses `org.graalvm.nativeimage.hosted.Feature` to scan for Clojure classes and then register their associated packages via `org.graalvm.nativeimage.hosted.RuntimeClassInitializationRuntimeClassInitialization.initializeAtBuildTime`.

## Issue
When I adopted this migration path, I discovered it was failing for me on the Windows platform.
I have no known issues on macOS or Linux platforms.

I have narrowed down a straightforward way to trigger the failure.
Ask native-image to build code that includes and runs Clojure tests.

I have also been able to remove `clj-easy/graal-build-time` from the equation and reproduce the failure on Windows by using `--initialize-at-build-time` with the specific packages that `clj-easy/graal-build-time` would have discovered.

## Reproduction
These reproductions include Clojure code.

### Tooling
GraalVM Community Edition: v21.2.0
Clojure: v1.10.3
Babashka v6.2.0 as a cross platform scripting tool.

All of the above can be installed on Windows via scoop.
GraalVM can be found in the `java` scoop bucket.
Clojure and Babashka can be found in the `scoop-clojure` bucket.

### What does success look like?
On macOS and linux, when run, the resulting image should show:

```
Hello, running tests in sample namespace.

Testing hello-world.sample-test

Ran 1 tests containing 1 assertions.
0 failures, 0 errors.
All done.
```

### Graal Team
I am not sure what works best for you, so offer some alternatives.
I've checked in a resulting jar under `exhibit/uber.jar`.
If you want to use this prebuilt jar (and not install Clojure or Babashka):

- On Windows: `.\repro1-jar.ps1` (should fail)
- On macOS or Linux: `./repro1-jar.sh` (should pass)

If you'd rather build `uber.jar` on your system (use Clojure but not Babashka)

- On Windows: `.\repro1.ps1` (should fail)
- On macOS or Linux: `./repro1.sh` (should pass)

Should you be interested in using Babashka read on.

### Tasks
We are using babashka tasks to launch reproduction scenarios.

Use `bb tasks` to show all tasks. Tasks that test scenarios are:
```
sanity        Simple sanity test, run our program using Clojure
sanity-java   Simple sanity test, run our program from compiled classes in jar
global-iabt   Build and run native image using global --initialize-at-build-time
gbt           Build and run native image using graal-build-time without global --initialize-at-build-time. This one fails on Windows.
specific-iabt Try to replicate graal-build-time work by expressing as specific --initialize-at-build-time. Also fails on Windows.
```

The task that fails on Windows are `gbt` and `specific-iabt`.

The task of immediate interest for the Graal team would likely be `specific-iabt`.
Should the Graal team not be interested in installing or using Babashka, the `specific-iabt.ps1` PowerShell script has been included.
Should the Graal team want to forego setting up Clojure and work from the jar file, I will make one available.


### GitHub Actions

Have setup GitHub Actions to run each scenario under macOS, ubuntu and Windows.
We should see failure for Windows for `gbt`.
We also see a failure for `specific-iabt`.
