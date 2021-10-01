;; see https://ask.clojure.org/index.php/10905/control-transient-deps-that-compiled-assembled-into-uberjar?show=10913#c10913
(require 'clojure.tools.deps.alpha.util.s3-transporter)

(ns build
  (:require [clojure.tools.build.api :as b]))

(defn- uber-jar* [basis uber-file]
  (let [lib 'hello-world/hello-world
        class-dir "target/classes"
        src-dirs ["src"]]
    (println "Writing pom")
    (b/write-pom {:class-dir class-dir
                  :lib lib
                  :version "1.0.0"
                  :basis basis
                  :src-dirs ["src"]})
    (b/copy-dir {:src-dirs src-dirs
                 :target-dir class-dir})

    (println "Compiling Clojure sources to classes")
    (b/compile-clj {:basis basis
                    :src-dirs src-dirs
                    :class-dir class-dir
                    :ns-compile '[hello-world.main]})

    (println "Building uberjar" uber-file)
    (b/uber {:class-dir class-dir
             :uber-file uber-file
             :basis basis
             :main 'hello-world.main})))

(defn uber-jar [_]
  (uber-jar* (b/create-basis {:project "deps.edn"})
             "target/uber.jar"))

(defn uber-jar-gbt [_]
  (uber-jar* (b/create-basis {:project "deps.edn" :aliases [:gbt]})
             "target/uber-gbt.jar"))
