(ns doorpe.frontend.my-bookings.views.customer
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [accountant.core :as accountant]
            [cljs.core.async :refer [<!]]
            [doorpe.frontend.auth.auth :as auth]
            [doorpe.frontend.components.no-data-found :refer [no-data-found]]
            [doorpe.frontend.db :as db]
            [doorpe.frontend.util :refer [backend-domain]]
            ["@material-ui/core" :refer [Grid Container Typography Card CardContent TextField Button MenuItem
                                         Select FormControl  Grid Card CardContent CardAction]]))

(defn cancel-booking
  [booking-id]
  (go (let [url (str backend-domain "/cancel-booking/" booking-id)
            res (<! (http/post url {:with-credentials? false
                                    :headers {"Authorization" (auth/set-authorization)}}))
            status (-> res
                       :body
                       :status)]
        (if status
          (accountant/navigate! "/my-bookings")
          (do
            (js/alert ":-( something went wrong")
            (accountant/navigate! "/dashboard"))))))

(defn render-my-bookings
  [{:keys [booking-id service-provider-name service-name booking-on service-on service-time status img]}]
  [:> Card {:variant :outlined
            :style {:max-width :400px
                    :margin "30px"}}
   [:> CardContent
    [:> Typography {:variant "h6"}
     service-name]

    [:br]
    [:> Typography {:variant "button"}
     (str "Service Provider : " service-provider-name)]

    [:img {:src img
           :style {:height :80px}}]

    [:br]

    [:> Typography {:variant "button"}
     (str "Booking made on : " booking-on)]

    [:br]

    [:> Typography {:variant "button"}
     (str "Service on : " service-on)]

    [:br]

    [:> Typography {:variant "button"}
     (str "Service time : " service-time)]

    [:br]

    [:> Typography {:variant "button"}
     (str "Status : " status)]
    [:br]
    [:br]

    (and (= status "pending")
         [:> Button {:variant :contained
                     :color :secondary
                     :on-click #(cancel-booking booking-id)}
          "Cancel Booking"])]])

(defn fetch-bookings
  []
  (go (let [res (<! (http/get "http://localhost:7000/my-bookings" {:with-credentials? false
                                                                   :headers {"Authorization" (auth/set-authorization)}}))
            bookings (:body res)
            c (count bookings)]
        (if (> c 0)
          (swap! db/app-db assoc  :my-bookings bookings)
          (swap! db/app-db assoc :my-bookings nil)))))

(defn customer
  []
  (let [_ (fetch-bookings)]
    (fn []
      (let [my-bookings (:my-bookings @db/app-db)]
        [:div {:style {:display :flex}}
         (if my-bookings
           `[:<> ~@(map render-my-bookings my-bookings)]
           [no-data-found])]))))