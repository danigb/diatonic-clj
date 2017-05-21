(defproject diatonic "0.1.0-SNAPSHOT"
  :description "Musical notation"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :source-paths ["src/cljc"]
  :profiles {:dev
                  {:dependencies [[com.cemerick/piggieback "0.2.1"]
                                  [proto-repl "0.3.1"]
                                  [org.clojure/tools.nrepl "0.2.10"]]
                  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}})
