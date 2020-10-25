(ns doorpe.frontend.revenue-generated.revenue-generated
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [doorpe.frontend.auth.auth :as auth]
            [doorpe.frontend.util :refer [backend-domain]]
            ["@material-ui/core" :refer [Grid Container Typography Card CardContent TextField Button MenuItem Tooltip Link Paper Divider
                                         Select FormControl  Grid Card CardContent CardAction]]))

(def revenue (reagent/atom {:revenue nil}))

(defn render-payment-details
  [{:keys [transaction-id paid-on amount]}]
  [:<>
   [:> Grid {:item true
             :xs 10}
    [:> Typography {:variant :caption}
     (str "received on - " paid-on ", transaction-id " transaction-id)]]
   [:> Grid {:item true
             :xs 2}
    [:> Typography {:variant :caption
                    :color :primary}
     (str  "+" amount)]]])

(defn fetch-revenue-generated
  []
  (go
    (let [url (str backend-domain "/revenue-generated")
          res (<! (http/get url {:with-credentials? false
                                 :headers {"Authorization" (auth/set-authorization)}}))
          _ (swap! revenue assoc :revenue (:body res))])))

(defn revenue-generated
  []
  (let [_ (fetch-revenue-generated)]
    (fn []
      (let [revenue-generated (:revenue @revenue)
            total-bookings-count (:total-bookings-count revenue-generated)
            charges-per-booking (:charges-per-booking revenue-generated)
            total-revenue-generated (:total-revenue-generated revenue-generated)
            payments (:payments revenue-generated)
            total-amount-received (:total-amount-received revenue-generated)]
        [:> Container {:maxWidth :sm}
         [:> Paper
          [:br]
          [:> Typography {:variant :caption
                          :color :secondary}
           (str "*DoorPe charges per booking " charges-per-booking " rupees")]

          [:br]
          [:br]
          [:> Grid {:container true
                    :justify :center}
           [:> Grid {:item true
                     :xs 10}
            "Total Bookings"]
           [:> Grid {:item true
                     :xs 2}
            total-bookings-count]

           [:> Grid {:item true
                     :xs 10}
            (str "Total Revenue Generated ( " total-bookings-count " * " charges-per-booking  " )")]
           [:> Grid {:item true
                     :xs 2}
            total-revenue-generated]


           `[:<> ~@(map render-payment-details payments)]


           [:> Grid {:item true
                     :xs 10}
            "Total amount received"]
           [:> Grid {:item true
                     :xs 2}
            [:> Typography {:variant :caption
                            :color :primary
                            :style {:font-weight :bold}}
             total-amount-received]]

           [:> Grid {:item true
                     :xs 10}
            "Outstanding Amount"]

           [:> Grid {:item true
                     :xs 2}
            [:> Typography {:variant :caption
                            :color :secondary
                            :style {:font-weight :bold}}
             (- total-revenue-generated total-amount-received)]]]]]))))