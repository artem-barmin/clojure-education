(ns lecture-state
  (:require  [cognitect.transit :as transit])
  (:import [java.io ByteArrayInputStream ByteArrayOutputStream]))

(#{:test 1 2} :test1)
(some (fn [a] (= a 1)) [1 2 3])
([1 2 3] 1)

(defn my-intersection [set1 set2]
  (filter set1 set2))

(my-intersection #{1 2 3} #{2 3 5})

(map :test [{:test 1} {:test 2}])

(def obj {:name "Alice"
          :age 30
          :address {:city "Wonderland"
                    :street "Rabbit Hole"
                    :postal-code "12345"}
          :phones ["123-456-7890" "987-654-3210"]
          :emails ["alice@example.com" "alice@wonderland.com"]})

(def out (ByteArrayOutputStream. 4096))
(def writer (transit/writer out :json))
(transit/write writer obj)

;; Take a peek at the JSON
(prn (.toString out))

(def test (atom nil))

(reset! test 1)
(swap! test inc)

;; Define a dynamic variable
(def ^:dynamic *greeting* "Hello")

(defn test1 [a]
  (fn [] (prn a)))

(def delayed-fn (test1 "Artem"))

(delayed-fn)

;; Function to print a greeting message
(defn greet [name]
  (println (str *greeting* ", " name "!")))

;; Main function to demonstrate binding
(greet "Alice") ;; Should print "Hello, Alice!"

  ;; Temporarily change the greeting using binding
(binding [*greeting* "Hi"]
  (greet "Bob")) ;; Should print "Hi, Bob!"

    ;; Nested binding
(binding [*greeting* "Hey"]
  (greet "Charlie") ;; Should print "Hey, Charlie!"))

    ;; Back to the outer binding
  (greet "Dave")) ;; Should print "Hi