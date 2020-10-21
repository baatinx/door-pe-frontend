(ns doorpe.frontend.pending-dues.pending-dues
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [accountant.core :as accountant]
            [doorpe.frontend.auth.auth :as auth]
            [clojure.string :as string]
            [doorpe.frontend.db :as db]
            [doorpe.frontend.util :refer [backend-domain]]
            ["@material-ui/core" :refer [Grid Container Typography Card CardContent TextField Button MenuItem Tooltip Link Paper Divider
                                         Select FormControl  Grid Card CardContent CardAction]]))

(def dues (reagent/atom {:dues nil}))

(defn render-payment-details
  [{:keys [transaction-id paid-on amount]}]
  [:<>
   [:> Grid {:item true
             :xs 10}
    [:> Typography {:variant :caption}
     (str "paid on - " paid-on ", transaction-id " transaction-id)]]
   [:> Grid {:item true
             :xs 2}
    [:> Typography {:variant :caption
                    :color :primary}
     (str  "-" amount)]]])

(defn fetch-pending-dues
  []
  (go
    (let [url (str backend-domain "/pending-dues")
          res (<! (http/get url {:with-credentials? false
                                 :headers {"Authorization" (auth/set-authorization)}}))
          _ (swap! dues assoc :dues (:body res))])))

(defn pending-dues
  []
  (let [_ (fetch-pending-dues)]
    (fn []
      (let [pending-dues (:dues @dues)
            total-bookings-count (:total-bookings-count pending-dues)
            charges-per-booking (:charges-per-booking pending-dues)
            total-amount-due (:total-amount-due pending-dues)
            payments (:payments pending-dues)
            total-amount-paid (:total-amount-paid pending-dues)]
        [:> Container {:maxWidth :sm}
         [:> Paper
          [:br]
          [:> Typography {:variant :caption
                          :color :secondary}
           (str "*DoorPe charges per booking - " charges-per-booking " rupees")]

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
            (str "Total amount due ( " total-bookings-count " * " charges-per-booking  " )")]
           [:> Grid {:item true
                     :xs 2}
            total-amount-due]


           `[:<> ~@(map render-payment-details payments)]


           [:> Grid {:item true
                     :xs 10}
            "Total amount paid"]
           [:> Grid {:item true
                     :xs 2}
            [:> Typography {:variant :caption
                            :color :primary
                            :style {:font-weight :bold}}
             total-amount-paid]]

           [:> Grid {:item true
                     :xs 10}
            "pending dues"]

           [:> Grid {:item true
                     :xs 2}
            [:> Typography {:variant :caption
                            :color :secondary
                            :style {:font-weight :bold}}
             (- total-amount-due total-amount-paid)]]]]]))))