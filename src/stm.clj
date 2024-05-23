(ns stm)
;; Define references to shared mutable states
(def account-a (ref 100))
(def account-b (ref 200))
(def account-c (ref 200))

(comment
  (dosync
   (let [current-balance (get-balance from)]
     (if (> current-balance amount-to-transfer)
;;       <<<<<<< Thread switch >>>>>>>>
       (do
         (set-balance from (- current-balance amount-to-transfer))
         (set-balance to (+ current-balance amount-to-transfer)))
       (throw "Insufficient funds")))))

(defn prn-safe [& args]
  (locking *out*
    (apply println args)))

;; Function to transfer money from one account to another
(defn transfer [from-account to-account amount]
  (let [retry-count (atom 0)]
    (dosync
     (let [from-balance @from-account
           thread-name (format "%s(%s)"
                               (.getName (Thread/currentThread))
                               (swap! retry-count inc))]
       (prn-safe thread-name ": Attempting to transfer" amount "from" from-account "to" to-account "retries" @retry-count)
       (Thread/sleep 1000)
       (if (>= from-balance amount)
         (do
          ;; Simulate a delay to cause potential race conditions
           (alter from-account - amount) ;; from-account = from-acccount - amount
           (alter to-account + amount)
           (prn-safe thread-name ": Transfer successful. New balances:" @from-account @to-account))
         (do
           (prn-safe thread-name ": Insufficient funds. Transfer rolled back.")
           (throw (Exception. "Insufficient funds"))))))))

;; Main function to demonstrate two concurrent transactions
(defn -main []
  (prn-safe "Initial balances: Account-A =" @account-a ", Account-B =" @account-b)

  ;; Start two concurrent transfer transactions
  (doto (Thread. #(try (transfer account-a account-b 50)
                       (catch Exception e (prn-safe "Error:" (.getMessage e)))))
    (.setName "First")
    (.start))
  (doto (Thread. #(try (transfer account-a account-c 70)
                       (catch Exception e (prn-safe "Error:" (.getMessage e)))))
    (.setName "Second")
    (.start))

  ;; Wait for both threads to complete
  (Thread/sleep 3000) ;; Wait long enough for both threads to complete

  (prn-safe "Final balances: Account-A =" @account-a ", Account-B =" @account-b))

;; Call the main function to run the example
(-main)

(pmap (fn [a]
        (prn a))
      (range 1 100))
