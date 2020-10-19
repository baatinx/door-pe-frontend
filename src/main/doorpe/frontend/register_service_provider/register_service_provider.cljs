(ns doorpe.frontend.register-service-provider.register-service-provider
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent]
            [accountant.core :as accountant]
            [doorpe.frontend.util :refer [backend-domain check-validity]]
            [doorpe.frontend.components.util :refer [two-br]]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            ["@material-ui/core" :refer [Container Typography TextField Button MenuItem Paper Checkbox Link
                                         Select FormControl  Grid Card CardContent CardAction]]))

(defn- js-promp-and-verify-user-otp?
  [expected-otp]
  (let [value (js/prompt "OTP has been sent, please Enter OTP that you recieved")
        actual-otp (js/parseInt value 10)]
    (if (= actual-otp expected-otp)
      true
      (and (js/alert "Invalid OTP") false))))

(defn dispatch-register-as-service-provider
  [{:keys [name contact district address password]}]
  (go (let [my-file  (-> (.getElementById js/document "my-file")
                         .-files first)
            otp-checkbox-checked? (.-checked (.getElementById js/document "otp-checkbox"))
            otp-method (if otp-checkbox-checked? "voice" "text")
            url (str  backend-domain "/send-otp/" contact "/" otp-method)
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
                                                                              :multipart-params [[:name name]
                                                                                                 [:contact contact]
                                                                                                 [:district district]
                                                                                                 [:address address]
                                                                                                 [:password password]
                                                                                                 [:user-type "service-provider"]
                                                                                                 ["my-file" my-file]]}))
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
  (let [initial-vaules {:name "" :contact "" :district "" :password "" :re-enter-password ""}
        values (reagent/atom initial-vaules)]
    [:> Container {:maxWidth "sm"}
     [:> Paper {:variant :outlined
                :square true}
      [:> Typography {:variant :h6}
       "Service Provider Registration"]

      [:br]

      [:div
       [:> TextField {:variant :outlined
                      :label "Full Name"
                      :type :text
                      :id :name
                      :required true
                      :on-change #(swap! values assoc :name (.. % -target -value))
                      :helperText ""}]
       [two-br]

       [:> TextField {:variant :outlined
                      :label "Phone number"
                      :id :contact
                      :type :number
                      :required true
                      :InputProps {:inputProps {:min 0
                                                :max 9999999999}}
                      :on-change #(swap! values assoc :contact (.. % -target -value))
                      :helperText ""}]
       [two-br]

       [:input {:type :file
                :id :my-file
                :required true}]
       [two-br]

       [:> TextField {:variant :outlined
                      :label "Choose District"
                      :select :true
                      :on-change #(swap! values assoc :district (.. % -target -value))
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
        [:> MenuItem {:value :pulwama}
         "Pulwama"]
        [:> MenuItem {:value :islamabad}
         "Islamabad"]
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
         "Udhampur"]]

       [two-br]

       [:> TextField {:variant :outlined
                      :required true
                      :id :password
                      :on-change #(swap! values assoc :password (.. % -target -value))
                      :label :password
                      :type :password
                      :helperText "Password must include at-least one digit and one uppercase letter"}]
       [two-br]

       [:> TextField {:variant :outlined
                      :on-change #(swap! values assoc :re-enter-password (.. % -target -value))
                      :label "Re Enter password"
                      :required true
                      :id :re-enter-password
                      :type :password
                      :helperText "Re Enter password"}]
       [two-br]

       [:<>
        [:> Checkbox {:id :otp-checkbox}]
        [:> Typography {:variant :caption
                        :style {:display :inline-block}} "Send me Voice OTP instead of text SMS"]
        [:br]
        [:> Checkbox {:id :terms-and-conditions-checkbox}]
        [:> Typography {:variant :caption
                        :style {:display :inline-block}} "I have read and agreed with the "]
        [:> Link {:href "#"}
         " terms and conditions *"]]

       [:br]
       [:br]

       [:> Button {:variant :contained
                   :color :primary
                   :on-click #(let [password (:password @values)
                                    re-enter-password (:re-enter-password @values)
                                    terms-and-conditions-checkbox? (.-checked (.getElementById js/document "terms-and-conditions-checkbox"))]
                                (if (= password re-enter-password)
                                  (if terms-and-conditions-checkbox?
                                    (check-validity @values ["name" "contact" "my-file" "password" "re-enter-password"] dispatch-register-as-service-provider)
                                    (js/alert "Please agree the terms and conditions"))
                                  (js/alert "Password Does not Match")))}
        "Register"]]]]))