(ns diatonic.note
  (:require [clojure.string :as str]
            [diatonic.utils :refer [abs parse-int]]))

(def pattern #"^([a-gA-G])(#{1,}|b{1,}|x{1,}|)(-?\d*)(.*)$")

(defn split [str]
  "Split a music note in scientific notation into its components:
   step, accidentals and octave"
  (let [[m l a o q] (re-find pattern str)]
    (if (and m (= q ""))
      (let [letter (str/upper-case l)
            acc (str/replace a "x" "##")
            oct (if (< 0 (count o)) (parse-int o) nil)]
        [letter acc oct])
      nil)))

(defn note? [str]
  "Test if a string is a note in scientific notation"
  (if (split str) true false))

(defn letter->step [letter]
  "Get the int value from note letter (C=0, D=1, ...)"
  (if letter (mod (+ (int (first letter)) 3) 7) nil))

(defn accidentals->alteration [acc]
  "Get the int value of accidentals string"
  (let [len (count acc)]
    (if (= (first acc) \b) (* -1 len) len)))

(defn pitch [str]
  "Convert a note into a pitch notation (step, alteration, octave)"
  (let [[letter acc oct] (split str)
        step (letter->step letter)
        alt (accidentals->alteration acc)]
    (if letter {:step step :alteration alt :octave oct} nil)))

(defn step->letter [step]
  (nth ["C" "D" "E" "F" "G" "A" "B"] step))

(defn alteration->accidentals [num]
  "Convert an alteration number to accidentals string"
  (let [c (if (> num 0) \# \b)]
    (str/join (repeat (abs num) c))))

(defn pitch->note [{:keys [step alteration octave]}]
  "Convert a pitch into a note string"
  (let [letter (step->letter step)
        acc (alteration->accidentals alteration)]
    (if octave (str letter acc octave) (str letter acc))))

(defn pitch-class [note]
  "Get the note pitch class"
  (let [[l a] (split note)]
    (if l (str l a) nil)))

(defn with-octave [note oct]
  "Return a note in a given octave"
  (let [[l a] (split note)]
    (if l (str l a oct) nil)))

(defn- pitch->chroma [{:keys [step alteration]}]
  "Get the chroma of a pitch"
  (mod (+ step alteration) 7))

(defn chroma [note]
  "Get the chroma of a note"
  (pitch->chroma (pitch note)))

(defn midi [note]
  "Get the midi value of a note"
  (let [pitch (pitch note)]
    (if (:octave pitch)
      (+ (pitch->chroma pitch) (* 12 (:octave pitch)) 12)
      nil)))
