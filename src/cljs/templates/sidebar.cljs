(ns templates.sidebar)

(defn render-sidebar
  [state-atom new-event-atom event-template]
  (let [adding-new-event (:new @state-atom)]
    [:div#graphing
      ; [:div#newEvent
        [:button.largeInput
          [:div
            [:a.landingLink {:href "/"
                             :style {:color "black"
                                     :cursor "default"}}
              "Back"]]]
        [:button#addEvent.largeInput
          {:on-click
            #(do (swap! state-atom assoc :new (not adding-new-event))
                 (reset! new-event-atom event-template))}
          (if adding-new-event
            "Cancel"
            "New Event")]
        (if adding-new-event
          [:button.largeInput
            "Ok, post!"])]))
