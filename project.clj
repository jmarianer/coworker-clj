(defproject coworker "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 ; To use coworker from Maven Central, Uncomment the next line
                 ; and remove everything from here to the end of the file.
                 ;[io.kungfury/coworker "1.1.4"]

                 [org.jetbrains.kotlin/kotlin-stdlib-jre8 "1.2.71"]
                 [org.jetbrains.kotlinx/kotlinx-coroutines-core "0.24.0"]
                 [org.slf4j/slf4j-api "1.7.25"]
                 [com.zaxxer/HikariCP "3.1.0"]
                 [org.postgresql/postgresql "42.2.5"]
                 [com.jsoniter/jsoniter "0.9.23"]
                 ]
  :resource-paths ["/Users/rmarianer/code/Coworker/target/Coworker-1.1.4.jar"])
