(ns doorpe.frontend.auth.login
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as r]
            [accountant.core :as accountant]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [doorpe.frontend.util :refer [backend-domain]]
            [doorpe.frontend.components.util :refer [two-br]]
            [doorpe.frontend.auth.auth :as auth]
            [doorpe.frontend.db :as db]
            ["@material-ui/core" :refer [Container Typography TextField Button MenuItem
                                         Select FormControl  Grid Card CardContent CardAction]]))

(defn do-login
  [{username :username password :password}]
  (go (let [url (str  backend-domain "/login")
            res (<! (http/post  url {:with-credentials? false
                                     :form-params {:username username
                                                   :password password}}))
            {:keys [token user-id user-type name address latitude longitude]} (:body res)]
        (if token
          (do
            (reset! auth/auth-state {:authenticated? true
                                     :token token
                                     :user-id user-id
                                     :user-type (keyword user-type)
                                     :dispatch-view (keyword user-type)})
            (swap! db/app-db update-in [:my-profile] merge {:name name
                                                            :address address
                                                            :latitude latitude
                                                            :longitude longitude})
            (accountant/navigate! "/dashboard"))
          (do
            (js/alert ":-( Invalid Username/Password")
            (accountant/dispatch-current!))))))


(defn login []
  (let [initial-vaules {:username "" :password ""}
        values (r/atom initial-vaules)]
    [:> Container
     [:> Typography {:variant :h6}
      "Login"]

     [:br]

     [:> TextField {:variant :outlined
                    :label "Phone Number"
                    :id :username
                    :on-change #(swap! values assoc :username (.. % -target -value))
                    :helperText "Phone Number should be of 10 digit"}]
     [two-br]
     [:> TextField {:variant :outlined
                    :label "Your Password"
                    :id :password
                    :on-change #(swap! values assoc :password (.. % -target -value))
                    :type :password
                    :helperText ""}]
     [two-br]
     [:> Button {:variant :contained
                 :color :primary
                 :on-click #(do-login @values)}
      "Login"]]))

