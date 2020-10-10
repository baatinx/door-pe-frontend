(ns doorpe.frontend.register.register
  (:require [accountant.core :as accountant]
            ["@material-ui/core" :refer [Grid Container Typography Card CardContent TextField Button MenuItem
                                         Select FormControl  Grid Card CardContent CardAction]]))

(defn register
  []
  [:> Container
   [:> Grid {:container true
             :spacing 5
             :justify :space-around
             :alignItems :center}
    [:> Grid {:item true
              :xs 6}
     [:> Card
      [:> CardContent
       [:> Button {:variant "contained"
                   :color :primary
                   :on-click #(accountant/navigate! "/register/customer")}
        "Register as Customer"]]]]

    [:> Grid {:item true
              :xs 6}
     [:> Card
      [:> CardContent
       [:> Button {:variant "contained"
                   :color :secondary
                   :on-click #(accountant/navigate! "/register/service-provider")}
        "Register as service Provider"]]]]]])