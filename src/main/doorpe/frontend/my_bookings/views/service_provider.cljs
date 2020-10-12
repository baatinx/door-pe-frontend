(ns doorpe.frontend.my-bookings.views.service-provider
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [accountant.core :as accountant]
            [cljs.core.async :refer [<!]]
            [doorpe.frontend.auth.auth :as auth]
            [doorpe.frontend.db :as db]
            [doorpe.frontend.util :refer [backend-domain]]
            ["@material-ui/core" :refer [Grid Container Typography Card CardContent TextField Button MenuItem
                                         Select FormControl  Grid Card CardContent CardAction]]))

(def location-coords (atom {}))

(defn success
  [position]
  (let [coords position.coords
        latitude coords.latitude
        longitude coords.longitude]
    (reset! location-coords {:latitude (str latitude)
                             :longitude (str longitude)})))

(defn error
  [err]
  (js/console.log (str "ERROR: " err.code " Message: " err.message)))

(defn set-location-coords
  []
  (let [options {:enableHighAccuracy true}]
    (js/navigator.geolocation.getCurrentPosition success error)))

(defn accept-booking
  [booking-id]
  (go (let [url (str backend-domain "/accept-booking/" booking-id)
            res (<! (http/post url {:with-credentials? false
                                    :headers {"Authorization" (auth/set-authorization)}}))
            status (-> res
                       :body
                       :status)]
        (if status
          (accountant/navigate! "/my-bookings")
          (js/alert ":-( something went worng, please try again later)")))))

(defn reject-booking
  [booking-id]
  (go (let [url (str backend-domain "/reject-booking/" booking-id)
            res (<! (http/post url {:with-credentials? false
                                    :headers {"Authorization" (auth/set-authorization)}}))
            status (-> res
                       :body
                       :status)]
        (if status
          (accountant/navigate! "/my-bookings")
          (js/alert ":-( something went worng, please try again later)")))))

(defn render-my-bookings
  [{:keys [booking-id customer-name customer-contact customer-address service-name booking-on service-on service-time latitude longitude status]}]
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

    (let [url (str "https://www.google.com/maps/dir/"
                   (:latitude @location-coords) "," (:longitude @location-coords)
                   "/"
                   latitude "," longitude
                   "/")]
      [:> Button {:variant :contained
                  :color :secondary
                  :href url
                  :target "_blank"}
       "Location coords"])

    [:br]
    [:br]

    (if (= status "pending")
      [:<>
       [:> Button {:variant :contained
                   :color :secondary
                   :on-click #(accept-booking booking-id)}
        "Accept Booking"]
       [:br]
       [:br]
       [:> Button {:variant :contained
                   :color :secondary
                   :on-click #(reject-booking booking-id)}
        "Reject Booking"]])]])

(defn fetch-bookings
  []
  (go (let [res (<! (http/get "http://localhost:7000/my-bookings" {:with-credentials? false
                                                                   :headers {"Authorization" (auth/set-authorization)}}))
            bookings (:body res)
            c (count bookings)]
        (if  (> c 0)
          (swap! db/app-db assoc :my-bookings bookings)
          (swap! db/app-db assoc :my-bookings nil)))))

(defn service-provider
  []
  (let [_ (fetch-bookings)
        _ (set-location-coords)]
    (fn []
      (let [my-bookings (:my-bookings @db/app-db)]
        [:div {:style {:display :flex}}
         (if my-bookings
           `[:<> ~@(map render-my-bookings my-bookings)]
           [:div "no bookings"])]))))