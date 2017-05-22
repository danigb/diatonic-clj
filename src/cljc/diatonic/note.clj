(ns diatonic.note
  (:require [clojure.string :as str]))

(def pattern #"^([a-gA-G])(#{1,}|b{1,}|x{1,}|)(-?\d*)(.*)$")

(defn parse [str]
  "Parse a note string"
  (let [match (re-find pattern str)]
    (if (and match (= (nth match 4) "")) (rest match) nil)))

(defn- normalize [letter accidentals octave]
  (let [lt (str/upper-case letter)
        acc (str/replace accidentals #"x" "##")
        oct (if (= octave "") nil (Integer/parseInt octave))]
    [lt acc oct]))

(defn note [str]
  "Create a note from a string"
  (let [[parsed letter acc oct type] (re-find pattern str)]
    (if (and parsed (= type "")) (normalize letter acc oct) nil)))

(defn note? [note]
  "Test if a string is a note"
  (if (parse note) true false))

(defn letter [note]
  "Retrieve the letter of a given note (always in upper case)"
  (if note (first note) nil))

(defn letter->step [letter]
  (if letter (mod (+ (int (first letter)) 3) 7) nil))

(defn step [note]
  "Get the step number of a note (C = 0, D = 1..., B = 6)"
  (letter->step (letter note)))

(defn accidentals [note]
  "Get the accidentals of a note"
  (second note))

(defn accidentals->int [acc]
  "Return the int value of accidentals string"
  (let [len (count acc)]
    (if (= (first acc) \b) (* -1 len) len)))

(defn alteration [note]
  (accidentals->int (accidentals note)))

(defn pitch-class [note]
  "Get the note pitch class"
  (if note
    (str (letter note) (accidentals note))
    nil))

(defn octave [note]
  "Return the octave of a note (0 if the note is not valid)"
  (nth note 2))

;; TODO: return an array?
(defn in-octave [note oct]
  "Return the note in a given octave"
  (let [pc (pitch-class note)]
    (if pc (str pc oct) nil)))

(defn chroma [note]
  "Get the chroma number of a note (the pitch class numeric value from 0 to 6)"
  (mod (+ (step note) (alteration note)) 7))

(defn midi [note]
  "Get the midi value of a note"
  (+ (chroma note) (* 12 (octave note)) 12))
