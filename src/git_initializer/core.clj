(ns git-initializer.core
  (:gen-class)
  (:require [clojure.java.shell :as sh]
            [clojure.string :as str]))

(defn- build-git-pipeline [msg]
  [["add" "."]
   ["commit" "-m" msg]
   ["push"]])

(defn- run-git-step [args]
  (println (str ">> git " (str/join " " args) "..."))
  (let [{:keys [exit out err]} (apply sh/sh "git" args)]
    (if (zero? exit)
      (do
        (when-not (str/blank? out) (println out))
        true)
      (do
        (println "ERROR : " err)
        false))))

(defn execute-process [commit-msg]
  (let [pipeline (build-git-pipeline commit-msg)]
    (reduce (fn [_ step]
              (if (run-git-step step)
                :ok
                (reduced :error)))
            :ok pipeline)))

(defn -main [& args]
  (if-let [msg (first args)]
      (execute-process msg)
    (println "Correct usage: ginit <commit-msg>")))

