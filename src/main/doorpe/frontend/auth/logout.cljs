(ns doorpe.frontend.auth.logout
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [accountant.core :as accountant]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [doorpe.frontend.util :refer [backend-domain]]
            [doorpe.frontend.components.util :refer [two-br three-br]]
            [doorpe.frontend.auth.auth :as auth]
            [doorpe.frontend.db :as db]
            ["@material-ui/core" :refer [Container Typography TextField Button MenuItem
                                         Select FormControl  Grid Card CardContent CardAction]]))

(defn do-logout
  []
  (go (let [url (str backend-domain "/logout")
            res (<! (http/post url  {:with-credentials? false
                                     :headers {"Authorization" (auth/set-authorization)}}))
            ok (get-in res [:body :logout])]
        (reset! auth/auth-state {:authenticated? false
                                 :user-id nil
                                 :user-type nil
                                 :dispatch-view :public})
        (reset! db/app-db {:my-profile nil
                           :my-bookings nil})

        (accountant/navigate! "/"))))

(defn logout
  []
  (#(do-logout))

  [:> Typography {:variant :h1}
   " :-( Bye! Logging out ...."])