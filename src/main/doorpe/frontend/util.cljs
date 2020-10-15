(ns doorpe.frontend.util)

(def backend-domain "http://localhost:7000")

(defn validity-state?
  [element-id]
  (let [element (js/document.getElementById element-id)
        is-valid? element.validity.valid]
    is-valid?))

(defn check-validity
  [values elements-vector fn-on-success]
  (let [states (mapv validity-state?
                     elements-vector)
        ok? (not (some false? states))]
    (if ok?
      (fn-on-success values)
      (js/alert "* all feilds are required, please provide valid input"))))