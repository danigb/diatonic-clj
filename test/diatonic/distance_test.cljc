(ns diatonic.test.note
  (:use [clojure.test])
  (:require [diatonic.distance :refer :all]
            [diatonic.note :refer [note]]))

(deftest test-note->distance
  (is (= [-2 5] (note->distance (note "Bb3")))))

(deftest test->distance->note
  (is (= ["B" "b" 3] (distance->note [-2 5]))))

(run-tests)
