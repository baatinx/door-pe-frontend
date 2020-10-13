(ns doorpe.frontend.add-a-service.add-a-service
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [accountant.core :as accountant]
            [doorpe.frontend.db :as db]
            [doorpe.frontend.add-a-service.views.choose-category :refer  [choose-category]]
            [doorpe.frontend.add-a-service.views.choose-service :refer  [choose-service]]
            [doorpe.frontend.add-a-service.views.degree-holder-consent :refer  [degree-holder-consent]]
            [doorpe.frontend.add-a-service.views.confirm-service :refer  [confirm-service]]))

(defn add-a-service
  []
  (let [service-state (:add-a-service @db/app-db)
        category-id (:category-id service-state)
        service-id (:service-id service-state)]
    (cond
      (not category-id) [choose-category]
      (not (and category-id service-id)) [choose-service]

      (and
       category-id
       service-id
       (= true (get-in @db/app-db [:add-a-service :by-default-critical-service?]))) [confirm-service]


      (and
       category-id
       service-id
       (= false (get-in @db/app-db [:add-a-service :by-default-critical-service?]))
       (= nil (get-in @db/app-db [:add-a-service :degree-holder-consent?]))) [degree-holder-consent]

      (and
       category-id
       service-id
       (= false (get-in @db/app-db [:add-a-service :by-default-critical-service?]))
       (or
        (= true (get-in @db/app-db [:add-a-service :degree-holder-consent?]))
        (= false (get-in @db/app-db [:add-a-service :degree-holder-consent?])))) [confirm-service])))
