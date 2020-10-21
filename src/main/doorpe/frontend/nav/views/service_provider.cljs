(ns doorpe.frontend.nav.views.service-provider
  (:require ["@material-ui/core" :refer [CssBaseline Box Container]]
            [doorpe.frontend.nav.nav-links :refer [nav-link]]
            [accountant.core :as accountant]
            [doorpe.frontend.db :as db]))

(defn service-provider
  []
  [:<>
   [:> CssBaseline
    [:> Box {:style {:background-color :royalblue}}
     [:> Container
      [nav-link {:text "Dashboard"
                 :on-click #(accountant/navigate! "/dashboard")}]
      [nav-link {:text "My Bookings"
                 :on-click #(accountant/navigate! "/my-bookings")}]
      [nav-link {:text "pending-dues"
                 :on-click #(accountant/navigate! "/pending-dues")}]
      [nav-link {:text "pay-dues"
                 :on-click #(accountant/navigate! "/pay-dues")}]
      [nav-link {:text "provide service"
                 :on-click #(do
                              (swap! db/app-db assoc :provide-service nil)
                              (accountant/navigate! "/provide-service"))}]
      [nav-link {:text "Book Complaint"
                 :on-click #(accountant/navigate! "/book-complaint")}]
      [nav-link {:text "My Profile"
                 :on-click #(accountant/navigate! "/my-profile")}]
      [nav-link {:text "Logout"
                 :on-click #(accountant/navigate! "/logout")}]]]]])