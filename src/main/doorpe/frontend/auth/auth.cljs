(ns doorpe.frontend.auth.auth
  (:require [reagent.core :as r]))

(def auth-state (r/atom {:authenticated? false
                         :token nil
                         :user-id nil
                         :user-type nil
                         :dispatch-view :public}))

(defn get-token
  []
  (:token @auth-state))

(defn set-authorization
  []
  (str "Token " (get-token)))

