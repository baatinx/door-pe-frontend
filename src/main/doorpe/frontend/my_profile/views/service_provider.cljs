(ns doorpe.frontend.my-profile.views.service-provider
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent]
            [accountant.core :as accountant]
            [doorpe.frontend.util :refer [backend-domain]]
            [doorpe.frontend.components.util :refer [two-br]]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [doorpe.frontend.auth.auth :as auth]
            ["@material-ui/core" :refer [Container Typography TextField Button MenuItem Paper
                                         Link Select FormControl Grid Card CardContent CardAction]]))

(def my-profile (reagent/atom {}))
(def location-coords (reagent/atom {}))

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

(defn fetch-service-provider-details
  []
  (go (let [url (str  backend-domain "/my-profile")
            res (<! (http/get url {:with-credentials? false
                                   :headers {"Authorization" (auth/set-authorization)}}))
            user-details (:body res)
            doc {:name (:name user-details)
                 :contact (:contact user-details)
                 :email (:email user-details)
                 :gender (:gender user-details)
                 :age (:age user-details)
                 :pin-code (:pin-code user-details)
                 :district (:district user-details)
                 :latitude (:latitude user-details)
                 :longitude (:longitude user-details)
                 :img (:img user-details)}]
        (reset! my-profile doc))))

(defn update-my-profile
  [updated-details]
  (go (let [url (str backend-domain "/update-my-profile")
            res  (<! (http/post url {:with-credentials? false
                                     :headers {"Authorization" (auth/set-authorization)}
                                     :form-params updated-details}))]
        (accountant/navigate! "/my-profile"))))

