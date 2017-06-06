(ns diatonic.test.interval
  (:use [clojure.test])
  (:require [diatonic.interval :as ivl]))

(deftest split-test
  (is (= [5 "P"] (ivl/split "P5")))
  (is (= [5 "P"] (ivl/split "5P")))
  (is (= [-5 "P"] (ivl/split "P-5"))))

(deftest pitch-test
  (is (= {:step 4 :alteration 1 :octave 0} (ivl/pitch "A5")))
  (is (= {:step 3 :alteration -1 :octave -1} (ivl/pitch "A-5"))))

(deftest pitch->interval-test
  (is (= "A5" (ivl/pitch->interval (ivl/pitch "A5"))))
  (is (= "A-5" (ivl/pitch->interval (ivl/pitch "A-5")))))

(deftest simplify-test
  (is (= ["P1" "M2" "M3" "P4" "P5" "M6" "M7"]
         (map ivl/simplify ["P1" "M2" "M3" "P4" "P5" "M6" "M7"])))
  (is (= ["P8" "M2" "M3" "P4" "P5" "M6" "M7"]
         (map ivl/simplify ["P8" "M9" "M10" "P11" "P12" "M13" "M14"]))))


(deftest invert-test
  (is (= ["P1" "m7" "m6" "P5" "P4" "m3" "m2"]
         (map ivl/invert ["P1" "M2" "M3" "P4" "P5" "M6" "M7"])))
  (is (= ["A1" "M7" "M6" "A5" "A4" "M3" "M2"]
         (map ivl/invert ["d1" "m2" "m3" "d4" "d5" "m6" "m7"])))
  (is (= ["P-1" "m-7" "m-6" "P-5" "P-4" "m-3" "m-2"]
         (map ivl/simplify ["P-1" "M-2" "M-3" "P-4" "P-5" "M-6" "M-7"])))
  (is (= ["P8" "m14" "m13" "P12" "P11" "m10" "m9"]
         (map ivl/invert ["P8" "M9" "M10" "P11" "P12" "M13" "M14"]))))

(run-tests)
