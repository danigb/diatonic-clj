(ns diatonic.test.note
  (:use [clojure.test])
  (:require [diatonic.note :refer :all]))

(deftest pitch-class-test
  (is (= "C" (pitch-class (note "C4"))))
  (is (= "F##" (pitch-class (note "fx")))))

(run-tests)
