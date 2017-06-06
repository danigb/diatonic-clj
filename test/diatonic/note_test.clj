(ns diatonic.test.note
  (:use [clojure.test])
  (:require [diatonic.note :as note]))

(deftest split-test
  (is (= ["C" "#" 4] (note/split "C#4")))
  (is (= ["C" "##" nil] (note/split "cx"))))

(deftest ->pitch-test
  (is (= {:step 1 :alteration 2 :octave 4} (note/->pitch "dx4"))))

(deftest pitch->note-test
  (is (= "Cb4" (note/pitch->note (note/->pitch "cb4")))))

(deftest pitch-class-test
  (is (= "C##" (note/pitch-class "cx"))))

(deftest midi-test
  (is (= 60 (note/midi "C4")))
  (is (= 62 (note/midi "cx4"))))

(run-tests)
