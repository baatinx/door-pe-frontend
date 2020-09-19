(ns doorpe.frontend.footer.views.authenticated
  (:require ["@material-ui/core" :refer [Container Typography]]))

(defn authenticated
  []
  [:div {:style {:margin-top :900px
                 :background-color :black}}
   [:> Container
    [:> Typography {:variant :h3
                    :style {:color :white
                            :text-align :center}}
     "This is authenticated Footer"]]])