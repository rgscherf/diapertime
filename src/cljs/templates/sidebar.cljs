(ns templates.sidebar)

(def summary {:time-slept ["Slept last 24h" 540]
              :avg-slept ["Avg sleep last 24h" 90]
              :time-awake ["Awake last 24h" 300]
              :avg-awake ["Avg awake last 24h" 30]
              :times-peed ["Peed last 24h" 8]
              :times-pooped ["Pooped last 24h" 2]})

(defn- render-summary-col
  [[desc-a val-a] [desc-b val-b]]
  (let [desc-style
        {:style {:flex "1"
                 :opacity "0.38"
                 :display "flex"
                 :justify-content "flex-end"
                 :align-items "center"
                 :text-align "right"
                 :font-size "0.9em"}}
        val-style
        {:style {:flex "1"
                 :display "flex"
                 :justify-content "flex-start"
                 :align-items "center"
                 :font-size "2em"
                 :padding "0 10px"}}]
    [:div
      {:style {:flex "1"
               :display "flex"
               :flex-direction "column"}}
      [:div
        {:style {:display "flex"
                 :flex "1"}}
        [:div
          desc-style
          desc-a]
        [:div
          val-style
          [:div val-a]]]
      [:div
        {:style {:display "flex"
                 :flex "1"}}
        [:div
          desc-style
          desc-b]
        [:div
          val-style
          [:div val-b]]]]))

(defn- render-summary
  "summary info baby's last 24 hours."
  []
  [:div
    {:style {:width "75%"
             :height "120px"
             :display "flex"}}
    [render-summary-col (:time-slept summary) (:avg-slept summary)]
    [render-summary-col (:time-awake summary) (:avg-awake summary)]
    [render-summary-col (:times-peed summary) (:times-pooped summary)]])

(defn render-sidebar
  [state-atom new-event-atom event-template]
  (let [adding-new-event (:new @state-atom)]
    [:div
      {:style {:display "flex"
               :justify-content "space-between"
               :align-items "center"
               :width "100%"
               :margin-bottom "20px"}}
      ;; events summary
      [render-summary]
      ;; interation buttons
      [:div
        {:style {:display "flex"
                 :flex-direction "column"
                 :justify-content "space-around"
                 :height "100%"
                 :min-height "120px"
                 :align-items "center"
                 :margin-left "20px"}}
        (if adding-new-event
          [:button.larginInput
            {:style {:width "150px"
                     :height "50px"}
             :on-click
              #(do (swap! state-atom assoc :new (not adding-new-event))
                   (reset! new-event-atom event-template))}
            "Cancel"])
        (if adding-new-event
          [:button.larginInput
            {:style {:width "150px"
                     :height "50px"}}
            "Post!"])
        (if (not adding-new-event)
          [:button.largeInput
            {:on-click
              #(do (swap! state-atom assoc :new (not adding-new-event))
                   (reset! new-event-atom event-template))
             :style {:width "150px"
                     :height "120px"}}
            "New event"])]]))
          ;
          ; (if adding-new-event
          ;   "Cancel"
          ;   "New Event"))
        ; (if adding-new-event
        ;   [:button.largeInput
        ;     "Ok, post!"])]]))
