(ns diatonic.distance
  (:require [diatonic.note :as nt]
            [diatonic.interval :as ivl]
            [diatonic.utils :refer [floor]]))

(defn- step->fifths [step] (nth [0 2 4 -1 1 3 5] step))
(defn- step->octs [step] (nth [0 1 2 -1 0 1 2] step))

(defn- pitch->distance [{:keys [step alteration octave]}]
  (let [fifths (+ (step->fifths step) (* alteration 7))
        foct (if octave (- octave (step->octs step) (* alteration 4)))]
    {:fifths fifths :octaves foct}))

;; Return the number of fifths as if it were unaltered
(defn- fifths->step [fifths] (nth [3 0 4 1 5 2 6] (mod (+ fifths 1) 7)))

(defn- distance->pitch [{:keys [fifths octaves]}]
  (let [step (fifths->step fifths)
        alt (floor (/ (+ fifths 1) 7))
        oct (if octaves (floor (+ octaves (* alt 4) (step->octs step))))]
    {:step step :alteration alt :octave oct}))

(defn- add [{fa :fifths oa :octaves} {fb :fifths ob :octaves}]
  "Add two distances"
  {:fifths (+ fa fb) :octaves (if (and oa ob) (+ oa ob) nil)})

; FIXME: better substract between pure fifths
(defn- substract [{fa :fifths oa :octaves} {fb :fifths ob :octaves}]
  "Substract two distances"
  {:fifths (- fa fb) :octaves (if (and oa ob) (+ oa ob) 0)})

(defn- operate [fn a b]
  (distance->pitch (fn (pitch->distance a) (pitch->distance b))))

(defn transpose [note interval]
  "Tranpose a note by an interval"
  (nt/pitch->note (operate add (nt/pitch note) (ivl/pitch interval))))

(defn transpose-by [interval note]
  "Transpose a note by an interval, arguments flipped"
  (transpose note interval))

(defn interval [a b]
  "Get the interval between two notes"
  (ivl/pitch->interval (operate substract (nt/pitch b) (nt/pitch a))))
