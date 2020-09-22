(ns doorpe.frontend.nav.views.customer
  (:require ["@material-ui/core" :refer [CssBaseline Box Typography Paper Tabs Container Button AppBar Link Menu MenuItem]]
            [doorpe.frontend.nav.nav-links :refer [nav-link]]
            [accountant.core :as accountant]
            [doorpe.frontend.auth.auth :as auth]))

(defn customer
  []
  [:<>
   [:> CssBaseline
    [:> Box {:style {:background-color :royalblue}}
     [:> Container
      [nav-link {:text "Dashboard"
                 :on-click #(accountant/navigate! "/customer/dashboard")}]
      [nav-link {:text "My Bookings"
                 :on-click #(accountant/navigate! "/customer/my-bookings")}]
      [nav-link {:text "My Profile"
                 :on-click #(accountant/navigate! "/customer/my-profile")}]
      [nav-link {:text "Logout"
                 :on-click #(accountant/navigate! "/customer/logout")}]]]]])