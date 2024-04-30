(ns main)

(+ 1 2)

(eval (cons '+ (cons 1 (cons 2 nil))))

(rest (list 1 2 3))

(do
  (defn my-nth [lst n]
    (if (= n 0)
      (first lst)
      (my-nth (rest lst) (- n 1))))
  (my-nth (list 10 20 30) 1))
