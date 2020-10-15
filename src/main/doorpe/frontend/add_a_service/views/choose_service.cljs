(ns doorpe.frontend.add-a-service.views.choose-service
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [accountant.core :as accountant]
            [doorpe.frontend.db :as db]
            [doorpe.frontend.util :refer [backend-domain]]
            ["@material-ui/core" :refer [Grid Container Typography Card CardContent TextField Button MenuItem
                                         Select FormControl  Grid Card CardContent CardAction]]))

(def services (reagent/atom {:services nil}))

(defn render-services
  [{:keys [_id name description critical-service charge-type img]}]
  [:> Card {:variant :outlined
            :style {:max-width :400px
                    :margin "30px"}}
   [:> CardContent
     [:img {:src img
            :style {:height :250px}}]
    [:> Typography {:variant "h5"}
     name]

    [:br]
    [:> Typography {:variant "body1"}
     description]

    [:br]

    [:> Button {:variant :contained
                :color :primary
                :on-click #(do
                             (swap! db/app-db update-in [:add-a-service] assoc :service-id _id)
                             (swap! db/app-db update-in [:add-a-service] assoc :by-default-critical-service? critical-service)
                             (swap! db/app-db update-in [:add-a-service] assoc :charge-type charge-type))}
     "Select"]]])

(defn fetch-services
  []
  (go
    (let [category-id (get-in @db/app-db [:add-a-service :category-id])
          url (str backend-domain "/all-services-by-category-id/" category-id)
          res (<! (http/get url {:with-credentials? false}))
          _ (swap! services assoc :services (:body res))])))

(defn choose-service
  []
  (let [_ (fetch-services)]
    (fn []
      (let [services (:services @services)]
        [:<>
         [:> Button {:variant :contained
                     :color :secondary
                     :on-click #(swap! db/app-db update-in [:add-a-service] dissoc :category-id)}
          "Go Back"]
         [:div {:style {:display :flex}}
          `[:<> ~@(map render-services services)]]]))))