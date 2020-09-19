(ns doorpe.frontend.nav.nav-links
    (:require ["@material-ui/core" :refer [Link]]))

(defn nav-link
  [{:keys [text href]}]
  [:> Link {:href href
            :style {:display :inline-block
                    :color :white
                    :padding :15px
                    :margin :10px}}
   text])