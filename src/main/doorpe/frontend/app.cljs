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
                                             book-complaint
                                             about-us
                                             contact-us
                                             feedback

                                             ;; customer handlers
                                             customer-dashboard
                                             customer-my-bookings
                                             customer-my-profile
                                             customer-logout]]))

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

  ;; public routes
  (secretary/defroute "/" []
    (reset! page #'home))

  (secretary/defroute "/register/as-customer" []
    (reset! page #'register-as-customer))

  (secretary/defroute "/login" []
    (reset! page #'login))

  (secretary/defroute "/book-complaint" []
    (reset! page #'book-complaint))

  (secretary/defroute "/about-us" []
    (reset! page #'about-us))

  (secretary/defroute "/contact-us" []
    (reset! page #'contact-us))

  (secretary/defroute "/feedback" []
    (reset! page #'feedback))



  ;; customer routes
  (secretary/defroute "/customer/dashboard" []
    (reset! page #'customer-dashboard))

  (secretary/defroute "/customer/my-bookings" []
    (reset! page #'customer-my-bookings))

  (secretary/defroute "/customer/my-profile" []
    (reset! page #'customer-my-profile))

  (secretary/defroute "/customer/logout" []
    (reset! page #'customer-logout)))


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