(defn service-provider
  []
  (let [_ (set-location-coords)
        _ (fetch-service-provider-details)]
    (fn []
      (let [customer @my-profile
            initial-values {:name (:name customer)
                            :contact (:contact customer)
                            :email (:email customer)
                            :age (:age customer)
                            :gender (:gender customer)
                            :pin-code (:pin-code customer)
                            :district (:district customer)
                            :latitude (:latitude customer)
                            :longitude (:longitude customer)
                            :img (:img customer)}
            values (reagent/atom initial-values)]
        [:> Container {:maxWidth "sm"}
         [:> Paper {:variant :outlined
                    :square true}
          [:> Typography {:variant :h6}
           "My Profile"]
          [:br]

          [:img {:src (:img initial-values)
                 :style {:width :100px
                         :border-radius :4px}}]

          [:> Grid {:container true
                    :alignItems :center
                    :spacing 6
                    :justify :space-around}
           [:> Grid {:item true
                     :xs 3}
            [:> Typography {:variant :body1}
             "user name"]]
           [:> Grid {:item true
                     :xs 9}
            [:> TextField {:variant :outlined
                           :placeholder (:name initial-values)
                           :type :text
                           :on-change #(swap! my-profile assoc :name (.. % -target -value))}]]]


          [:> Grid {:container true
                    :alignItems :center
                    :spacing 6
                    :justify :space-around}
           [:> Grid {:item true
                     :xs 3}
            [:> Typography {:variant :body1}
             "contact"]]
           [:> Grid {:item true
                     :xs 9}
            [:> TextField {:variant :outlined
                           :label (:contact initial-values)
                           :disabled true
                           :type :number
                           :helperText ""}]]]


          [:> Grid {:container true
                    :alignItems :center
                    :justify :space-around
                    :spacing 6}
           [:> Grid {:item true
                     :xs 3}
            [:> Typography {:variant :body1}
             "email ID"]]
           [:> Grid {:item true
                     :xs 9}
            [:> TextField {:variant :outlined
                           :placeholder (:email initial-values)
                           :type :email
                           :on-change #(swap! my-profile assoc :email (.. % -target -value))}]]]

          [:> Grid {:container true
                    :alignItems :center
                    :justify :space-around
                    :spacing 6}
           [:> Grid {:item true
                     :xs 3}
            [:> Typography {:variant :body1}
             "gender"]]
           [:> Grid {:item true
                     :xs 9}
            [:> TextField {:variant :outlined
                           :label (:gender initial-values)
                           :select :true
                           :on-change #(swap! my-profile assoc :gender (.. % -target -value))
                           :style {:width :200px}}
             [:> MenuItem {:value :male}
              "Male"]
             [:> MenuItem {:value :female}
              "Female"]]]]

          [:> Grid {:container true
                    :alignItems :center
                    :spacing 6
                    :justify :space-around}
           [:> Grid {:item true
                     :xs 3}
            [:> Typography {:variant :body1}
             "age"]]
           [:> Grid {:item true
                     :xs 9}
            [:> TextField {:variant :outlined
                           :placeholder (:age initial-values)
                           :type :number
                           :on-change #(swap! my-profile assoc :age (.. % -target -value))}]]]

          [:> Grid {:container true
                    :alignItems :center
                    :spacing 5
                    :justify :space-around}
           [:> Grid {:item true
                     :xs 3}
            [:> Typography {:variant :body1}
             "district"]]
           [:> Grid {:item true
                     :xs 9}
            [:> TextField {:variant :outlined
                           :label (:district initial-values)
                           :select :true
                           :on-change #(swap! my-profile assoc :district (.. % -target -value))
                           :style {:width :200px}}
             [:> MenuItem {:value :srinagar}
              "Srinagar"]
             [:> MenuItem {:value :jammu}
              "Jammu"]
             [:> MenuItem {:value :bandipora}
              "Bandipora "]
             [:> MenuItem {:value :baramullah}
              "Baramullah"]
             [:> MenuItem {:value :budgam}
              "Budgam"]
             [:> MenuItem {:value :ganderbal}
              "Ganderbal"]
             [:> MenuItem {:value :kulgam}
              "Kulgam"]
             [:> MenuItem {:value :kupwara}
              "Kupwara"]
             [:> MenuItem {:value :islamabad}
              "Islamabad"]
             [:> MenuItem {:value :pulwama}
              "Pulwama"]
             [:> MenuItem {:value :shopian}
              "Shopian"]
             [:> MenuItem {:value :doda}
              "Doda"]
             [:> MenuItem {:value :kathua}
              "Kathua"]
             [:> MenuItem {:value :kishtwar}
              "Kishtwar"]
             [:> MenuItem {:value :poonch}
              "Poonch"]
             [:> MenuItem {:value :rajouri}
              "Rajouri"]
             [:> MenuItem {:value :ramban}
              "Ramban"]
             [:> MenuItem {:value :reasi}
              "Reasi"]
             [:> MenuItem {:value :samba}
              "Samba"]
             [:> MenuItem {:value :udhampur}
              "Udhampur"]]]]

          [:> Grid {:container true
                    :alignItems :center
                    :spacing 5
                    :justify :space-around}
           [:> Grid {:item true
                     :xs 3}
            [:> Typography {:variant :body1}
             "pin code"]]
           [:> Grid {:item true
                     :xs 9}
            [:> TextField {:variant :outlined
                           :placeholder (:pin-code initial-values)
                           :type :number
                           :on-change #(swap! my-profile assoc :pin-code (.. % -target -value))}]]]

          [:> Grid {:container true
                    :alignItems :center
                    :spacing 5
                    :justify :flex-start}
           [:> Grid {:item true
                     :xs 12}
            [:> Typography {:variant :caption}
             "location coordinates "]
            [:> Typography {:variant :caption}
             (str "[ " (:latitude @values) " , ")]
            [:> Typography {:variant :caption}
             (str (:longitude @values) " ] ")]
            [:> Link {:variant :body2
                      :color :primary
                      :style {:cursor :pointer}
                      :on-click #(swap! my-profile merge @location-coords)}
             "update my home coordinates"]]]

          [:> Grid {:container true
                    :alignItems :center
                    :spacing 5
                    :justify :center}
           [:> Grid {:item true
                     :xs 12}
            [:> Button {:variant :contained
                        :color :primary
                        :on-click #(update-my-profile @values)}
             "Update Details"]]]]]))))
