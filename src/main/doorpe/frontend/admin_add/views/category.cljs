(ns doorpe.frontend.admin-add.views.category
  (:require [doorpe.frontend.db :as db]
            ["@material-ui/core" :refer [Grid Container Typography Card CardContent TextField Button MenuItem
                                         Select FormControl  Grid Card CardContent CardAction]]))

(defn category
  []
  [:> Container {:maxWidth :sm}
   [:div
    [:> Card
     [:> CardContent
      [:> Typography "Category Name"]
      [:br]

      [:> TextField {:variant :outlined
                     :label "Category Name"
                     :style {:width :500px}}]
      [:br]
      [:br]
      [:br]

      [:> Typography "Category Description"]
      [:br]
      [:> TextField {:variant :outlined
                     :label "Category Description"
                     :multiline true
                     :placeholder " Add Category Description ..."
                     :rows :10
                     :style {:width :500px}}]
      [:br]
      [:br]
      [:> Button {:variant :contained
                  :color :secondary}
       "Add Category"]]]]])