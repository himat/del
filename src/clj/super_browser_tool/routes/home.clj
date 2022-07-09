(ns super-browser-tool.routes.home
  (:require
   [super-browser-tool.layout :as layout]
   [super-browser-tool.db.core :as db]
   [clojure.java.io :as io]
   [super-browser-tool.middleware :as middleware]
   [ring.util.response]
   [ring.util.http-response :as response]
   [struct.core :as st]
   
   ))

(def user-schema
  [[:first_name
    st/required
    st/string]

   [:email
    st/required
    st/string
    {:message "email must be a valid email"
     :validate #(clojure.string/includes? % "@") }]

   ])

(defn validate-user [params]
  (first (st/validate params user-schema)
         ))

(defn save-user! [{:keys [params]}]
  (if-let [errors (validate-user params)]
    (-> (response/found "/")
        (assoc :flash (assoc params :errors errors)))
    (do 
      db/create-user!
      (assoc params :last_name "last" :password "password" :role "user")
      (response/found "/")

      )


    )

  )

(defn home-page [{:keys [flash] :as request }]
  ;; (layout/render request "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))
  (layout/render
    request
    "home.html"
    (merge {:users (db/get-users)}
           (select-keys flash [:first_name :email :errors])
           
           ))
  )

(defn about-page [request]
  (layout/render request "about.html"))

(defn home-routes []
  [ "" 
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/" {:get home-page
         :post save-user!}]
   ["/about" {:get about-page}]])

