(ns doorpe.frontend.my-profile.my-profile
  (:require [doorpe.frontend.auth.auth :as auth]
            [doorpe.frontend.my-profile.views.customer :refer [customer]]
            [doorpe.frontend.my-profile.views.service-provider :refer   [service-provider]]))

(defn my-profile
  []
  (let [user-type (:user-type  @auth/auth-state)]
    (cond
      (= :customer user-type) [customer]
      (= :service-provider user-type) [service-provider])))