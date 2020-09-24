(ns doorpe.frontend.auth.auth
  (:require [reagent.core :as r]))

(def auth-state (r/atom {:authenticated? false
                         :token nil
                         :user-id nil
                         :user-type nil
                         :dispatch-view :public}))
(defn auth
  []
  @auth-state)