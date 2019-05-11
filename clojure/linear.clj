(defn is-vector? [& args] (every? vector? args))

(defn equal-len?
  ([] true)
  ([v] true)
  ([v u] (== (count u) (count v)))
  ([v u & vs] (and (equal-len? v u) (apply equal-len? u vs))))

(defn matrix? [m] (and (is-vector? m) (apply equal-len? m)))
(defn is-matrix? [& args] (every? matrix? args))

(defn apply-v2v [operation]
  (fn [v & vs]
    {:pre [(apply equal-len? v vs)]}
    (apply mapv operation v vs)))

(defn apply-m2m [operation]
  (fn [m & ms]
    {:pre [(apply equal-len? m ms) (is-matrix? (apply vector m ms))]}
    (apply mapv operation m ms)))

(defn apply-v2s [operation]
  (fn
    ([args] args)
    ([args & vals] (mapv (fn [f] (apply operation f vals)) args))))

(defn form [b] (loop [v (vector)
                      cur b]
                 (if
                   (or (instance? Number cur) (= (count cur) 0))
                   v
                   (recur (conj v (count cur)) (nth cur 0))
                   )))


(defn broadcast [b t]
  {:pre (= (nthrest t (- (count t) (count (form b)))) (form t))}
  (if (equal-len? (form b) t)
    b
    (vec (repeat (nth t 0) (broadcast b (rest t)))))
  )

(defn gen-operator [o]
  (fn
    ([x]
     (if (number? x)
       (o x)
       (mapv (gen-operator o) x)))
    ([v u]
     (if (number? v)
       (o v u)
       (mapv (gen-operator o) v u)))))

(defn apply-b2b [operation]
  (fn
    ([b] ((gen-operator operation) b))
    ([b t]
     (let [m (max-key count (form b) (form t))]
       ((gen-operator operation) (broadcast b m) (broadcast t m))))
    ([b t & ts]
     (reduce (apply-b2b operation) ((apply-b2b operation) b t) ts))))

(def v+ (apply-v2v +))
(def v- (apply-v2v -))
(def v* (apply-v2v *))
(def m+ (apply-m2m v+))
(def m- (apply-m2m v-))
(def m* (apply-m2m v*))

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
   {:pre [(is-vector? v u) (== (count v) 3) (== (count u) 3)]}
   (vector (- (* (v 1) (u 2)) (* (v 2) (u 1)))
           (- (* (v 2) (u 0)) (* (v 0) (u 2)))
           (- (* (v 0) (u 1)) (* (v 1) (u 0)))))
  ([v u & vs]
   {:pre [(apply equal-len? v u vs)]}
   (apply vect (vect v u) vs)))
