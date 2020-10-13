(ns doorpe.frontend.add-a-service.views.confirm-service
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [accountant.core :as accountant]
            [reagent.core :as reagent]
            [doorpe.frontend.components.util :refer [two-br]]
            [doorpe.frontend.util :refer [backend-domain]]
            [doorpe.frontend.db :as db]
            [doorpe.frontend.auth.auth :as auth]
            ["@material-ui/core" :refer [Grid Container Paper Typography Label Card CardContent TextField Button MenuItem
                                         Select FormControl  Grid Card CardContent CardAction]]))

(defn dispatch-add-a-service
  [{:keys [service-charges charges experience service-intro degree-title]}]
  (let [url (str backend-domain "/add-a-service")
        add-a-service (:add-a-service @db/app-db)
        by-default-critical-service? (:by-default-critical-service? add-a-service)
        degree-holder-consent? (:degree-holder-consent? add-a-service)
        charge-type (:charge-type add-a-service)
        service-id (:service-id add-a-service)
        doc {:service-id service-id
             :service-charges service-charges
             :experience experience
             :service-intro service-intro}
        degree  (if (or by-default-critical-service? degree-holder-consent?)
                  (merge doc {:professional-degree-holder true
                              :degree-title degree-title
                              :certificate "certificate.png"})
                  (merge doc {:professional-degree-holder false}))
        form-params (if (= "fixed" charge-type)
                      (merge degree {:charges charges})
                      degree)
        res (<! (http/post url {:with-credentials? false
                                :headers {"Authorization" (auth/set-authorization)}
                                :form-params form-params}))]
    (js/alert res)
    (accountant/navigate! "/dashboard")))

(defn confirm-service
  []
  (let [add-a-service (:add-a-service @db/app-db)
        by-default-critical-service? (:by-default-critical-service? add-a-service)
        degree-holder-consent? (:degree-holder-consent? add-a-service)
        charge-type (:charge-type add-a-service)
        initial-vaules {:service-charges "" :charges "" :experience "" :service-intro "" :degree-tittle ""}
        values (reagent/atom initial-vaules)]
    [:> Container {:maxWidth "sm"}
     [:> Paper {:variant :outlined
                :square true}
      [:> Typography {:variant :h6}
       "Add Service"]

      [:br]

      [:> TextField {:variant :outlined
                     :label "Service Carges"
                     :type :number
                     :on-change #(swap! values assoc :service-charges (.. % -target -value))}]
      [two-br]

      (if (= "fixed" charge-type)
        [:> TextField {:variant :outlined
                       :label "Charges"
                       :type :number
                       :on-change #(swap! values assoc :charges (.. % -target -value))}]

        [:> TextField {:variant :outlined
                       :label "*Charges will be disscussed on Inspection"
                       :type :number
                       :disabled true
                       :error true
                       :color :secondary}])

      [two-br]

      [:> TextField {:variant :outlined
                     :label "Experience"
                     :type :number
                     :on-change #(swap! values assoc :experience (.. % -target -value))
                     :helper-text "*Experience in years"}]
      [two-br]

      [:> TextField {:variant :outlined
                     :label "Service Introduction"
                     :type :text
                     :on-change #(swap! values assoc :service-intro (.. % -target -value))
                     :placeholder " e.g Hi! my name is Asif, I'm expert ..."
                     :multiline true
                     :rows 10
                     :fullWidth true
                     :helperText "Please be introduce yourself to your audience"}]
      [two-br]

      (if (or by-default-critical-service? degree-holder-consent?)
        [:<>
         [:> TextField {:variant :outlined
                        :label "Degree/course Title"
                        :type :text
                        :on-change #(swap! values assoc :degree-title (.. % -target -value))
                        :helper-text "e.g 2 years Diploma in / 3 month training in  - "}]
         [two-br]

         [:input {:accept :image/.*
                  :id :contained-button-file
                  :multiple false
                  :type :file}]


         [two-br]])

      [:> Button {:variant :contained
                  :color :primary
                  :on-click #(dispatch-add-a-service @values)}
       "Register"]]]))
