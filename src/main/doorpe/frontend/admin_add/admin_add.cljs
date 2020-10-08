(ns doorpe.frontend.admin-add.admin-add
  (:require [doorpe.frontend.db :as db]
            [doorpe.frontend.admin-add.views.category :refer [category]]
            [doorpe.frontend.admin-add.views.service :refer [service]]))

(defn admin-add
  []
  (let [admin-add (:admin-add @db/app-db)]
    (cond
      (= "category" admin-add) [category]
      (= "service" admin-add) [service])))