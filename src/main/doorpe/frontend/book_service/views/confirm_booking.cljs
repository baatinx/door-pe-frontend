(ns doorpe.frontend.book-service.views.confirm-booking
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [accountant.core :as accountant]
            [reagent.core :as reagent]
            [doorpe.frontend.util :refer [backend-domain]]
            [doorpe.frontend.db :as db]
            [doorpe.frontend.auth.auth :as auth]
            ["@material-ui/core" :refer [Grid Container Paper Typography Card CardContent TextField Button MenuItem Checkbox
                                         Select FormControl  Grid Card CardContent CardAction]]))

(def location-coords (reagent/atom {}))

(defn success
  [position]
  (let [coords position.coords
        latitude coords.latitude
        longitude coords.longitude]
    (reset! location-coords {:current-latitude (str latitude)
                             :current-longitude (str longitude)})))

(defn error
  [err]
  (js/console.log (str "ERROR: " err.code " Message: " err.message)))

(defn set-location-coords
  []
  (let [options {:enableHighAccuracy true}]
    (js/navigator.geolocation.getCurrentPosition success error)))

(defn make-booking
  [{:keys [date time]}]
  (go (let [url (str backend-domain "/book-service")
            coords-checkbox? (.-checked (.getElementById js/document "coords-checkbox"))
            customer-id (:user-id @auth/auth-state)
            service-id (get-in @db/app-db [:book-service :service-id])
            service-provider-id (get-in @db/app-db [:book-service :service-provider-id])
            service-charges (get-in @db/app-db [:book-service :service-charges])
            charges (get-in @db/app-db [:book-service :charges])

            current-latitude (:current-latitude @location-coords)
            current-longitude  (:current-longitude @location-coords)
            doc {:customer-id customer-id
                 :service-provider-id service-provider-id
                 :service-id service-id
                 :service-on date
                 :service-time time
                 :service-charges service-charges
                 :charges charges}

            form-params (if coords-checkbox?
                          doc
                          (into doc
                                {:current-latitude current-latitude
                                 :current-longitude current-longitude}))

            res (<! (http/post url {:with-credentials? false
                                    :headers {"Authorization" (auth/set-authorization)}
                                    :form-params form-params}))
            insert-status (-> res
                              :body
                              :status)]
        (if insert-status
          (accountant/navigate! "/my-bookings")
          (do
            (js/alert ":-( Booking Failed, please try agian later")
            (swap! db/app-db :assoc :book-service nil)
            (accountant/navigate! "/dashboard"))))))

(defn confirm-booking
  []
  (let [_ (set-location-coords)
        initial-values (reagent/atom {:date "" :time ""})]
    [:> Container {:maxWidth "sm"}
     [:> Button {:variant :contained
                 :color :secondary
                 :on-click #(swap! db/app-db update-in [:book-service] dissoc :service-provider-id)}
      "back"]

     [:br]

     [:> Paper {:variant :outlined
                :square true}

      [:> Grid {:container true}
       [:> Grid {:item true
                 :xs 6}
        [:> Typography {:variant "button"}
         "Choose Booking Date"]]

       [:> Grid {:item true
                 :xs 6}
        [:> TextField
         {:variant :outlined
          :type :date
          :on-change #(swap! initial-values assoc :date (.. % -target -value))}]]]

      [:br]

      [:> Grid {:container true}
       [:> Grid {:item true
                 :xs 6}
        [:> Typography {:variant "button"}
         "Choose Booking Timming"]]

       [:> Grid {:item true
                 :xs 6}
        [:> TextField
         {:variant :outlined
          :type :time
          :on-change #(swap! initial-values assoc :time (.. % -target -value))}]]]


      [:br]

      [:> Checkbox {:id :coords-checkbox}]
      [:> Typography {:variant :caption
                      :style {:display :inline-block}} "Use my home coordinates instead of current"]

      [:br]

      [:> Grid {:container true
                :justify :center
                :align :center}
       [:> Grid {:item true
                 :xs 6}
        [:> Button {:variant :contained
                    :color :primary
                    :on-click #(let [service-on (-> @initial-values
                                                    :date
                                                    js/Date.)
                                     today   (doto (js/Date.) (.setHours 0 0 0 0))]
                                 (if (>= service-on today)
                                   (make-booking @initial-values)
                                   (js/alert ":-( please choose a valid date")))}
         "Confirm Booking"]]]]]))
