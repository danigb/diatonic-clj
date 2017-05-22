(ns diatonic.distance
  (:require [diatonic.note :as note]))

(defn- step->fifths [step] (nth [0 2 4 -1 1 3 5] step))
(defn- step->octs [step] (nth [0 1 2 -1 0 1 2] step))

(defn encode [step alt oct]
  (let [fifths (+ (step->fifths step) (* alt 7))
        foct (if oct (- oct (step->octs step) (* alt 4)))]
    [fifths foct]))

;; Return the number of fifths as if it were unaltered
(defn- fifths->step [fifths] (nth [3 0 4 1 5 2 6] (mod (+ fifths 1) 7)))

(defn decode [[fifths octs]]
  (let [step (fifths->step fifths)]
    [step 0 0]))


(defn note->distance [note]
  (encode (note/step note) (note/alteration note) (note/octave note)))

(defn distance->note [dist]
  (let [[step alt oct] (decode dist)
        letter (note/step->letter step)
        acc (note/alteration->accidentals)
        oct nil]
    [letter acc oct]))


(note/note "Bb3")
(note->distance (note/note "Bb3"))
(distance->note [-2 5])
