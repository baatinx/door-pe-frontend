(ns doorpe.frontend.admin-add.views.service
  (:require [doorpe.frontend.db :as db]
            ["@material-ui/core" :refer [Grid Container Typography Card CardContent TextField Button MenuItem
                                         Select FormControl  Grid Card CardContent CardAction]]))

(defn service
  []
  [:> Container {:maxWidth :sm}
   [:div
    [:> Card
     [:> CardContent
      [:> Typography "Service Name"]
      [:br]

      [:> TextField {:variant :outlined
                     :label "Service Name"
                     :style {:width :500px}}]
      [:br]
      [:br]
      [:> Typography "Charge Type"]

      [:br]

      [:> TextField {:variant :outlined
                     :label "Charge Type"
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

      [:> TextField {:variant :outlined
                     :label "Choose Category"
                     :select true
                     :style {:width :400px}}
       [:> MenuItem {:value true}
        "category 1"]
       [:> MenuItem {:value false}
        "category 2"]]

      [:br]
      [:br]

      [:> Typography "Category Description"]
      [:br]
      [:> TextField {:variant :outlined
                     :label "Service Description"
                     :multiline true
                     :placeholder " Add Service Description ..."
                     :rows :10
                     :style {:width :500px}}]
      [:br]
      [:br]
      [:> Button {:variant :contained
                  :color :secondary}
       "Add Service"]]]]])