(ns diatonic.note
  (:require [clojure.string :as str]))

(def pattern #"^([a-gA-G])(#{1,}|b{1,}|x{1,}|)(-?\d*)(.*)$")

(defn parse [note]
  "Parse a note string. Returns [letter accidentals octave]"
  (let [match (re-find pattern note)]
    (if (and match (= (nth match 4) "")) (rest match) [])))

(defn note? [note]
  "Test if a string is a note"
  (if (re-find pattern note) true false))

(defn letter [note]
  "Retrieve the letter of a given note (always in upper case)"
  (-> note parse first str/upper-case))

(defn accidentals [note]
  (-> note parse second (str/replace #"x" "##")))

(defn octave [note]
  (-> note parse (nth 2)))

(defn pitch-class [note]
  "Get the note pitch class"
  (str (letter note) (accidentals note)))
