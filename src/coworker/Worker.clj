(ns coworker.Worker
  (:gen-class
   :extends "io.kungfury.coworker.BackgroundJavaWork"
   :constructors {[Object, long, int, String, int] [long, int, String, int]}
   :init "init"
   :state "state"))

(def workers (atom {}))

(defn -getSerializedState [this]
  (pr-str @(:workerstate (.state this))))
(defn -init [connManager id stage strand priority] [[id stage strand priority] {:connManager connManager :workerstate (atom "")}])
(defn -Work [this serialized]
  (let [[worker stage & args] (clojure.edn/read-string serialized)
        function (-> @workers (get worker) (get stage))
        nextstate (apply function args)]
    (if (nil? nextstate)
      (.. this (finishWork))
      (do
        (reset! (:workerstate (.state this)) (cons worker nextstate))
        (.. this (yieldStage (:connManager (.state this)) 17 (java.time.Instant/now)))))))
