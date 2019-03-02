(ns dojo-boids.core
  (:require [reagent.core :as reagent :refer [atom]]
            [quil.middleware :as m]
            [quil.core :as q]))

(enable-console-print!)

(println "This text is printed from src/dojo-boids/core.cljs. Go ahead and edit it and see reloading in action.")


(defn find-neighbours [boid all-boids])

(defn align-boid [boid neightbours])

(defn init-boid [radius width height]
  {:pos         {:x 1
                 :y 1}
   :speed       0
   :orientation (rand 360)})

(defn draw-boid [_]
  (q/stroke (q/random 255))             ;; Set the stroke colour to a random grey
  (q/stroke-weight (q/random 10))       ;; Set the stroke thickness randomly
  (q/fill (q/random 255))               ;; Set the fill colour to a random grey

  (let [diam (q/random 100)       ;; Set the diameter to a value between 0 and 100
        x    (q/random (q/width)) ;; Set the x coord randomly within the sketch
        y    (q/random (q/height))]     ;; Set the y coord randomly within the sketch
    (q/ellipse x y diam diam)))

(defn draw [{:keys [boids history]}]
  (q/background 255)
  (doseq [boids history]
    (doseq [boid boids]
      (draw-boid boid)))
  (doseq [boid boids]
    (draw-boid boid)))

(defn update-state []
  {:pos nil})

(defn init [num-boids radius width height]
  {:boids     (repeatedly num-boids (init-boid radius width height))
   :width     width
   :height    height
   :max-speed 3.5})

(defn canvas []
  (reagent/create-class
   {:component-did-mount
    (fn [component]
      (let [node   (reagent/dom-node component)
            width  (.-width node)
            height (.-height node)]
        (q/sketch
         :host node
         :draw draw
         :setup (init 25 5 width height)
         :update update-state
         :size [width height]
         :middleware [m/fun-mode])))
    :render
    (fn []
      [:canvas
       {:width  (/ (.-innerWidth js/window) 2)
        :height (/ (.-innerHeight js/window) 2)}])}))

(defn hello-world []
  [:div
   [:h3 "Edit this and watch it change!"]
   [canvas]])

(reagent/render-component [hello-world]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
