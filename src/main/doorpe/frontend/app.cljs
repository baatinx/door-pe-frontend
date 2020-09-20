(ns doorpe.frontend.app
  (:require-macros [secretary.core :refer [defroute]])
  (:require [reagent.dom :as reagent-dom]
            [secretary.core :as secretary]
            [accountant.core :as accountant]
            [reagent.core :as reagent]

            [doorpe.frontend.nav.nav :refer [nav]]
            [doorpe.frontend.footer.footer :refer [footer]]

            [doorpe.frontend.handler :refer [home
                                             register-as-customer
                                             login
                                             complaint
                                             feedback]]))

(defonce page (reagent/atom #'home))

(defn current-page
  []
  [:div
   [nav]
   [@page]
   [footer]])

;; routes
(defn app-routes
  []
  (secretary/defroute "/" []
    (reset! page #'home))

  (secretary/defroute "/register-as-customer" []
    (reset! page #'register-as-customer))

  (secretary/defroute "/login" []
    (reset! page #'login))

  (secretary/defroute "/complaint" []
    (reset! page #'complaint))

  (secretary/defroute "/feedback" []
    (reset! page #'feedback)))


(defn ^:dev/after-load start
  []
  (app-routes)
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