(ns doorpe.frontend.nav.nav
  (:require [doorpe.frontend.auth.auth :as auth]
            [doorpe.frontend.nav.views.public :refer [public]]
            [doorpe.frontend.nav.views.customer :refer [customer]]
            [doorpe.frontend.nav.views.service-provider :refer [service-provider]]
            [doorpe.frontend.nav.views.admin :refer [admin]]))

(defn nav
  []
  (let [{authenticated? :authenticated? :as au} @auth/auth-state]
    (if authenticated?
      (cond
        (= :customer (:dispatch-view au)) [customer]
        (= :service-provider (:dispatch-view au)) [service-provider]
        (= :admin (:dispatch-view au)) [admin]
        :else [public])
      [public])))
