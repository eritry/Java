;homework 8
(defn operation [f]
  (fn [& args]
    (fn [var]
      (apply f (map (fn [x] (double (x var))) args)))))

(defn div [a & bs] (reduce (fn [a b] (/ (double a) (double b))) a bs))
(defn neg [x] (- x))
(defn sqr [x] (* x x))
(defn sqrt [x] (Math/sqrt (Math/abs x)))

(def add (operation +))
(def subtract (operation -))
(def multiply (operation *))
(def divide (operation div))
(def negate (operation -))

(def min (operation clojure.core/min))
(def max (operation clojure.core/max))

(def constant constantly)
(defn variable [x] (fn [var] (get var x)))

(defn parse [expr]
  (let [operations {'+ add, '- subtract, '/ divide, '* multiply, 'negate negate, 'min min, 'max max}]
    (cond
      (seq? expr) (let [e (first expr)]
                    (apply (get operations e) (map parse (rest expr))))
      (number? expr) (constant expr)
      (symbol? expr) (variable (str expr)))))

(def parseFunction
  (comp parse read-string))

;homework 9
(declare Add)
(declare Subtract)
(declare Multiply)
(declare Divide)

(definterface Expression (evaluate [var]) (^String toString []) (diff [name]))
(defn evaluate [expression var] (.evaluate (expression) var))
(defn toString [expression] (.toString (expression)))
(defn diff [expression name] (.diff (expression) name))

(deftype Const [value]
  Expression
  (evaluate [_ _] value)
  (toString [_] (format "%.1f" value))
  (diff [_ _] (constantly (Const. 0))))

(defn Constant [value] (constantly (Const. value)))
(def TWO (Constant 2))
(def ONE (Constant 1))
(def ZERO (Constant 0))

(deftype Var [value]
  Expression
  (evaluate [_ var] (get var value))
  (toString [_] value)
  (diff [_ name] (if (= name value) ONE ZERO)))
(defn Variable [value] (constantly (Var. value)))

(def Operations {})

(deftype Operation [type arguments]
  Expression
  (evaluate [_ var] (apply (nth (get Operations type) 0) (map (fn [x] (evaluate x var)) arguments)))
  (toString [_] (str "(" type " " (clojure.string/join " " (map (fn [x] (toString x)) arguments)) ")"))
  (diff [_ name] (apply (nth (get Operations type) 1) name arguments)))

(defn get-diff [name x] (diff x name))
(defn get-diffs [name & args] (mapv (fn [x] (get-diff name x)) args))

(defn diff-multiply
  ([name xs] (reduce (fn [a b] (Add (Multiply (get-diff name a) b) (Multiply (get-diff name b) a))) xs)))

(defn diff-divide
  ([name xs] (reduce (fn [a b] (Divide (Subtract (Multiply (get-diff name a) b) (Multiply (get-diff name b) a)) (Multiply b b))) xs)))

(defn differ* [op] (fn [name & args] (op name args)))
(defn build-operation [op type diff-f]
  (fn [& arguments] (def Operations (assoc Operations type [op diff-f])) (constantly (Operation. type arguments))))

(def Add      (build-operation +   "+"       (fn [name & args] (apply Add      (apply get-diffs name args)))))
(def Subtract (build-operation -    "-"      (fn [name & args] (apply Subtract (apply get-diffs name args)))))
(def Multiply (build-operation *    "*"      (differ* diff-multiply)))
(def Divide   (build-operation div  "/"      (differ* diff-divide)))
(def Negate   (build-operation neg  "negate" (fn [name arg]    (Negate   (get-diff name arg)))))
(def Square   (build-operation sqr  "square" (fn [name arg]    (Multiply (get-diff name arg) TWO arg))))
(def Sqrt     (build-operation sqrt "sqrt"   (fn [name arg]    (Divide   (Multiply arg (get-diff name arg))
                                                                         (Multiply TWO (Sqrt (Multiply (Square arg) arg)))))))

(defn parseObj [expr]
  (let [operations {'+ Add, '- Subtract, '/ Divide, '* Multiply, 'negate Negate, 'sqrt Sqrt, 'square Square}]
    (cond
      (seq? expr) (let [e (first expr)] (apply (get operations e) (map parseObj (rest expr))))
      (number? expr) (Constant expr)
      (symbol? expr) (Variable (str expr)))))

(defn parseObject [expr] (parseObj (read-string expr)))