(ns dojo-boids.core
  (:require [reagent.core :as reagent :refer [atom]]
            [quil.middleware :as m]
            [quil.core :as q]))

(enable-console-print!)

(def ^:const max-speed 3.5)

(defn distance [{p1 :pos} {p2 :pos}]
  (let [[x y] (mapv (fn [v1 v2] (Math/abs (- v1 v2))) p1 p2)]
    (Math/sqrt (+ (* x x) (* y y)))))

(defn find-neighbours [{p1 :pos :as boid} boids radius]
  (keep
   (fn [{p2 :pos :as neightbour}]
     (let [distance (distance p1 p2)]
       (when (and (not= boid neightbour) (< distance radius))
         neightbour)))))

(defn align-boid [boid neightbours])

(defn init-boid [radius width height]
  (fn []
    {:pos         {:x (rand-int width)
                   :y (rand-int height)}
     :speed       (rand max-speed)
     :direction (rand (* 2 js/Math.PI))}))

(defn draw-boid [{{:keys [x y]} :pos}]
  (q/stroke 3)
  (q/stroke-weight 3)
  (q/fill (q/random 255))
  (let [diam 3]
    (q/ellipse x y diam diam)))

(defn draw [{:keys [boids history]}]
  (q/background 255)
  (doseq [boids history]
    (doseq [boid boids]
      (draw-boid boid)))
  (doseq [boid boids]
    (draw-boid boid)))

(defn update-state [state]
   state)

(defn init [num-boids radius width height]
  (fn []
    {:boids     (repeatedly num-boids (init-boid radius width height))
     :width     width
     :height    height
     :max-speed 3.5}))

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
   [canvas]])

(reagent/render-component [hello-world]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
