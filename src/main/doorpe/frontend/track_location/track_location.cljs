(ns doorpe.frontend.track-location.track-location
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


(defn home-render []
  [:div {:style {:height "300px"}}])

(defn home-did-mount [this]
  (let [map-canvas (reagent-dom/dom-node this)
        map-options (clj->js {"center" (js/google.maps.LatLng. 34.064621, 74.818832)
                              "zoom" 8})]
    (js/google.maps.Map. map-canvas map-options)))

; AIzaSyAbe9OWF2GtQC2OaWv_ejspmj47novjdHw
; AIzaSyAbe9OWF2GtQC2OaWv_ejspmj47novjdHw

(defn track-location
  []
  (reagent/create-class {:reagent-render home-render
                         :component-did-mount home-did-mount}))


