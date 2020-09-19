(ns doorpe.frontend.nav.views.public
  (:require ["@material-ui/core" :refer [CssBaseline Box Typography Paper Tabs Container Button AppBar Link Menu MenuItem]]
            [doorpe.frontend.nav.nav-links :refer [nav-link]]))

(defn public
  []
  [:<>
   [:> CssBaseline
    [:> Box {:style {:background-color :royalblue}}
     [:> Container
      [nav-link {:text "Home"
                 :href "/"}]
      [nav-link {:text "Register"
                 :href "./register-as-customer"}]
      [nav-link {:text "Login"
                 :href "./login"}]
      [nav-link {:text "Complaint"
                 :href "./complaint"}]
      [nav-link {:text "Feedback"
                 :href "./feedback"}]]]]])