(ns doorpe.frontend.footer.footer
  (:require [doorpe.frontend.auth.auth :refer [auth]]
            [doorpe.frontend.footer.views.authenticated :refer [authenticated]]
            [doorpe.frontend.footer.views.public :refer [public]]))

(defn footer
  []
  (let [{authenticated? :authenticated?} (auth)]
    (if authenticated?
      [authenticated]
      [public])))
