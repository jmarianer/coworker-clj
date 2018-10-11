(ns sample-main.core
  (:gen-class)
  (:require [coworker.core :as cw])
  )

(cw/defworker :goodbye [] (println "Goodbye"))

(cw/defstatemachine :longcalculation
  {:init (fn [n] `(:acc ~n 0))
   :acc (fn [n acc]
          (println "Adding" n)
          (Thread/sleep 500)  ; Simulate long calcuation or whatever
          (if (> n 1)
            `(:acc ~(- n 1) ~(+ acc n))
            `(:done ~(+ acc n))))
   :done (fn [acc]
           (println "Result is" acc)
           nil)})

(defn- client []
  (cw/runworker {:at (.. (java.time.Instant/now) (plusSeconds 5))} :echo "Yay!")
  (cw/runworker :longcalculation 10)

  (cw/runworker :hello)
  (cw/runworker :echo "Arguments!")
  (cw/runworker :echo "Many" (+ 5 5) :arguments)
  (cw/runworker :goodbye)
  
  (cw/runworker :echo "Yay should appear in about five seconds")
  )

(defn -main
  [cmd & args]
  (case cmd
    "server" (cw/server)
    "client" (client)))
