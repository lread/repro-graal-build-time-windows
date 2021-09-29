(ns hello-world.main
  (:require [clojure.test :as t]
            [hello-world.sample-test])
  (:gen-class))


(defn -main []
  (println "Hello, running tests in sample namespace.")
  (t/run-tests 'hello-world.sample-test)
  (println "All done."))
