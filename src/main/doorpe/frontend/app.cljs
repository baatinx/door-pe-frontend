(ns doorpe.frontend.app
  (:require [reagent.dom :as reagent]))

(defn app 
  []
  [:h3 {:style {:color :tomato
                :font "normal 40px sans-serif"}}
   "Hello DOM"])

(defn ^:dev/after-load start
  []
  (reagent/render [app]
            (.getElementById js/document "root")))

(defn ^:export init
  []
  (start))