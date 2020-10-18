(ns doorpe.frontend.provide-service.provide-service
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [accountant.core :as accountant]
            [doorpe.frontend.db :as db]
            [doorpe.frontend.provide-service.views.choose-category :refer  [choose-category]]
            [doorpe.frontend.provide-service.views.choose-service :refer  [choose-service]]
            [doorpe.frontend.provide-service.views.degree-holder-consent :refer  [degree-holder-consent]]
            [doorpe.frontend.provide-service.views.confirm-service :refer  [confirm-service]]))

(defn provide-service
  []
  (let [service-state (:provide-service @db/app-db)
        category-id (:category-id service-state)
        service-id (:service-id service-state)]
    (cond
      (not category-id) [choose-category]
      (not (and category-id service-id)) [choose-service]

      (and
       category-id
       service-id
       (= true (get-in @db/app-db [:provide-service :by-default-critical-service?]))) [confirm-service]


      (and
       category-id
       service-id
       (= false (get-in @db/app-db [:provide-service :by-default-critical-service?]))
       (= nil (get-in @db/app-db [:provide-service :degree-holder-consent?]))) [degree-holder-consent]

      (and
       category-id
       service-id
       (= false (get-in @db/app-db [:provide-service :by-default-critical-service?]))
       (or
        (= true (get-in @db/app-db [:provide-service :degree-holder-consent?]))
        (= false (get-in @db/app-db [:provide-service :degree-holder-consent?])))) [confirm-service])))
