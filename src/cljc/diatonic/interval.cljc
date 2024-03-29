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

(defn- inum->step [inum]
  "Get the step of an interval given it's interval number"
  (mod (- (abs inum) 1) 7))

(defn- step->type [step]
  "Get the type of a interval given it step number"
  (nth ["P" "M" "M" "P" "P" "M" "M"] step))

(defn- inum->octave [num]
  "Get the number of octaves of a interval number"
  (floor (/ (- num 1) 7)))

(defn- alteration [type q]
  "Get the alteration number of an interval given its type and quality"
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

(defn- invert-pitch [{step :step alt :alteration oct :octave}]
  (let [s (mod (- 7 step) 7)
        type (step->type step)
        a (if (= type "P") (- alt) (- (inc alt)))]
    {:step s :alteration a :octave oct}))

(defn pitch [ivl]
  "Get interval in pitch notation (step, alteration, octave)"
  (let [[inum quality] (split ivl)
        step (inum->step inum)
        type (step->type step)
        alt (alteration type quality)
        oct (inum->octave inum)
        pitch {:step step :alteration alt :octave oct}]
    (if (< oct 0) (invert-pitch pitch) pitch)))

(defn- pitch->quality [{step :step alt :alteration}]
  "Convert a pitch (step alteration) to a quality string"
  (let [type (step->type step)]
    (cond
      (= 0 alt) (if (= type "M") "M" "P")
      (and (= -1 alt) (= type "M")) "m"
      (< 0 alt) (fill-str "A" alt)
      (< alt 0) (fill-str "d" (- alt))
      :else nil)))

(defn- pitch->inum [{step :step oct :octave}]
  "Convert a pitch (step octave) to a interval number"
  (if (< oct 0)
    (- (+ step (* -7 (inc oct)) 1))
    (+ step (* 7 oct) 1)))

(defn pitch->interval [pitch]
  "Convert a pitch into an interval string"
  (let [p (if (< (:octave pitch) 0) (invert-pitch pitch) pitch)
        q (pitch->quality p)
        inum (pitch->inum p)]
    (str q inum)))

; FIXME: invert descending intervals
(defn invert [interval]
  "Get the inversion of a given interval"
  (pitch->interval (invert-pitch (pitch interval))))

; FIXME: simplify descending intervals
(defn- simplify-pitch [{step :step oct :octave :as pitch}]
  (if (and (= step 0) (= oct 1)) pitch
    (assoc pitch :octave 0)))

(defn simplify [interval]
  "Get the simplified interval"
  (pitch->interval (simplify-pitch (pitch interval))))

(simplify "M10") "P5"
; (simplify "P-11") "P5"
