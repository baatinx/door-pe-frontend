(ns doorpe.frontend.nav.nav-links
    (:require ["@material-ui/core" :refer [Button]]))

(defn nav-link
  [{:keys [text on-click]}]
  [:> Button {:variant :default
              :on-click on-click
              :style {:display :inline-block
                      :color :white
                      :padding "20px 20px"
                      :margin "0 10px"}}
   text])