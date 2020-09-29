(ns doorpe.frontend.my-bookings.views.customer
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [doorpe.frontend.auth.auth :as auth]
            [doorpe.frontend.db :as db]))

(defn fetch-bookings
  []
  (go (let [res (<! (http/get "http://localhost:7000/my-bookings" {:with-credentials? false
                                                                   :headers {"Authorization" (auth/set-authorization)}}))
            bookings (:body res)
            c (count bookings)]
        (and (> c 0)
             (swap! db/app-db update-in [:my-bookings] bookings)))))

(defn customer
  []
  (let [_ (fetch-bookings)]
    (if (:my-bookings @db/app-db)
      [:div "alksdlfja" @db/app-db "---------"]
      [:div "no bookings"])))