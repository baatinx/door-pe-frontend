(ns doorpe.frontend.my-bookings.views.customer
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
(defn cancel-booking
  [booking-id]
  (go (let [url (str backend-domain "/cancel-booking/" booking-id)
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
  [{:keys [booking-id service-provider-name service-provider-contact service-provider-address service-name service-charge-type booking-on service-on service-time status]}]
  [:> Card {:variant :outlined
            :style {:max-width :400px
                    :margin "30px"}}
   [:> CardContent
    [:> Typography {:variant "h6"}
     service-name]

    [:br]
    [:> Typography {:variant "button"}
     (str "Service Provider : " service-provider-name)]

    [:br]

    [:> Typography {:variant "button"}
     (str "Contact : " service-provider-contact)]

    [:br]

    [:> Typography {:variant "button"}
     (str "Address : " service-provider-address)]

    [:br]

    [:> Typography {:variant "button"}
     (str "Charge Type : " service-charge-type)]

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
                  :on-click #(cancel-booking booking-id)}
       "Cancel Booking"])]])

(defn fetch-bookings
  []
  (go (let [_ (reset! my-bookings {})
            res (<! (http/get "http://localhost:7000/my-bookings" {:with-credentials? false
                                                                   :headers {"Authorization" (auth/set-authorization)}}))
            bookings (:body res)
            c (count bookings)]
        (and (> c 0)
             (swap! db/app-db assoc  :my-bookings bookings)))))

(defn customer
  []
  (let [_ (fetch-bookings)
        my-bookings (:my-bookings @db/app-db)]
    (if my-bookings
      [:div {:style {:display :flex}}
       `[:<> ~@(map render-my-bookings my-bookings)]

       [:> Card {:variant :outlined
                 :style {:max-width :400px
                         :margin "30px"
                         :text-align :center}}
        [:> Button {:variant "contained"
                    :color :primary
                    :style {:margin " 100px 50px"}
                    :on-click #(do (swap! db/app-db assoc :book-a-service nil)
                                   (accountant/navigate! "/book-a-service"))}
         "Book a new service"]]]
      [:div "no bookings"])))
