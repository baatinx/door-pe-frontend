(ns doorpe.frontend.footer.views.public
  (:require ["@material-ui/core" :refer [Container Typography]]))

(defn public
  []
  [:div {:style {:margin-top :900px
                            :background-color :black}}
   [:> Container
    [:> Typography {:variant :h3
                    :style {:color :white
                            :text-align :center}}
     "This is public Footer"]]])