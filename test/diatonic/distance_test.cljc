(ns diatonic.test.distance
  (:use [clojure.test])
  (:require [diatonic.distance :as dist]))

(deftest test-transpose
  (is (= "G4" (dist/transpose "C4" "P5")))
  (is (= "G4" (dist/transpose "C4" "P-5"))))

(deftest test-interval
  (is (= "P5" (dist/interval "C4" "G4"))))

(run-tests)
