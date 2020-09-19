(ns doorpe.frontend.auth.auth)

(defn auth
  []
  {:authenticated? false
   :user-id nil
   :user-type nil
   :dispatch-view :public})