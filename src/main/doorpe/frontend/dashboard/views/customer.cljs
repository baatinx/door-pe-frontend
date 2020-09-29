(ns doorpe.frontend.dashboard.views.customer
  (:require [doorpe.frontend.db :as db]))

(defn customer
  []
  (let [username (-> @db/app-db
                     :my-profile
                     :name)]
    [:div
     (str "Welcome, " username)]))