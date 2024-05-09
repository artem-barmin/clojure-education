(ns main)

(def global-scope 1)

(defn test-closure [external-scope]
  (let [test1 1020]
    (fn nested []
      (let [local-scope 3]
        (prn global-scope test1 external-scope local-scope)))))

((test-closure 2))
((test-closure 3))
((test-closure 4))
((test-closure 5))


(let [nested-fn (test-closure 3)]
  (with-redefs [global-scope 10]
    (nested-fn)))