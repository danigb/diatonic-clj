(ns diatonic.note
  (:require [clojure.string :as str]))

(def pattern #"^([a-gA-G])(#{1,}|b{1,}|x{1,}|)(-?\d*)(.*)$")

(def INVALID ["" "" ""])

(defn parse [note]
  """Parse a note string.
  Returns [letter accidentals octave] or diatonic.note/INVALID if not valid note."""
  (let [match (re-find pattern note)]
    (if (and match (= (nth match 4) "")) (rest match) INVALID)))

(defn note? [note]
  "Test if a string is a note"
  (if (re-find pattern note) true false))

(defn letter [note]
  "Retrieve the letter of a given note (always in upper case)"
  (-> note parse first str/upper-case))

(defn step [note]
  "Get the step number of a note (C = 0, D = 1..., B = 6)"
  (let [l (first (letter note))]
    (if l (mod (+ (int l) 3) 7) 0)))

(defn accidentals [note]
  (-> note parse second (str/replace #"x" "##")))

(defn accidentals->int [acc]
  "Return the int value of accidentals string"
  (let [dir (if (= (first acc) \b) -1 1)]
    (* dir (count acc))))

(defn alteration [note]
  "Return the alteration of a note: an integer representing the accidentals.
  It returns 0 if it's not a valid note."
  (accidentals->int (accidentals note)))

(defn octave [note]
  "Return the octave of a note (0 if the note is not valid)"
  (let [oct (-> note parse (nth 2))]
    (if (= "" oct) 0 (Integer/parseInt oct))))

(defn in-octave [note oct]
  "Return the note in a given octave"
  (let [pc (pitch-class note)]
    (if pc (str pc oct) nil)))

(defn pitch-class [note]
  "Get the note pitch class"
  (let [n (letter note)]
    (if n (str n (accidentals note)) nil)))

(defn chroma [note]
  "Get the chroma number of a note (the pitch class numeric value from 0 to 6)"
  (mod (+ (step note) (alteration note)) 7))

(defn midi [note]
  "Get the midi value of a note"
  (+ (chroma note) (* 12 (octave note)) 12))

(midi "C4")
