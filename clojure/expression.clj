(defn operation [f]
  (fn [& args]
    (fn [var]
      (apply f (map (fn [x] (double (x var))) args)))))

(def add (operation +))
(def subtract (operation -))
(def multiply (operation *))
(def divide (operation (fn [a b] (/ (double a) (double b)))))
(def negate (operation -))
(def min (operation min))
(def max (operation max))

(defn constant [x] (fn [var] x))
(defn variable [x] (fn [var] (get var x)))

(defn parseFunction [expr]
  (let [operations { '+ add, '- subtract, '/ divide, '* multiply, 'negate negate, 'min min, 'max max}]
    (cond
      (seq? expr) (let [e (first expr)] (apply (get operations e) (map parseFunction (rest expr))))
      (string? expr) (parseFunction (read-string expr))
      (number? expr) (constant expr)
      (symbol? expr) (variable (str expr))
      )))

