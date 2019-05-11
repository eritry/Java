(defn operation [f]
  (fn [& args]
    (fn [var]
      (apply f (map (fn [x] (double (x var))) args)))))

(defn min-max-operation [f]
  (fn [& args]
    (fn [var]
      (reduce f (map (fn [x] (double (x var))) args)))))

(def add (operation +))
(def subtract (operation -))
(def multiply (operation *))
(def divide (operation (fn [a b] (/ (double a) (double b)))))
(def negate (operation -))

(def min (min-max-operation clojure.core/min))
(def max (min-max-operation clojure.core/max))

(defn constant [x] (constantly x))
(defn variable [x] (fn [var] (get var x)))

(defn parse [expr]
  (let [operations { '+ add, '- subtract, '/ divide, '* multiply, 'negate negate, 'min min, 'max max}]
    (cond
      (seq? expr) (let [e (first expr)] (apply (get operations e) (map parse (rest expr))))
      (number? expr) (constant expr)
      (symbol? expr) (variable (str expr)))))

(def parseFunction
  (comp parse read-string))


(defn broadcast [b t db dt]
  (cond
    (== 0 db) (vec [b t])
    (== db dt) (vec (mapv (fn [bi ti] (broadcast bi ti (dec db) (dec dt))) b t))
    :else (vec (mapv (fn [bi] (broadcast bi t (dec db) dt)) b))))

(defn check-b2b [operation]
  (fn
    ([b] b)
    ([b & bs]
     (apply (check-b2b operation) (
                                    (fn [b t]
                                      (if (< (depth b) (depth t))
                                        (unary-b2b operation (apply operation (broadcast t b (depth t) (depth b))) (depth t))
                                        (println (apply + (broadcast b t (depth b) (depth t))))
                                        ;(apply operation (broadcast b t (depth b) (depth t)))
                                        )
                                      ) b (nth bs 0)
                                    ) (rest bs))
      ))
  )