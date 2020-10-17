(ns doorpe.frontend.admin-service-requests.admin-service-requests
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent]
            [cljs-http.client :as http]
            [doorpe.frontend.auth.auth :as auth]
            [cljs.core.async :refer [<!]]
            [accountant.core :as accountant]
            [doorpe.frontend.db :as db]
            [doorpe.frontend.util :refer [backend-domain]]
            ["@material-ui/core" :refer [Grid Container Typography Card CardContent TextField Button MenuItem
                                         Select FormControl  Grid Card CardContent CardAction]]))

(def service-requests (reagent/atom {:service-requests nil}))

(defn accept-service
  [_id]
  (go (let [url (str backend-domain "/admin-service-requests")
            res (<! (http/get url {:with-credentials? false
                                   :query-params {:action "approve-service"
                                                  :_id _id}
                                   :headers {"Authorization" (auth/set-authorization)}}))])
      (accountant/navigate! "/admin-service-requests")))


(defn reject-service
  [_id]
  (go (let [url (str backend-domain "/admin-service-requests")
            res (<! (http/get url {:with-credentials? false
                                   :query-params {:action "reject-service"
                                                  :_id _id}
                                   :headers {"Authorization" (auth/set-authorization)}}))])
      (accountant/navigate! "/admin-service-requests")))

(defn render-service-requests
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
                :on-click #(accept-service _id)}
     "Approve"]

    [:> Button {:variant :contained
                :color :secondary
                :on-click #(reject-service _id)}
     "Reject"]]])

(defn fetch-service-requests
  []
  (go
    (let [url (str backend-domain "/all-service-requests")
          res (<! (http/get url {:with-credentials? false
                                 :headers {"Authorization" (auth/set-authorization)}}))]
      (swap! service-requests assoc :service-requests (:body res)))))

(defn admin-service-requests
  []
  (let [_ (fetch-service-requests)]
    (fn []
      (let [service-requests (:service-requests @service-requests)]
        [:<>
         [:div {:style {:display :flex}}
          `[:<> ~@(map render-service-requests service-requests)]]]))))
