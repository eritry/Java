(defn is-vector? [& args] (every? vector? args))

(defn equal-len?
  ([] true)
  ([v] true)
  ([v u] (and (is-vector? v u) (== (count u) (count v))))
  ([v u & vs] (and (equal-len? v u) (apply equal-len? u vs))))

(defn apply-v2v [operation] (fn [v & vs]
                              {:pre [(apply equal-len? v vs)]}
                              (apply mapv operation v vs)))

(defn apply-v2s [operation] (fn
                       ([args] args)
                       ([args & vals] (mapv (fn [f] (apply operation f vals)) args))))

(defn form [b] (loop [v (vector)
                      cur b]
                 (if
                   (instance? Number cur)
                   v
                   (recur (conj v (count cur)) (cur 0))
                   )))


(defn depth [b] (if
                  (instance? Number b)
                  0
                  (+ (depth (b 0)) 1)))


(defn unary-b2b [operation b d] (if
                                  (== 0 d)
                                  (operation b)
                                  (mapv (fn [b] (unary-b2b operation b (dec d))) b)))

(defn broadcast [operation b t db dt]
  (cond
    (== 0 db) (operation b t)
    (== db dt)
    (vec (for [i (range (count b))] (broadcast operation (b i) (t i) (dec db) (dec dt))))
    :else (vec (for [i (range (count b))] (broadcast operation (b i) t (dec db) dt)))))


(defn check-b2b [operation]
  (fn
    ([b] b)
    ([b & bs]
     (apply (check-b2b operation) (
                                    (fn [b t]
                                      (if (< (depth b) (depth t))
                                        (unary-b2b operation (broadcast operation t b (depth t) (depth b)) (depth t))
                                        (broadcast operation b t (depth b) (depth t)))
                                      ) b (nth bs 0)
                                    ) (rest bs))
      ))
  )

(defn apply-b2b [operation]
  (fn
    ([b] (unary-b2b operation b (depth b)))
    ([b & bs] (apply (check-b2b operation) b bs))))


(def v+ (apply-v2v +))
(def v- (apply-v2v -))
(def v* (apply-v2v *))
(def m+ (apply-v2v v+))
(def m- (apply-v2v v-))
(def m* (apply-v2v v*))

(def v*s (apply-v2s *))
(def m*s (apply-v2s v*s))

(defn scalar [& vs] (apply + (apply v* vs)))
(defn m*v [m v] (mapv (fn [row] (scalar row v)) m))
(defn transpose [m] (apply mapv vector m))

(def b+ (apply-b2b +))
(def b- (apply-b2b -))
(def b* (apply-b2b *))


(def m*m (fn
           ([m] m)
           ([m n] (mapv (fn [row] (mapv (fn [col] (scalar row col)) (transpose n))) m))
           ([m n & ms] (apply m*m (m*m m n) ms))))

(defn vect
  ([v] {:pre [(vector? v)]} v)
  ([v u]
   {:pre [(equal-len? v u) (== (count v) 3) (== (count u) 3)]}
   (vector (- (* (v 1) (u 2)) (* (v 2) (u 1)))
           (- (* (v 2) (u 0)) (* (v 0) (u 2)))
           (- (* (v 0) (u 1)) (* (v 1) (u 0)))))
  ([v u & vs]
   {:pre [(apply equal-len? v u vs)]}
   (apply vect (vect v u) vs)))