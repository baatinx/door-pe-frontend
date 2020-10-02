(ns doorpe.frontend.book-a-service.views.choose-category
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [accountant.core :as accountant]
            [doorpe.frontend.db :as db]
            [doorpe.frontend.util :refer [backend-domain]]
            ["@material-ui/core" :refer [Grid Container Typography Card CardContent TextField Button MenuItem
                                         Select FormControl  Grid Card CardContent CardAction]]))

(def categories (reagent/atom {:categories nil}))

(defn render-categories
  [{:keys [_id category-name desc]}]
  [:> Card {:variant :outlined
            :style {:max-width :400px
                    :margin "30px"}}
   [:> CardContent
    [:> Typography {:variant "h6"}
     category-name]

    [:br]
    [:> Typography {:variant "button"}
     (str "Description : " desc)]

    [:br]

    [:> Button {:variant :contained
                :color :primary
                :on-click #(swap! db/app-db update-in [:book-a-service] assoc :category-id _id)}
     "Select"]]])

(defn fetch-categories
  []
  (go
    (let [url (str backend-domain "/all-categories")
          res (<! (http/get url {:with-credentials? false}))
          _ (swap! categories assoc :categories (:body res))])))

(defn choose-category
  []
  (let [_ (fetch-categories)
        categories (:categories @categories)]
    [:div {:style {:display :flex}}
     `[:<> ~@(map render-categories categories)]]))