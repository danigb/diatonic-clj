(ns diatonic.utils)

;; portable (Clojure and ClojureScript) numerical utility functions

;; I can't belive this is not part of the standard library
(defn abs [n]
  (if (< n 0) (- n) n))

;; FIXME: make it compatible with ClojureScript
(defn floor [r] (Math/floor r))
