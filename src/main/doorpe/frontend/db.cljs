(ns doorpe.frontend.db)

(def app-db (atom {:my-profile {:name ""
                                :address ""
                                :pin-code ""
                                :latitude ""
                                :longitude ""}
                   :booking {:category "one"}}))