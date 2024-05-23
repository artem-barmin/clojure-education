(ns lecture-macroses
  (:use clojure.walk))

(let [mp {:test 1 :nested {:other 10} :nested-vector [11 21 31]}
      {other :test} mp
      {{nested-other :other} :nested} mp
      {[first-nested second-nested] :nested-vector} mp
      {:keys [test]} mp]
  [other test nested-other first-nested second-nested])

(defmacro my-when [cond & body]
  `(if (not ~cond)
     (do
       ~@body)))

(defmacro my-when-simple [cond & body]
  (list 'if cond (cons 'do body)))

(defmacro my-when-test [cond & body]
  (prn cond body))

(defn my-test-fn-with-macro []
  (my-when true (prn "A")))

(my-test-fn-with-macro)

(defn macro-simulate []
  (list '+ 1 2))

(eval (macro-simulate))



(def global-var 1)

(defmacro my-macros [& body]
  `(let [global-var# 10]
     (prn global-var#)
     (do  ~@body)))

(defmacro my-macro-double-execute [cond1 cond2 & body]
  `(let [cond1-evaluated# ~cond1
         cond2-evaluated# ~cond2]
     (if cond1-evaluated#
       (if (and cond1-evaluated# cond2-evaluated#)
         (do ~@body)))))

(my-macro-double-execute
 (do (prn "First") 1)
 (do (prn "Second") 2)
 (prn "Execute"))

(my-when
 true
 (prn "First")
 (prn "Second"))

(with-db-context
  (query db  "SELECT * FROM table"))

(defn get-db-connection []
  "DB connection")

(defmacro with-db-context [db-var-name & body]
  `(let [~db-var-name (get-db-connection)]
     ~@body))

(with-db-context db
  (prn "First" db)
  (prn "Second"))

(macroexpand-all '(with-db-context
                    db
                    (prn "First" db)
                    (prn "Second")))

(my-when-simple (= (* 2 2) 4) (prn "A") (prn "B"))

(my-when true
         (prn "A")
         (prn "B"))

(macroexpand-all '(or (= 2 2) false true (= 3 3)))

(macroexpand-all
 '(my-when-simple true
                  (prn "1")
                  (prn "2")))

(defn- my-thread-last-fun [value forms]
  (prn value forms)
  (if (seq forms)
    (let [[form & rst] forms]
      (concat form [(my-thread-last-fun value rst)]))
    value))

(defmacro my-thread-last [value & forms]
  (my-thread-last-fun value (reverse forms)))

(macroexpand-all
 '(my-thread-last (do (prn "testl") [1 2 3])
                  (map inc)
                  (filter even?)
                  (map dec)))

(macroexpand-all
 '(my-thread-last [1 2 3]
                  (map inc)
                  (filter even?)))

(filter even? (map inc [1 2 3]))

`(+ 1 2)
(list 'clojure.core/+ 1 2)
(list '+ 1 2)
'(+ 1 2)