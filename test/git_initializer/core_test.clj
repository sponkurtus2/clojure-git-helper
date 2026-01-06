(ns git-initializer.core-test
  (:require [clojure.test :refer :all]
            [git-initializer.core :refer :all]
            [clojure.java.shell :as sh]))

(deftest commit-creation-test
  (testing "Function generates the correct arguments"
    (let [msg "My first commit"
          result (commit msg)]
      (is (= ["commit" "-m" "My first commit"] result))
      (is (vector? result)))))

(deftest execute-process-flow-test
  (testing "The full process call git 3 times with the correct arguments"
    (let [petitions-history (atom [])]

      (with-redefs [sh/sh (fn [& args]
                            (swap! petitions-history conj args)
                            {:exit 0 :out "OK" :err ""})]
        (execute-process "Test Commit")

        (is (= 3 (count @petitions-history)))
        (is (= ["git" "add" "."] (first @petitions-history)))
        (is (= ["git" "commit" "-m" "Test Commit"] (second @petitions-history)))
        (is (= ["git" "push"] (last @petitions-history)))))))
