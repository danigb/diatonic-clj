(ns diatonic.distance
  (:require [diatonic.note :refer [note step alteration octave]]))

(defn- step->fifths [step] (nth [0 2 4 -1 1 3 5] step))
(defn- step->octs [step] (nth [0 1 2 -1 0 1 2] step))

(defn encode [step alt oct]
  (let [fifths (+ (step->fifths step) (* alt 7))
        foct (if oct (- oct (step->octs step) (* alt 4)))]
    [fifths foct]))


(defn note->distance [note]
  (encode (step note) (alteration note) (octave note)))

(step (note "Eb3"))
(note->distance (note "Eb"))
(note->distance (note "Eb3"))
(map (comp note->distance note) ["Cb" "Db" "Eb" "Fb" "Gb"])
(map (comp note->distance note) ["C" "D" "E" "F" "G"])
