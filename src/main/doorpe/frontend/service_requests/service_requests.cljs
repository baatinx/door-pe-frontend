(ns doorpe.frontend.service-requests.service-requests
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent]
            [cljs-http.client :as http]
            [doorpe.frontend.auth.auth :as auth]
            [doorpe.frontend.components.no-data-found :refer [no-data-found]]
            [cljs.core.async :refer [<!]]
            [accountant.core :as accountant]
            [doorpe.frontend.util :refer [backend-domain]]
            ["@material-ui/core" :refer [Grid Container Typography Card CardContent TextField Button MenuItem
                                         Select FormControl  Grid Card CardContent CardAction]]))

(def all-service-requests (reagent/atom {:all-service-requests nil}))

(defn approve-service-request
  [_id]
  (go (let [url (str backend-domain "/approve-service-request")
            res (<! (http/get url {:with-credentials? false
                                   :query-params {:_id _id}
                                   :headers {"Authorization" (auth/set-authorization)}}))])
      (accountant/navigate! "/service-requests")))


(defn reject-service-request
  [_id]
  (go (let [url (str backend-domain "/reject-service-request")
            res (<! (http/get url {:with-credentials? false
                                   :query-params {:_id _id}
                                   :headers {"Authorization" (auth/set-authorization)}}))])
      (accountant/navigate! "/service-requests")))

(defn render-service-request
  [{:keys [_id service-provider-name service-name service-info]}]
  [:> Card {:variant :outlined
            :style {:max-width :400px
                    :margin "30px"}}
   [:> CardContent
    [:img {:src (:certificate-img service-info)
           :style {:height :200px}}]
    [:> Typography {:variant "h5"}
     service-provider-name]

    [:> Typography {:variant "body1"}
     service-name]

    [:> Button {:variant :contained
                :color :primary
                :on-click #(approve-service-request _id)}
     "Approve"]

    [:> Button {:variant :contained
                :color :secondary
                :on-click #(reject-service-request _id)}
     "Reject"]]])

(defn fetch-all-service-requests
  []
  (go
    (let [url (str backend-domain "/all-service-requests")
          res (<! (http/get url {:with-credentials? false
                                 :headers {"Authorization" (auth/set-authorization)}}))]
      (swap! all-service-requests assoc :all-service-requests (:body res)))))

(defn service-requests
  []
  (let [_ (fetch-all-service-requests)]
    (fn []
      (let [all-service-requests (:all-service-requests @all-service-requests)]
        (if (not-empty all-service-requests)
          [:div {:style {:display :flex}}
           `[:<> ~@(map render-service-request all-service-requests)]]
          [no-data-found])))))
