(ns doorpe.frontend.admin-edit.admin-edit
  (:require [doorpe.frontend.db :as db]
            [doorpe.frontend.admin-edit.views.category :refer [category]]
            [doorpe.frontend.admin-edit.views.service :refer [service]]))

(defn admin-edit
  []
  (let [edit-what (:admin-edit @db/app-db)]
    (cond
      (= "category" edit-what) [category]
      (= "service" edit-what) [service])))