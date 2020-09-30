(ns doorpe.frontend.book-a-service.views.choose-category
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [accountant.core :as accountant]
            [doorpe.frontend.db :as db]
            ["@material-ui/core" :refer [Grid Container Typography Card CardContent TextField Button MenuItem
                                         Select FormControl  Grid Card CardContent CardAction]]))

(defn choose-category
  []
  [:> Button {:on-click #(swap! db/app-db update-in [:book-a-service] assoc :category-id "alkjdflkjadl")}
   "category"])