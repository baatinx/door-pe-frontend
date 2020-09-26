(ns doorpe.frontend.footer.footer
  (:require [doorpe.frontend.auth.auth :as auth]
            [doorpe.frontend.footer.views.authenticated :refer [authenticated]]
            [doorpe.frontend.footer.views.public :refer [public]]))

(defn footer
  []
  (let [{authenticated? :authenticated?} @auth/auth-state]
    (if authenticated?
      [authenticated]
      [public])))
