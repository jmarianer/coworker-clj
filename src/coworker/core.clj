(ns coworker.core
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

(defn runworker [& args]
  (let [[options args] (if (map? (first args)) [(first args) (next args)] [nil args])
        {strand :strand at :at priority :priority :or
         {strand "default" at (java.time.Instant/now) priority 100}} options
        [name args] [(first args) (next args)]]
    (.. WorkInserter INSTANCE (InsertWork defaultConnManager "coworker.Worker" (pr-str (cons name (cons :init args))) strand at priority))))

(defmacro defstatemachine [name stages]
  `(swap! coworker.Worker/workers assoc ~name ~stages))

(defmacro defworker [name params & body]
  `(defstatemachine ~name
    {:init (fn ~params ~@body nil)}))

(defworker :hello [] (println "Hello, Clojure"))
(defworker :echo [& args] (apply println args))
