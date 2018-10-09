(ns coworker.Worker
  (:gen-class
   :extends "io.kungfury.coworker.BackgroundJavaWork"
   :constructors {[Object, long, int, String, int] [long, int, String, int]}
   :init "init"))

(def workers (atom {}))

(defn -init [_ id stage strand priority] [[id stage strand priority]])
(defn -Work [this serialized]
  (let [[worker args] (clojure.edn/read-string serialized)]
    (apply (get @workers worker) args)
    (.. this (finishWork))))
