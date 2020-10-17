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
      [nav-link {:text "Add new Category"
                 :on-click #(do
                              (swap! db/app-db assoc :admin-add "category")
                              (accountant/navigate! "/admin-add"))}]
      [nav-link {:text "Add new Service"
                 :on-click #(do
                              (swap! db/app-db assoc :admin-add "service")
                              (accountant/navigate! "/admin-add"))}]
      [nav-link {:text "Edit Category"
                 :on-click #(do
                              (swap! db/app-db assoc :admin-edit "category")
                              (accountant/navigate! "/admin-edit"))}]
      [nav-link {:text "Edit Service"
                 :on-click #(do
                              (swap! db/app-db assoc :admin-edit "service")
                              (accountant/navigate! "/admin-edit"))}]
      [nav-link {:text "Service Requests"
                 :on-click #(accountant/navigate! "/admin-service-requests")}]
      [nav-link {:text "Logout"
                 :on-click #(accountant/navigate! "/logout")}]]]]])