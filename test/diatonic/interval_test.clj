(ns diatonic.test.interval
  (:use [clojure.test])
  (:require [diatonic.interval :as ivl]))

(deftest split-test
  (is (= [5 "P"] (ivl/split "P5")))
  (is (= [5 "P"] (ivl/split "5P")))
  (is (= [-5 "P"] (ivl/split "P-5"))))

(deftest pitch-test
  (is (= {:step 4 :alteration 1 :octave 0} (ivl/->pitch "A5")))
  (is (= {:step 3 :alteration -1 :octave -1} (ivl/->pitch "A-5"))))

(deftest pitch->interval-test
  (is (= "A5" (ivl/pitch->interval (ivl/->pitch "A5"))))
  (is (= "A-5" (ivl/pitch->interval (ivl/->pitch "A-5")))))

(deftest invert-test
  (is (= "P4" (ivl/invert "P5")))
  (is (= "P4" (ivl/invert "M8"))))

(run-tests)
