(ns doorpe.frontend.register-service-provider.register-service-provider
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as r]
            [accountant.core :as accountant]
            [doorpe.frontend.util :refer [backend-domain]]
            [doorpe.frontend.components.util :refer [two-br]]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            ["@material-ui/core" :refer [Container Typography TextField Button MenuItem
                                         Select FormControl  Grid Card CardContent CardAction]]))

(defn- js-promp-and-verify-user-otp?
  [expected-otp]
  (let [value (js/prompt "OTP has been sent, please Enter OTP that you recieved")
        actual-otp (js/parseInt value 10)]
    (if (= actual-otp expected-otp)
      true
      (and (js/alert "Invalid OTP") false))))

(defn dispatch-register-as-customer
  [{:keys [name contact district address password]}]
  (go (let [url (str  backend-domain "/send-otp/" contact)
            response (<! (http/get url {:with-credentials? false}))
            is-everything-ok? (and (= 200 (:status response))
                                   (:success response)
                                   (-> response
                                       :body
                                       :success))
            expected-otp (-> response
                             :body
                             :expected-otp)]
        (if (and is-everything-ok?
                 expected-otp
                 (js-promp-and-verify-user-otp? expected-otp))
          (let [db-response (<!  (http/post (str backend-domain "/register") {:with-credentials? false
                                                                              :form-params {:name name
                                                                                            :contact contact
                                                                                            :district district
                                                                                            :address address
                                                                                            :password password
                                                                                            :user-type "service-provider"}}))
                inserted? (get-in db-response [:body :insert-status])]
            (if inserted?
              (do
                (js/alert "Account Created Successfully")
                (accountant/navigate! "/login"))
              (do
                (js/alert "Server Down, please try after some time :-(")
                (accountant/navigate! "/"))))
          (accountant/navigate! "/")))))

(defn register-service-provider []
  (let [initial-vaules {:name "" :contact "" :district "" :address "" :password "" :password-match ""}
        values (r/atom initial-vaules)]
    [:> Container {:maxWidht "xs"
                   :style {:margin-top :20px}}
     [:> Typography {:variant :h6}
      "Service Provider Registration"]

     [:br]

     [:div
      [:> TextField {:variant :outlined
                     :label "Full Name"
                     :type :text
                     :id :name
                     :on-change #(swap! values assoc :name (.. % -target -value))
                     :helperText ""}]
      [two-br]

      [:> TextField {:variant :outlined
                     :label "Phone number"
                     :type :number
                     :on-change #(swap! values assoc :contact (.. % -target -value))
                     :id :contact
                     :helperText ""}]
      [two-br]

      [:> TextField {:variant :outlined
                     :label "Choose District"
                     :select :true
                     :on-change #(swap! values assoc :district (.. % -target -value))
                     :id :address
                     :helperText ""
                     :style {:width :200px}}
       [:> MenuItem {:value :Srinagar}
        "Srinagar"]
       [:> MenuItem {:value :Islamabad}
        "Islamabad"]
       [:> MenuItem {:value :Baramullah}
        "Baramullah"]
       [:> MenuItem {:value :Kupwara}
        "Kupwara"]
       [:> MenuItem {:value :kishtiwar}
        "kishtiwar"]]

      [two-br]

      [:> TextField {:variant :outlined
                     :label "Full Address"
                     :on-change #(swap! values assoc :address (.. % -target -value))
                     :id :address
                     :helperText ""}]
      [two-br]

      [:> TextField {:variant :outlined
                     :on-change #(swap! values assoc :password (.. % -target -value))
                     :label :password
                     :type :password
                     :id :password
                     :helperText "Password must include at-least one digit and one uppercase letter"}]
      [two-br]

      [:> Button {:variant :contained
                  :color :primary
                  :on-click #(dispatch-register-as-customer @values)}
       "Register"]]]))