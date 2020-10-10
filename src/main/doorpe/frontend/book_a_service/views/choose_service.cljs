(ns doorpe.frontend.book-a-service.views.choose-service
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
  [{:keys [_id name description]}]
  [:> Card {:variant :outlined
            :style {:max-width :400px
                    :margin "30px"}}
   [:> CardContent
    [:> Typography {:variant "h5"}
     name]

    [:br]
    [:> Typography {:variant "body1"}
     description]

    [:br]

    [:> Button {:variant :contained
                :color :primary
                :on-click #(swap! db/app-db update-in [:book-a-service] assoc :service-id _id)}
     "Select"]]])

(defn fetch-services
  []
  (go
    (let [category-id (get-in @db/app-db [:book-a-service :category-id])
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
                     :on-click #(swap! db/app-db update-in [:book-a-service] dissoc :category-id)}
          "Go Back"]
         [:div {:style {:display :flex}}
          `[:<> ~@(map render-services services)]]]))))