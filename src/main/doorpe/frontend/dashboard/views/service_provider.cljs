(ns doorpe.frontend.dashboard.views.service-provider
  (:require [doorpe.frontend.db :as db]))

(defn service-provider
  []
  (let [username (-> @db/app-db
                     :my-profile
                     :name)]
    [:div
     (str "Welcome, " username)]))