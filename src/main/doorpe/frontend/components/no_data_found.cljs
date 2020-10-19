(ns doorpe.frontend.components.no-data-found
  (:require ["@material-ui/core" :refer [Grid Container Grid]]))

(defn no-data-found
  []
  [:> Container {:maxWidth :xs
                 :style {:margin-top :20px}}
   [:> Grid {:container true}
    [:> Grid {:item true
              :align :center
              :xs 12}
     [:div
      [:img {:src "./img/no-data-found.png"
             :height :250px
             :width :auto}]
      [:br]]]]])