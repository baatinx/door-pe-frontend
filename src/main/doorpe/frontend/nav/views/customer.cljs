(ns doorpe.frontend.nav.views.customer
  (:require ["@material-ui/core" :refer [CssBaseline Box Container]]
            [doorpe.frontend.nav.nav-links :refer [nav-link]]
            [accountant.core :as accountant]
            [doorpe.frontend.db :as db]))

(defn customer
  []
  [:<>
   [:> CssBaseline
    [:> Box {:style {:background-color :royalblue}}
     [:> Container
      [nav-link {:text "Dashboard"
                 :on-click #(accountant/navigate! "/dashboard")}]
      [nav-link {:text "My Bookings"
                 :on-click #(accountant/navigate! "/my-bookings")}]
      [nav-link {:text "Book a Service"
                 :on-click #(do ((swap! db/app-db assoc :book-a-service nil)
                                 (accountant/navigate! "/book-a-service")))}]
      [nav-link {:text "My Profile"
                 :on-click #(accountant/navigate! "/my-profile")}]
      [nav-link {:text "Logout"
                 :on-click #(accountant/navigate! "/logout")}]]]]])