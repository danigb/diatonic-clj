# diatonic

> Music notation library for Clojure and ClojureScript

Work in progress. Not ready to be used yet.

```clojure
(require '[diatonic.note :as note]
         '[diatonic.interval :as ivl])
         '[diatonic.distance :as dist])

; Notes
(note/pitch-class "fx6") ; => "F##"
(note/midi "C4") ; => 60

; Intervals
(ivl/invert "P5") ; => "P4"

; Distances
(dist/transpose "C4" "5P") ; => "G4"
(dist/interval "C4" "G4") ; => "P5"
```

## Use

Not published yet.

## License

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
