(ns git-initializer.core
  (:gen-class)
  (:require [clojure.java.shell :as sh]))

(def add ["add" "."])
(defn commit [commit-msg] ["commit" "-m" commit-msg])
(def push ["push"])

(defn execute-process [commit-msg]
  (let [commands [add (commit commit-msg) push]]
    (doseq [args commands]
      (let [result (apply sh/sh "git" args)]
        (println (:out result))
        (println (:err result))))))

(defn -main
  [& args]
  (if (empty? args)
    (println "Error, you didn't specify a commit msg")
  (execute-process (first args))))

