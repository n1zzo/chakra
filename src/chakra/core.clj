(ns chakra.core
  (:gen-class)
  (:require [quil.core :as q :include-macros true]
                        [quil.middleware :as m]))

(def m (+ (/ 1 3) 3)) ; Multiplication factor
(def n 300) ; Number of points

(defn create-points [number]
  (map #(hash-map :id %
                  :angle (* (/ % number) m q/TWO-PI)
                  :radius 0
                  :size 20)
       (range 0 number)))

; The value returned from this is the initial state
(defn setup []
    (q/frame-rate 60)
    (q/background 1 1 1)
    (create-points n))

(defn update-point [point]
  (let [{:keys [radius angle size]} point]
    {:angle (+ angle 0.005)
     :radius (+ (* (q/sin (* m angle)) 80) 300)
     :size (+ (* (q/cos (* m angle)) 5) 10)}))

(defn draw-point [point]
  (let [{:keys [radius angle size]} point
        x (* radius (q/cos angle))
        y (* radius (q/sin angle))]
        (q/with-translation [(/ (q/width) 2)
                             (/ (q/height) 2)]
          (q/ellipse x y size size))))

; Pure function, takes an old state, return a new one
(defn update-state [state]
  (reduce conj '() (map update-point state)))

; Draws the passed state
(defn draw-state [state]
  (q/background 1 1 1)
  (loop [i 0]
    (draw-point (nth state i))
    (when (< i (- (count state) 1))
      (recur (inc i)))))

(q/defsketch chakra
  :title "Chakra"
  :settings #(q/smooth 2)
  :setup setup
  :update update-state
  :draw draw-state
  :middleware [m/fun-mode]
  :size [1000 1000])

(defn -main
  "A cool animation."
  [& args]
  (+ 1 1))
