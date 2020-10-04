(ns doorpe.frontend.home-page.home-page
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent]
            [accountant.core :as accountant]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [doorpe.frontend.util :refer [backend-domain]]
            [doorpe.frontend.components.util :refer [two-br]]
            [doorpe.frontend.auth.auth :as auth]
            [doorpe.frontend.db :as db]
            [reagent.dom :as reagent-dom]))

(defn home-page
  []
  ;; (go (let [res (<! (http/post "https://www.googleapis.com/geolocation/v1/geolocate?key=AIzaSyAbe9OWF2GtQC2OaWv_ejspmj47novjdHw" {:with-credentials? false}))]
        ;; (js/alert res)))

  [:div "This is home page"])

(defn showPosition
  [position]
  (js/alert position.coords.latitude)
  (js/alert position.coords.longitude))

(defn getLocation
  []
  (js/navigator.geolocation.getCurrentPosition showPosition))



