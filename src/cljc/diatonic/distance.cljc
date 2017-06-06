(ns diatonic.distance
  (:require [diatonic.note :as nt]
            [diatonic.interval :as ivl]
            [diatonic.utils :refer [floor]]))

(defn- step->fifths [step] (nth [0 2 4 -1 1 3 5] step))
(defn- step->octs [step] (nth [0 1 2 -1 0 1 2] step))

(defn pitch->distance [{:keys [step alteration octave]}]
  (let [fifths (+ (step->fifths step) (* alteration 7))
        foct (if octave (- octave (step->octs step) (* alteration 4)))]
    {:fifths fifths :octaves foct}))

;; Return the number of fifths as if it were unaltered
(defn- fifths->step [fifths] (nth [3 0 4 1 5 2 6] (mod (+ fifths 1) 7)))

(defn distance->pitch [{:keys [fifths octaves]}]
  (let [step (fifths->step fifths)
        alt (floor (/ (+ fifths 1) 7))
        oct (if octaves (floor (+ octaves (* alt 4) (step->octs step))))]
    {:step step :alteration alt :octave oct}))

(defn sum [a b]
  "Add two distances"
  {:fifths (+ (:fifths a) (:fifths b))
   :octaves (+ (:octaves a) (:octaves b))})

(defn subs [a b]
  "Substract two distances"
  {:fifths (- (:fifths a) (:fifths b))
   :octaves (- (:octaves a) (:octaves b))})

(defn transpose [note interval]
  (let [n (pitch->distance (nt/->pitch note))
        i (pitch->distance (ivl/->pitch interval))]
    (nt/pitch->note (distance->pitch (sum n i)))))

(defn transpose-by [interval note]
  (transpose note interval))

(defn interval [a b]
  (let [pa (pitch->distance (nt/->pitch a))
        pb (pitch->distance (nt/->pitch b))]
    (ivl/pitch->interval (distance->pitch (subs pb pa)))))
