(ns doorpe.frontend.admin-add.views.category
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [accountant.core :as accountant]
            [doorpe.frontend.util :refer [backend-domain]]
            [doorpe.frontend.auth.auth :as auth]
            [doorpe.frontend.db :as db]
            ["@material-ui/core" :refer [Grid Container Typography Card CardContent TextField Button MenuItem
                                         Select FormControl  Grid Card CardContent CardAction]]))

(defn add-category
  [{:keys [name description]}]
  (go (let [my-file  (-> (.getElementById js/document "my-file")
                         .-files first)
            url (str  backend-domain "/admin-add/category")
            res (<! (http/post url {:with-credentials? false
                                    :headers {"Authorization" (auth/set-authorization)}
                                    :multipart-params [[:name name]
                                                       [:description description]
                                                       ["my-file" my-file]]}))]
        (accountant/navigate! "/dashboard"))))

(defn category
  []
  (let [initial-values {:name "" :description ""}
        values (reagent/atom initial-values)]
    [:> Container {:maxWidth :sm}
     [:div
      [:> Card
       [:> CardContent
        [:> Typography "Category Name"]
        [:br]

        [:> TextField {:variant :outlined
                       :label "Category Name"
                       :on-change #(swap! values assoc :name (.. % -target -value))
                       :style {:width :500px}}]
        [:br]
        [:br]
        [:br]

        [:> Typography "Category Description"]
        [:br]
        [:> TextField {:variant :outlined
                       :label "Category Description"
                       :on-change #(swap! values assoc :description (.. % -target -value))
                       :multiline true
                       :placeholder " Add Category Description ..."
                       :rows :10
                       :style {:width :500px}}]
        [:br]
        [:br]

        [:input {:type :file
                 :id :my-file}]

        [:br]
        [:br]

        [:> Button {:variant :contained
                    :color :secondary
                    :on-click #(add-category @values)}
         "Add Category"]]]]]))