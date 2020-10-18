(ns doorpe.frontend.book-complaint.book-complaint
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [accountant.core :as accountant]
            [doorpe.frontend.util :refer [backend-domain]]
            [doorpe.frontend.auth.auth :as auth]
            [doorpe.frontend.db :as db]
            ["@material-ui/core" :refer [Grid Container Typography Card CardContent TextField Button MenuItem
                                         Select FormControl InputLabel  Grid Card CardContent CardAction]]))

(def all-services (reagent/atom {}))

(defn fetch-all-services
  []
  (go (let [url (str  backend-domain "/all-services")
            res (<! (http/get url {:with-credentials? false}))
            services (map (fn [service]
                            {:_id (:_id service)
                             :name (:name service)})
                          (:body res))]
        (reset! all-services {:services services}))))

(defn register-complaint
  [{:keys [service-id description]}]
  (go (let [url (str  backend-domain "/book-complaint")
            res (<! (http/post url {:with-credentials? false
                                    :headers {"Authorization" (auth/set-authorization)}
                                    :form-params {:service-id service-id
                                                  :description description}}))]
        (accountant/navigate! "/dashboard"))))

(defn render-menu-items
  [{:keys [_id name]}]
  [:option {:value _id}
   name])

(defn book-complaint
  []
  (let [_ (fetch-all-services)]
    (fn []
      (let [initial-values {:service-id "" :description ""}
            values (reagent/atom initial-values)]
        [:> Container {:maxWidth :sm}
         [:> Card
          [:> CardContent
           [:> Typography "complaint regarding"]
           [:> FormControl {:variant :outlined}
            [:> Select {:native true
                        :on-change #(swap! values assoc :service-id (.. % -target -value))
                        :style {:width :400px}}
             `[:optgroup ~@(map render-menu-items (:services @all-services))]]]

           [:br]
           [:br]

           [:> Typography "please tell us about your complaint"]
           [:br]
           [:> TextField {:variant :outlined
                          :label "Complaint"
                          :on-change #(swap! values assoc :description (.. % -target -value))
                          :multiline true
                          :placeholder "Your text here ..."
                          :rows :10
                          :style {:width :500px}}]

           [:br]
           [:br]
           [:> Button {:variant :contained
                       :color :secondary
                       :on-click #(register-complaint  @values)}
            "register complaint"]]]]))))