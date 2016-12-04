(ns templates.sidebar)

(defn render-sidebar [state-atom]
  (let [adding-new-event (:new @state-atom)]
    [:div {:id "graphing"}
      [:div {:id "newEvent"}
        [:button {:id "addEvent"
                  :class "largeInput"
                  :on-click #(swap!
                                state-atom
                                assoc
                                :new
                                (not adding-new-event))}
                (if adding-new-event
                  "Cancel"
                  "New Event")]
        (if adding-new-event
          [:div
            [:button {:class "largeInput"}
              "Ok, post!"]]
          [:div])]]))
