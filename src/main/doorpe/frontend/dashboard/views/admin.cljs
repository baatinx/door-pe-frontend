(ns doorpe.frontend.dashboard.views.admin
  (:require [doorpe.frontend.db :as db]))

(defn admin
  []
  (let [username (-> @db/app-db
                     :my-profile
                     :name)]
    [:div
     (str "Welcome Admin, " username)]))