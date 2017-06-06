(ns diatonic.interval
  (:require [diatonic.utils :refer [abs floor parse-int fill-str]]
            [clojure.string :as str]))

(def pattern #"(?x) # allow whitespaces and comments
  ([-+]?\d+)(d{1,4}|m|M|P|A{1,4}) # inverse shorthand notation (preferred)
  |
  (d{1,4}|m|M|P|A{1,4})([-+]?\d+) # normal shorthand notation
")

(defn split [str]
  "Split an interval string into its components:
    numbera and quality"
  (let [[parsed n1 q1 q2 n2] (re-find pattern str)]
        (if parsed (if n1 [(parse-int n1) q1] [(parse-int n2) q2]) nil)))

(defn inum->step [inum]
  "Get the step of an interval given it's interval number"
  (mod (- (abs inum) 1) 7))

(defn inum->type [inum]
  "Get the type of a interval given it's interval number"
  (nth ["P" "M" "M" "P" "P" "M" "M"] (inum->step inum)))

(defn inum->octave [num]
  "Get the number of octaves of a interval number"
  (floor (/ (- num 1) 7)))

(defn alteration [type q]
  (if (= type "P")
    (cond
      (str/starts-with? q "A") (count q)
      (str/starts-with? q "d") (- (count q))
      :else 0)
    (cond
      (= q "m") -1
      (str/starts-with? q "A") (count q)
      (str/starts-with? q "d") (- (count q) -1)
      :else 0)))

(defn ->pitch [ivl]
  (let [[inum quality] (split ivl)
        type (inum->type inum)
        step (inum->step inum)
        alt (alteration type quality)
        oct (inum->octave inum)]
    {:step step :alteration alt :octave oct}))

(defn pitch->quality [{step :step alt :alteration}]
  (let [type (inum->type (inc step))]
    (cond
      (= 0 alt) (if (= type "M") "M" "P")
      (and (= -1 alt) (= type "M")) "m"
      (< 0 alt) (fill-str "A" alt)
      (< alt 0) (fill-str "d" (- alt))
      :else nil)))

(defn pitch->inum [{step :step oct :octave}]
  (if (< oct 0)
    (- (+ step (* -7 (inc oct)) 1))
    (+ step (* 7 oct) 1)))

(defn pitch->interval [pitch]
  "Convert a pitch into an interval string"
  (let [q (pitch->quality pitch)
        inum (pitch->inum pitch)]
    (str q inum)))
