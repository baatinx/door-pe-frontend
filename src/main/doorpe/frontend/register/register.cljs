(ns doorpe.frontend.register.register
  (:require [accountant.core :as accountant]
            ["@material-ui/core" :refer [Grid Container Typography Card CardContent TextField Button MenuItem
                                         Select FormControl  Grid Card CardContent CardAction]]))

(defn register
  []
  [:<>
   [:> Container {:style {:text-align :center
                          :width :600px}}

    [:> Card {:variant :outlined
              :style {:max-width :400px
                      :margin "30px"
                      :text-align :center}}
     [:> Button {:variant "contained"
                 :color :primary
                 :style {:margin " 100px 50px"}
                 :on-click #(accountant/navigate! "/register/customer")}
      "Register as Customer"]]

    [:> Card {:variant :outlined
              :style {:max-width :400px
                      :margin "30px"
                      :text-align :center}}
     [:> Button {:variant "contained"
                 :color :secondary
                 :style {:margin " 100px 50px"}
                 :on-click #(accountant/navigate! "/register/service-provider")}
      "Register as service Provider"]]]])