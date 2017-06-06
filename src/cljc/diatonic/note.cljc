(ns diatonic.note
  (:require [clojure.string :as str]
            [diatonic.utils :refer [abs]]))

(def pattern #"^([a-gA-G])(#{1,}|b{1,}|x{1,}|)(-?\d*)(.*)$")

(defn split [str]
  "Split a music note in scientific notation into its components:
   step, accidentals and octave strings and optional type quailifier"
  (let [match (re-find pattern str)]
    (if (and match (= (nth match 4) "")) (rest match) nil)))

(defn letter->step [letter]
  (if letter (mod (+ (int (first letter)) 3) 7) nil))

(defn accidentals->alteration [acc]
  "Return the int value of accidentals string"
  (let [len (count acc)]
    (if (= (first acc) \b) (* -1 len) len)))

; FIXME: this sould work on clojure and clojurescript
(defn string->octave [str]
  "Get the value of a string representing an octave"
  (if (and str (> (count str) 0)) (Integer/parseInt str) nil))

(defn parse [str]
  "Parse a music note in scientific notation"
  (let [[letter acc o] (split str)
        step (letter->step letter)
        alt (accidentals->alteration acc)
        oct (string->octave o)]
    (if letter {:step step :alteration alt :octave oct} nil)))

(defn note? [str]
  "Test if a string is a note"
  (if (split str) true false))

(defn step->letter [step]
  (nth ["C" "D" "E" "F" "G" "A" "B"] step))

(defn alteration->accidentals [num]
  (let [c (if (> num 0) \# \b)]
    (str/join (repeat (abs num) c))))

(defn pitch-class [note]
  "Get the note pitch class"
  (let [[l a] (split note)]
    (if l (str l a) nil)))

(defn with-octave [note oct]
  "Return a note in a given octave"
  (let [[l a] (split note)]
    (if l (str l a oct) nil)))

(defn chroma [note]
  "Get the chroma number of a note (the pitch class numeric value from 0 to 6)"
  (let [{:keys [step alteration]} (parse note)]
    (mod (+ step alteration) 7)))

(defn midi [note]
  "Get the midi value of a note"
  (+ (chroma note) (* 12 (octave note)) 12))
