(ns lecure-libs
  (:require [org.httpkit.server :as http]
            [ring.util.response :as response]))

;; Define a simple handler
(defn handler [request]
  (response/response "Hello, World!"))

;; Main function to start the server
(defn -main []
  (http/run-server handler {:port 3000})
  (println "Server started on port 3000"))

;; Call the main function to start the server
(-main)