(ns templates.sidebar)

(defn render-sidebar [state-atom]
  (let [adding-new-event (:new @state-atom)]
    [:div#graphing
      [:div#newEvent
        [:button#addEvent.largeInput
          {:on-click #((let [change-state
                              (swap!
                                state-atom
                                assoc
                                :new
                                (not adding-new-event))]
                        (change-state)))}
          (if adding-new-event
            "Cancel"
            "New Event")]
        (if adding-new-event
          [:div
            [:button.largeInput
              "Ok, post!"]]
          [:div])]]))
