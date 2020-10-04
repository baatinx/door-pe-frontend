(ns doorpe.frontend.nav.views.public
  (:require ["@material-ui/core" :refer [CssBaseline Box Typography Paper Tabs Container Button AppBar Link Menu MenuItem]]
            [accountant.core :as accountant]
            [doorpe.frontend.nav.nav-links :refer [nav-link]]))

(defn public
  []
  [:<>
   [:> CssBaseline
    [:> Box {:style {:background-color :royalblue}}
     [:> Container
      [nav-link {:text "Home"
                 :on-click #(accountant/navigate! "/")}]
      [nav-link {:text "Register"
                 :on-click #(accountant/navigate! "/register")}]
      [nav-link {:text "Book Complaint"
                 :on-click #(accountant/navigate! "/book-complaint")}]
      [nav-link {:text "Feedback"
                 :on-click #(accountant/navigate! "/feedback")}]
      [nav-link {:text "About Us"
                 :on-click #(accountant/navigate! "/about-us")}]
      [nav-link {:text "Contact Us"
                 :on-click #(accountant/navigate! "/contact-us")}]
       [nav-link {:text "Track location"
                  :on-click #(accountant/navigate! "/track-location")}]
      [nav-link {:text "Login"
                 :on-click #(accountant/navigate! "/login")}]]]]])