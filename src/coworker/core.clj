(ns coworker.core
  (:gen-class)
  (:require [coworker.coworker :as cw]))

(cw/defworker :goodbye [] (println "Goodbye"))

(defn- client []
  (cw/runworker {:at (.. (java.time.Instant/now) (plusSeconds 60))} :echo "Yay!")

  (cw/runworker :hello)
  (cw/runworker :echo "Arguments!")
  (cw/runworker :echo "Many" (+ 5 5) :arguments)
  (cw/runworker :goodbye)
  
  (cw/runworker :echo "Yay should appear in about a minute")
  )

(defn -main
  [cmd & args]
  (case cmd
    "server" (cw/server)
    "client" (client)))
