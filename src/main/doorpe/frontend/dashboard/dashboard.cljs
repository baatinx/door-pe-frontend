(ns doorpe.frontend.dashboard.dashboard
  (:require [doorpe.frontend.auth.auth :as auth]
            [doorpe.frontend.dashboard.views.customer :refer [customer]]
            [doorpe.frontend.dashboard.views.service-provider :refer   [service-provider]]))

(defn dashboard
  []
  (let [user-type (:user-type  @auth/auth-state)]
    (cond
      (= :customer user-type) [customer]
      (= :service-provider user-type) [service-provider])))