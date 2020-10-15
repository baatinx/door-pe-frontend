(ns doorpe.frontend.auth.login
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent]
            [accountant.core :as accountant]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [doorpe.frontend.util :refer [backend-domain check-validity]]
            [doorpe.frontend.components.util :refer [two-br]]
            [doorpe.frontend.auth.auth :as auth]
            [doorpe.frontend.db :as db]
            ["@material-ui/core" :refer [Container Typography TextField Button MenuItem Paper Link
                                         Select FormControl  Grid Card CardContent CardAction]]))
(defn do-login
  [{username :username password :password}]
  (go (let [url (str  backend-domain "/login")
            res (<! (http/post  url {:with-credentials? false
                                     :form-params {:username username
                                                   :password password}}))
            {:keys [token user-id user-type name]} (:body res)]
        (if token
          (do
            (reset! auth/auth-state {:authenticated? true
                                     :token token
                                     :user-id user-id
                                     :user-type (keyword user-type)
                                     :dispatch-view (keyword user-type)})
            (swap! db/app-db update-in [:my-profile] merge {:name name})
            (accountant/navigate! "/dashboard"))
          (do
            (js/alert ":-( Invalid Username/Password")
            (accountant/dispatch-current!))))))

(defn login []
  (let [initial-vaules {:username "" :password ""}
        values (reagent/atom initial-vaules)]
    [:> Container {:maxWidth "sm"}
     [:> Paper {:variant :outlined
                :square true}
      [:> Typography {:variant :h6}
       "Login"]

      [:br]
      [:> TextField {:variant :outlined
                     :id :username
                     :type :number
                     :label "Phone Number"
                     :InputProps {:inputProps {:min 0
                                               :max 9999999999}}
                     :required true
                     :on-change #(swap! values assoc :username (.. % -target -value))
                     :helperText "Phone Number should be of 10 digit"}]
      [two-br]
      [:> TextField {:variant :outlined
                     :required true
                     :label "Your Password"
                     :id :password
                     :on-change #(swap! values assoc :password (.. % -target -value))
                     :type :password
                     :helperText ""}]

      [two-br]

      [:> Button {:variant :contained
                  :color :primary
                  :on-click #(check-validity @values ["username" "password"] do-login)}
       "Login"]

      [:> Typography
       "Forgot password? "
       [:> Link {:on-click #(accountant/navigate! "/")
                 :style {:cursor :pointer}}
        " click here"]]

      [:br]

      [:> Typography
       "Don't have Account? Sign up "
       [:> Link {:on-click #(accountant/navigate! "/register")
                 :style {:cursor :pointer}}
        " here"]]]]))
