(ns super-browser-tool.db.core-test
  (:require
   [super-browser-tool.db.core :refer [*db*] :as db]
   [java-time.pre-java8]
   [luminus-migrations.core :as migrations]
   [clojure.test :refer :all]
   ;; [clojure.java.jdbc :as jdbc]
   [next.jdbc :as jdbc]
   [super-browser-tool.config :refer [env]]
   [mount.core :as mount]))

;; (declare ^:dynamic *txn*)
;; (binding [next.jdbc.transaction/*nested-tx* :allow])

;; (def database-url (select-keys env [:database-url]))


(use-fixtures
  :once
  (fn [f]
    (mount/start
     #'super-browser-tool.config/env
     #'super-browser-tool.db.core/*db*)
    (migrations/migrate ["migrate"] (select-keys env [:database-url]))
    (f)))

(deftest test-users
  ;; (jdbc/with-transaction [t-conn *db* {:rollback-only true}]
  (jdbc/with-transaction [t-conn *db*]
    (println (select-keys env [:database-url]))
    (println t-conn)
    ;; (jdbc/execute! t-conn ["
;;     (jdbc/execute! database-url ["
;; INSERT INTO users
;; (first_name, last_name, email, password, role, created_at, updated_at, last_login)
;; VALUES ('first_name', 'last_name', 'email', 'password', 'role', now(), now(), now())
;;                            "])
    (is (= 1 (db/create-user!
              t-conn
              {
               :first_name "Sam"
               :last_name  "Smith"
               :email      "sam.smith@example.com"
               :role "user"
               :password       "pass"}
              {:connection t-conn}
              )))
    (is (= 1
           (count (db/get-users t-conn ))))
           ;; (count (db/get-users database-url ))))
    ;; (is (= {:id         1
    ;;         :first_name "Sam"
    ;;         :last_name  "Smith"
    ;;         :email      "sam.smith@example.com"
    ;;         :password       "pass"
    ;;         :admin      nil
    ;;         :last_login nil
    ;;         :is_active  nil}
    ;;        (db/get-user t-conn {:id 1} )))
    )
)

;;
;; (defn clear
;;   [test]
;;   (cleardb db) ;; delete all rows from all tables
;;   (try
;;     (test)
;;     (finally
;;       (cleardb db)))
;;
;;   )
;;
;; (defn setupdb [tests]
;;   (initdb db) ;; create the tables
;;   (try
;;     (tests)
;;     (finally
;;       (teardown db)))) ;; drop the tables
;;
;; (use-fixtures :each clear)
;; (use-fixtures :once setupdb)

;; (use-fixtures
;;   :each
;;   (fn [f]
;;     (jdbc/with-db-transaction
;;       [transaction (select-keys env [:database-url])]
;;       (jdbc/db-set-rollback-only! transaction)
;;       (binding [*txn* transaction] (f)))))




