(ns diatonic.interval
  (:require [diatonic.utils :refer [abs floor]]
            [clojure.string :as str]))
;; inverse shorthand notation (with quality after number, preferred)

(defrecord Interval [number quality])
(defn number [interval] (:number interval))
(defn quality [interval] (:quality interval))

(def pattern #"(?x) # allow whitspace and comments
  ^(?: # a composition of two different notations
    ([-+]?\d+)(d{1,4}|m|M|P|A{1,4}) # inverse shorthand notation
  )|(?:
    (d{1,4}|m|M|P|A{1,4})([-+]?\d+) # normal shorthand notation
  )$
")

;; FIXME: check if is a valid quality for that number
(defn- normalize [num quality]
  (Interval. (Integer/parseInt num) quality))

(defn interval [str]
  "Create an interval from a string"
  (let [[parsed n1 q1 q2 n2] (re-find pattern str)]
    (if parsed
      (if n1 (normalize n1 q1) (normalize n2 q2)))))

(defn number->step [number]
  (mod (- (abs number) 1) 7))

(defn step [interval]
  (number->step (number interval)))

(defn number->simple [number]
  (+ (number->step number) 1))

(defn number->type [number]
  (nth ["P" "M" "M" "P" "P" "M" "M"] (number->step number)))

(defn type [interval]
  (number->type (number interval)))

(defn alteration [interval]
  (let [type (type interval)
        q (quality interval)]
       (if (= type "P")
         (cond
           (str/starts-with? q "A") (count q)
           (str/starts-with? q "d") (- (count q))
           :else 0)
         (cond
           (= q "m") -1
           (str/starts-with? q "A") (count q)
           (str/starts-with? q "d") (- (count q) -1)
           :else 0)
           )))

(defn num->octaves [num]
  (floor (/ (- num 1) 7)))

(defn octaves [interval]
  (num->octaves (number interval)))

(def simple->number [])
