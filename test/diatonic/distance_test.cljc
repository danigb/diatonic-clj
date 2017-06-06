(ns diatonic.test.distance
  (:use [clojure.test])
  (:require [diatonic.distance :as dist]))

(deftest test-transpose
  (is (= "G4" (dist/transpose "C4" "P5")))
  (is (= "F3" (dist/transpose "C4" "P-5"))))

(deftest test-interval
  (is (= "P5" (dist/interval "C4" "G4")))
  (is (= "P-5" (dist/interval "C4" "F3"))))

(run-tests)
