(ns diatonic.test.note
  (:require [clojure.test :refer :all]
            [diatonic.note :as note]))

(deftest pitch-class
  (is (= "C" (note/pitch-class "C4")))
  (is (= "F##" (note/pitch-class "fx"))))
