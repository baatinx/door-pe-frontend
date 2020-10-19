(ns doorpe.frontend.check-complaints.check-complaints
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent]
            [cljs-http.client :as http]
            [doorpe.frontend.auth.auth :as auth]
            [cljs.core.async :refer [<!]]
            [doorpe.frontend.components.no-data-found :refer [no-data-found]]
            [accountant.core :as accountant]
            [doorpe.frontend.util :refer [backend-domain]]
            ["@material-ui/core" :refer [Grid Container Typography Card CardContent TextField Button MenuItem Link
                                         Select FormControl  Grid Card CardContent CardAction]]))

(def complaints (reagent/atom {:complaints nil}))

(defn reply-to-complaint
  [complaint-id reply-box-id]
  (go (let [admin-reply (.-value (.getElementById js/document reply-box-id))

            url (str backend-domain "/complaint-reply")
            res (<! (http/post url  {:with-credentials? false
                                     :headers {"Authorization" (auth/set-authorization)}
                                     :form-params {:complaint-id complaint-id
                                                   :admin-reply admin-reply}}))
            _ (js/alert res)]
        (accountant/navigate! "/check-complaints"))))

(defn render-complaints
  [{:keys [_id user-name description]}]
  (let [prefix "reply-box-"
        reply-box-id (str prefix _id)]
    [:> Card {:variant :outlined
              :style {:max-width :400px
                      :margin "30px"}}
     [:> CardContent
      [:> Typography {:variant :caption}
       description]

      [:br]
      [:br]


      [:> Typography {:variant :caption}
       "complainant : "
       [:> Typography {:variant :caption
                       :color :secondary
                       :style {:font-weight :bold
                               :text-decoration :underline}}
        user-name]]

      [:br]
      [:br]

      [:> TextField {:variant :outlined
                     :label "Complaint reply"
                     :id reply-box-id
                     :fullWidth true
                     :multiline true
                     :placeholder "Admin your reply here ...."
                     :rows :10}]

      [:br]
      [:br]

      [:> Button {:variant :contained
                  :color :primary
                  :on-click #(reply-to-complaint _id reply-box-id)}
       "reply"]]]))

(defn fetch-complaints
  []
  (go
    (let [url (str backend-domain "/check-complaints")
          res (<! (http/get url {:with-credentials? false
                                 :headers {"Authorization" (auth/set-authorization)}}))
          _ (swap! complaints assoc :complaints (:body res))])))

(defn check-complaints
  []
  (let [_ (fetch-complaints)]
    (fn []
      (let [complaints (:complaints @complaints)]
        [:div {:style {:display :flex}}
         (if-not (empty? complaints)
           `[:<> ~@(map render-complaints complaints)]
           [no-data-found])]))))
