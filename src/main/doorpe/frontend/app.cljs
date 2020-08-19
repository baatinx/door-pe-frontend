(ns doorpe.frontend.app
  (:require [reagent.dom :as reagent]
            [reagent.core]
            ["@material-ui/core" :refer [Button Checkbox FormControlLabel TextField]]
            ["@material-ui/pickers" :refer [DatePicker]]))

(defn greet
  [name]
  (js/alert "welcome " name))

(defn two-br
  []
  [:<>
   [:br]
   [:br]])

(defn title
  "returns a 'p' tag with a custom message in it + few new-lines"
  [msg]
  [:<>
   [two-br]
   [two-br]
   [:p [:b msg]]])


(defn app
  []
  [:<>
   [:p [:b "Basic Button"]]
   [:> Button {:variant "contained"
               :color "primary"
               :on-click #(js/alert "clicked")}
    "click me"]

   [title "Checkboxes"]
   [:> Checkbox]
   [:> Checkbox {:disabled "true"}]
   [:> Checkbox {:checked true
                 :color "primary"}]
   
  
   [:> FormControlLabel {:control (reagent.core/as-element [:> Checkbox])
                         :label "Checkbox with label control"}] 
   
   [title "Text Boxes"]
   [:> TextField {:label "standard"}]
   
   [two-br]
   [:> TextField {:variant "outlined"
                  :label "outlined"}]
   
   [two-br]])

(defn ^:dev/after-load start
  []
  (reagent/render [app]
            (.getElementById js/document "root")))

(defn ^:export init
  []
  (start))