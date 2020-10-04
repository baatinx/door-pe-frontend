(ns doorpe.frontend.my-bookings.views.service-provider
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent]
            [cljs-http.client :as http]
            [accountant.core :as accountant]
            [cljs.core.async :refer [<!]]
            [doorpe.frontend.auth.auth :as auth]
            [doorpe.frontend.db :as db]
            [doorpe.frontend.util :refer [backend-domain]]
            ["@material-ui/core" :refer [Grid Container Typography Card CardContent TextField Button MenuItem
                                         Select FormControl  Grid Card CardContent CardAction]]))

(def my-bookings (reagent/atom {:my-bookings nil}))

(defn accept-booking
  [booking-id]
  (go (let [url (str backend-domain "/accept-booking/" booking-id)
            res (<! (http/post url {:with-credentials? false
                                    :headers {"Authorization" (auth/set-authorization)}}))
            status (-> res
                       :body
                       :status)]
        (if status
          (do
            (accountant/navigate! "/my-bookings"))
          (accountant/navigate! "/dashboard")))))

(defn render-my-bookings
  [{:keys [booking-id customer-name customer-contact customer-address service-name service-charge-type booking-on service-on service-time status]}]
  [:> Card {:variant :outlined
            :style {:max-width :400px
                    :margin "30px"}}
   [:> CardContent
    [:> Typography {:variant "h6"}
     service-name]

    [:br]
    [:> Typography {:variant "button"}
     (str "Customer Name : " customer-name)]

    [:br]

    [:> Typography {:variant "button"}
     (str "Contact : " customer-contact)]

    [:br]

    [:> Typography {:variant "button"}
     (str "Address : " customer-address)]

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

    (if (or (= status "pending"))
      [:> Button {:variant :contained
                  :color :secondary
                  :on-click #(accept-booking booking-id)}
       "Accept Booking"])]])

(defn fetch-bookings
  []
  (go (let [res (<! (http/get "http://localhost:7000/my-bookings" {:with-credentials? false
                                                                   :headers {"Authorization" (auth/set-authorization)}}))
            bookings (:body res)
            c (count bookings)]
        (and (> c 0)
             (swap! db/app-db assoc  :my-bookings bookings)))))

(defn service-provider
  []
  (let [_ (fetch-bookings)
        my-bookings (:my-bookings @db/app-db)]
    (if my-bookings
      [:div {:style {:display :flex}}
       `[:<> ~@(map render-my-bookings my-bookings)]]
      [:div "no bookings"])))