(ns doorpe.frontend.pay-dues.pay-dues
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent]
            [cljs-http.client :as http]
            [doorpe.frontend.auth.auth :as auth]
            [cljs.core.async :refer [<!]]
            [accountant.core :as accountant]
            [doorpe.frontend.util :refer [backend-domain]]
            ["@material-ui/core" :refer [Grid Container Typography Card CardContent TextField Button MenuItem Link Paper
                                         Select FormControl  Grid Card CardContent CardAction]]))

(defn pay-pending-dues
  [{amount :amount}]
  (go (let [url (str backend-domain "/pay-dues")
            res (<! (http/post url  {:with-credentials? false
                                     :headers {"Authorization" (auth/set-authorization)}
                                     :form-params {:amount amount}}))]
        (accountant/navigate! "/"))))

(defn pay-dues
  []
  (let [initial-vaules {:amount ""}
        values (reagent/atom initial-vaules)]
    [:> Container {:maxWidth "sm"}
     [:> Paper {:variant :outlined
                :square true}
      [:> Typography {:variant :h6}
       "Pay Rupees"]

      [:> TextField {:variant :outlined
                     :label "Amount"
                     :type :text
                     :required true
                     :id :name
                     :on-change #(swap! values assoc :amount (.. % -target -value))
                     :helperText ""}]

      [:> Button {:variant :contained
                  :color :primary
                  :on-click #(pay-pending-dues @values)}
       "pay"]]]))

