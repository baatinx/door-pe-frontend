(ns doorpe.frontend.app
  (:require-macros [secretary.core :refer [defroute]])
  (:require [reagent.dom :as reagent-dom]
            [secretary.core :as secretary]
            [accountant.core :as accountant]
            [reagent.core :as reagent]

            [doorpe.frontend.nav.nav :refer [nav]]
            [doorpe.frontend.footer.footer :refer [footer]]

            [doorpe.frontend.handler :as handler]))

(defonce page (reagent/atom handler/home))

(defn current-page
  []
  [:div
   [nav]
   [@page]
   [footer]])

;; routes
(secretary/defroute "/" []
  (reset! page handler/home))

(secretary/defroute "/register-as-customer" []
  (reset! page handler/register-as-customer))

(secretary/defroute "/login" []
  (reset! page handler/login))

(secretary/defroute "/complaint" []
  (reset! page handler/complaint))

(secretary/defroute "/feedback" []
  (reset! page handler/feedback))

(defn ^:dev/after-load start
  []
  (accountant/configure-navigation!
   {:nav-handler (fn [path]
                   (secretary/dispatch! path))
    :path-exists? (fn [path]
                    (secretary/locate-route path))
    :reload-same-path? true})
  (reagent-dom/render [current-page]
                      (.getElementById js/document "application")))

(defn ^:export init
  []
  (start))