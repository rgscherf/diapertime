(ns templates.center-table
  (:require [templates.header-row :refer [render-header-row]]
            [templates.input-row :refer [render-input-row]]))


(defn render-center-table [new-state new-event]
    (fn [new-state]
      [:div {:id "innerContainer"}
        [:table {:id "mainTable" :class "table table-hover"}
          [render-header-row]
          [:tbody
            ;; this is where the action happens!
            (if new-state
              [render-input-row new-event])
            [:tr
              [:td "old-entry"]
              [:td "hello"]
              [:td "hello"]
              [:td "hello"]
              [:td "hello"]]
            [:tr
              [:td "old-entry"]
              [:td "hello"]
              [:td "hello"]
              [:td "hello"]
              [:td "hello"]]
            [:tr
              [:td "old-entry"]
              [:td "hello"]
              [:td "hello"]
              [:td "hello"]
              [:td "hello"]]]]]))
