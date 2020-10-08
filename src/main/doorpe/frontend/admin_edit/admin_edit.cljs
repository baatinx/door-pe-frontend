(ns doorpe.frontend.admin-edit.admin-edit
  (:require [doorpe.frontend.db :as db]
            [doorpe.frontend.admin-edit.views.category :refer [category]]
            [doorpe.frontend.admin-edit.views.service :refer [service]]))

(defn admin-edit
  []
  (let [admin-edit (:admin-edit @db/app-db)]
    (cond
      (= "category" admin-edit) [category]
      (= "service" admin-edit) [service])))