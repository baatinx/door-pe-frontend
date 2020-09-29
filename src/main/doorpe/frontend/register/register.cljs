(ns doorpe.frontend.register.register
  (:require [accountant.core :as accountant]))

(defn register
  []
  [:div
   [:button {:on-click #(accountant/navigate! "/register/customer")}
    "Register  customer"]
   [:br]
   [:br]
   [:br]
   [:br]
   [:button {:on-click #(accountant/navigate! "/register/service-provider")}
    "Register - service provider"]])