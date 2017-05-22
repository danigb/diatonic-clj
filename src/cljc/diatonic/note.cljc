(ns diatonic.note
  (:require [clojure.string :as str]
            [diatonic.utils :refer [abs]]))

(defrecord Note [letter alteration octave])


;; is that necesary?
(defn letter [note] (:letter note))
(defn alteration [note] (:alteration note))
(defn octave [note] (:octave note))

(def pattern #"^([a-gA-G])(#{1,}|b{1,}|x{1,}|)(-?\d*)(.*)$")

(defn parse [str]
  "Parse a note string"
  (let [match (re-find pattern str)]
    (if (and match (= (nth match 4) "")) (rest match) nil)))

(defn note? [note]
  "Test if a string is a note"
  (if (parse note) true false))

(defn- normalize [letter accidentals octave]
  (let [lt (str/upper-case letter)
        acc (str/replace accidentals #"x" "##")
        oct (if (= octave "") nil (Integer/parseInt octave))]
    (Note. lt acc oct)))

(defn note [str]
  "Create a note from a string"
  (let [[parsed letter acc oct type] (re-find pattern str)]
    (if (and parsed (= type "")) (normalize letter acc oct) nil)))

(defn letter->step [letter]
  (if letter (mod (+ (int (first letter)) 3) 7) nil))

(defn step->letter [step]
  (nth ["C" "D" "E" "F" "G" "A" "B"] step))

(defn step [note] (letter->step (letter note)))

(defn accidentals->alteration [acc]
  "Return the int value of accidentals string"
  (let [len (count acc)]
    (if (= (first acc) \b) (* -1 len) len)))

(defn alteration->accidentals [num]
  (let [c (if (> num 0) \# \b)]
    (str/join (repeat (abs num) c))))

(defn alteration [note]
  (accidentals->alteration (:accidentals note)))

(defn pitch-class [note]
  "Get the note pitch class"
  (if note
    (str (:letter note) (:accidentals note))
    nil))

(defn with-octave [note oct]
  "Return the note in a given octave"
  (Note. (:letter note) (:accidentals note) oct))

(defn chroma [note]
  "Get the chroma number of a note (the pitch class numeric value from 0 to 6)"
  (mod (+ (step note) (alteration note)) 7))

(defn midi [note]
  "Get the midi value of a note"
  (+ (chroma note) (* 12 (octave note)) 12))
