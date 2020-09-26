(ns doorpe.frontend.my-bookings.my-bookings
  (:require [doorpe.frontend.auth.auth :as auth]
            [doorpe.frontend.my-bookings.views.customer :refer [customer]]
            [doorpe.frontend.my-bookings.views.service-provider :refer [service-provider]]))

(defn my-bookings
  []
  (let [user-type (:user-type  @auth/auth-state)]
    (cond
      (= :customer user-type) [customer]
      (= :service-provider user-type) [service-provider])))