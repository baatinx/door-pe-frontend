(ns doorpe.frontend.admin-add.views.service
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [accountant.core :as accountant]
            [doorpe.frontend.util :refer [backend-domain]]
            [doorpe.frontend.auth.auth :as auth]
            [doorpe.frontend.db :as db]
            ["@material-ui/core" :refer [Grid Container Typography Card CardContent TextField Button MenuItem
                                         Select FormControl InputLabel  Grid Card CardContent CardAction]]))

(def all-categories (reagent/atom {}))

(defn fetch-all-categories
  []
  (go (let [url (str  backend-domain "/all-categories")
            res (<! (http/get url {:with-credentials? false}))
            categories (:body res)]
        (reset! all-categories {:catagories categories}))))

(defn add-service
  [{:keys [name category-id charge-type critical-service description]}]
  (go (let [url (str  backend-domain "/admin-add/service")
            res (<! (http/post url {:with-credentials? false
                                    :headers {"Authorization" (auth/set-authorization)}
                                    :form-params {:name name
                                                  :category-id category-id
                                                  :charge-type charge-type
                                                  :critical-service critical-service
                                                  :description description}}))]
        (accountant/navigate! "/dashboard"))))

(defn render-menu-items
  [{:keys [_id name]}]
  [:option {:value _id}
   name])

(defn service
  []
  (let [_ (fetch-all-categories)]
    (fn []
      (let [initial-values {:name "" :category-id "" :charge-type "" :critical-service "" :description ""}
            values (reagent/atom initial-values)]
        [:> Container {:maxWidth :sm}
         [:div
          [:> Card
           [:> CardContent
            [:> Typography "Service Name"]
            [:br]

            [:> TextField {:variant :outlined
                           :label "Service Name"
                           :on-change #(swap! values assoc :name (.. % -target -value))
                           :style {:width :500px}}]
            [:br]
            [:br]
            [:> Typography "Charge Type"]

            [:br]
            [:> TextField {:variant :outlined
                           :label "Charge Type"
                           :on-change #(swap! values assoc :charge-type (.. % -target -value))
                           :select true
                           :style {:width :200px}}

             [:> MenuItem {:value "fixed"}
              "Fixed"]
             [:> MenuItem {:value "variable"}
              "Variable"]]

            [:br]
            [:br]

            [:> Typography "Critical Service"]

            [:br]

            [:> TextField {:variant :outlined
                           :label "Critical Service"
                           :on-change #(swap! values assoc :critical-service (.. % -target -value))
                           :select true
                           :style {:width :200px}}
             [:> MenuItem {:value true}
              "True"]
             [:> MenuItem {:value false}
              "False"]]

            [:br]
            [:br]

            [:> Typography "Category Belongs to: "]
            [:br]
            [:> FormControl {:variant :outlined}
             [:> Select {:native true
                         :on-change #(swap! values assoc :category-id (.. % -target -value))
                         :style {:width :400px}}
              `[:optgroup ~@(map render-menu-items (:catagories @all-categories))]]]

            [:br]
            [:br]

            [:> Typography "Category Description"]
            [:br]
            [:> TextField {:variant :outlined
                           :label "Service Description"
                           :on-change #(swap! values assoc :description (.. % -target -value))
                           :multiline true
                           :placeholder " Add Service Description ..."
                           :rows :10
                           :style {:width :500px}}]
            [:br]
            [:br]
            [:> Button {:variant :contained
                        :color :secondary
                        :on-click #(add-service @values)}
             "Add Service"]]]]]))))