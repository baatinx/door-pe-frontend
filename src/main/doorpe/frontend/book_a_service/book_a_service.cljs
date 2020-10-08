(ns doorpe.frontend.book-a-service.book-a-service
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [accountant.core :as accountant]
            [doorpe.frontend.db :as db]
            [doorpe.frontend.book-a-service.views.choose-category :refer  [choose-category]]
            [doorpe.frontend.book-a-service.views.choose-service :refer  [choose-service]]
            [doorpe.frontend.book-a-service.views.choose-service-provider :refer  [choose-service-provider]]
            [doorpe.frontend.book-a-service.views.confirm-booking :refer  [confirm-booking]]))

(defn book-a-service
  []
  (let [booking-state (:book-a-service @db/app-db)
        category-id (:category-id booking-state)
        service-id (:service-id booking-state)
        service-provider-id (:service-provider-id booking-state)]
    (cond
      (not category-id) [choose-category]
      (not (and category-id service-id)) [choose-service]
      (not (and category-id service-id service-provider-id)) [choose-service-provider]
      (and category-id service-id service-provider-id) [confirm-booking])))