(ns coworker.coworker
  (:import [io.kungfury.coworker.dbs.postgres PgConnectionManager]
           [io.kungfury.coworker CoworkerManager]
           [io.kungfury.coworker StaticCoworkerConfigurationInput]
           [io.kungfury.coworker WorkInserter]
           [java.time Duration]
           [kotlin.jvm.functions Function1]))

(compile 'coworker.Worker)

(def configureConnManager
  (proxy [Function1] []
    (invoke [toConfigure]
      (doto toConfigure
        (.setJdbcUrl (System/getenv "JDBC_URL"))))))

(def defaultConnManager (PgConnectionManager. configureConnManager))

(defn server []
  (.. (CoworkerManager. defaultConnManager 1 nil (StaticCoworkerConfigurationInput. (Duration/parse "PT5M") (new java.util.HashMap)))
      (Start)))

(defn runworker [worker & args]
  (.. WorkInserter INSTANCE (InsertWork defaultConnManager "coworker.Worker" (pr-str [worker args]) "default" (java.time.Instant/now) 100)))

(defmacro defworker [name & args]
  `(swap! coworker.Worker/workers assoc ~name (fn ~@args)))

(defworker :hello [] (println "Hello, Clojure"))
(defworker :echo [& args] (apply println args))
