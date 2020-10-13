(ns doorpe.frontend.add-a-service.views.degree-holder-consent
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [accountant.core :as accountant]
            [doorpe.frontend.db :as db]
            [doorpe.frontend.util :refer [backend-domain]]
            ["@material-ui/core" :refer [Grid Container Paper Typography Card CardContent TextField Button MenuItem
                                         Select FormControl  Grid Card CardContent CardAction]]))

(defn degree-holder-consent
  []
  [:> Container {:maxWidth :sm}
   [:> Card
    [:> CardContent
     [:> Typography "Degree Holder Consent"]
     [:br]

     [:> Typography {:variant :caption}
      "Do you possed a valid professional degree?"]
     [:br ]
     [:br ]
     [:> Button {:variant :outlined
                 :on-click #(swap! db/app-db update-in [:add-a-service] assoc :degree-holder-consent? true)}
      "YES"]

     [:> Button {:variant :outlined
                 :on-click #(swap! db/app-db update-in [:add-a-service] assoc :degree-holder-consent? false)}
      "NO"]]]])
