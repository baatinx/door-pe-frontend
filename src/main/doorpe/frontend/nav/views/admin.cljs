(ns doorpe.frontend.nav.views.admin
  (:require ["@material-ui/core" :refer [CssBaseline Box Container]]
            [doorpe.frontend.nav.nav-links :refer [nav-link]]
            [accountant.core :as accountant]
            [doorpe.frontend.db :as db]))

(defn admin
  []
  [:<>
   [:> CssBaseline
    [:> Box {:style {:background-color :royalblue}}
     [:> Container
      [nav-link {:text "Dashboard"
                 :on-click #(accountant/navigate! "/dashboard")}]
      [nav-link {:text "revenue"
                 :on-click #(accountant/navigate! "/revenue")}]
      [nav-link {:text "Add Category"
                 :on-click #(do
                              (swap! db/app-db assoc :admin-add "category")
                              (accountant/navigate! "/admin-add"))}]
      [nav-link {:text "Add Service"
                 :on-click #(do
                              (swap! db/app-db assoc :admin-add "service")
                              (accountant/navigate! "/admin-add"))}]
      [nav-link {:text "Service Requests"
                 :on-click #(accountant/navigate! "/service-requests")}]
      [nav-link {:text "Complaints"
                 :on-click #(accountant/navigate! "/check-complaints")}]
      [nav-link {:text "Logout"
                 :on-click #(accountant/navigate! "/logout")}]]]]])