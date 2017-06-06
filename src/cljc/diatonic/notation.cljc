(ns diatonic.notation
  (:require [diatonic.utils :refer [floor]]))

; Number of fifths to reach a step
(defn- step->fifths [step] (nth [0 2 4 -1 1 3 5] step))
; Number of octs to compensate the fifths of each step
(defn- step->octs [step] (nth [0 1 2 -1 0 1 2] step))
; Step number of a given fifhts (without alteration)
(defn- fifths->step [fifths] (nth [3 0 4 1 5 2 6] (mod (+ fifths 1) 7)))

(defn pitch->distance [{:keys [step alt octave]}]
  (let [fifths (+ (step->fifths step) (* alt 7))
        foct (if octave (- octave (step->octs step) (* alt 4)))]
    {:fifths fifths :octaves octaves}))


(defn distance->pitch [{:keys [fifths octaves]}]
  (let [step (fifths->step fifths)
        alt (floor (/ (+ fifths 1) 7))
        oct (if octaves (floor (+ octaves (* alt 4) (step->octs step))))]
    {:step step :alt alt :octave octave }))
