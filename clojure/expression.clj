;homework 8
(defn operation [f]
  (fn [& args]
    (fn [var]
      (apply f (map (fn [x] (double (x var))) args)))))

(defn div [a b] (/ (double a) (double b)))

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
(definterface Expression (evaluate [var]) (^String toString []) (diff [name]))
(defn evaluate [expression var] (.evaluate (expression) var))
(defn toString [expression] (.toString (expression)))
(defn diff [expression name] (.diff (expression) name))

(deftype Const [value])

(deftype Const [value]
  Expression
  (evaluate [_ _] value)
  (toString [_] (format "%.1f" value))
  (diff [_ _] (constantly (Const. 0))))

(def ONE (Const. 1))
(def ZERO (Const. 0))

(deftype Var [value]
  Expression
  (evaluate [_ var] (get var value))
  (toString [_] value)
  (diff [_ name] (if (= name value) (constantly ONE) (constantly ZERO))))

(deftype Operation [op type diff-f arguments]
  Expression
  (evaluate [_ var] (apply op (map (fn [x] (evaluate x var)) arguments)))
  (toString [_] (str "(" type " " (clojure.string/join " " (map (fn [x] (toString x)) arguments)) ")"))
  (diff [_ name] (apply diff-f name arguments)))

(defn build-operation [op type diff-f] (fn [& arguments] (constantly (Operation. op type diff-f arguments))))

(defn get-diff [name]
  (fn [x] (diff x name)))
(declare diff-multiply)
(declare diff-divide)
(declare Sinh)

(declare Cosh)
(defn Variable [value] (constantly (Var. value)))
(defn Constant [value] (constantly (Const. value)))
(def TWO (Constant 2))

(def Add (build-operation + "+" (fn [name & args] (apply Add (map (get-diff name) args)))))
(def Subtract (build-operation - "-" (fn [name & args] (apply Subtract (map (get-diff name) args)))))
(def Multiply (build-operation * "*" (fn [name & args] (apply diff-multiply name args))))
(def Divide (build-operation (fn [& args] (reduce div args)) "/" (fn [name & args] (apply diff-divide name args))))
(def Divide (build-operation (fn [& args] (reduce div args)) "/" (fn [name & args] (apply diff-divide name args))))
(def Negate (build-operation (fn [x] (- x)) "negate" (fn [name arg] (Negate ((get-diff name) arg)))))
(def Sinh (build-operation (fn [x] (Math/sinh x)) "sinh" (fn [name arg] (Multiply (Cosh arg) (diff arg name)))))
(def Cosh (build-operation (fn [x] (Math/cosh x)) "cosh" (fn [name arg] (Multiply (Sinh arg) (diff arg name)))))
(def Square (build-operation (fn [x] (* x x)) "square" (fn [name arg] (Multiply ((get-diff name) arg) TWO arg))))
(def Sqrt (build-operation (fn [x] (Math/sqrt (Math/abs x))) "sqrt"
                           (fn [name arg] (Divide (Multiply arg ((get-diff name) arg))
                                                  (Multiply TWO (Sqrt (Multiply (Square arg) arg)))))))
(defn diff-multiply
  ([name a] ((get-diff name) a))
  ([name a b] (Add (Multiply ((get-diff name) a) b) (Multiply ((get-diff name) b) a)))
  ([name a b & xs] (diff-multiply name a (diff-multiply name b xs))))

(defn diff-divide
  ([name a] ((get-diff name) a))
  ([name a b] (Divide (Subtract (Multiply ((get-diff name) a) b) (Multiply ((get-diff name) b) a)) (Multiply b b)))
  ([name a b & xs] (diff-multiply name a (diff-multiply name b xs))))

(defn parseObj [expr]
  (let [operations {'+ Add, '- Subtract, '/ Divide, '* Multiply, 'negate Negate, 'sinh Sinh, 'cosh Cosh, 'sqrt Sqrt, 'square Square}]
    (cond
      (seq? expr) (let [e (first expr)] (apply (get operations e) (map parseObj (rest expr))))
      (number? expr) (Constant expr)
      (symbol? expr) (Variable (str expr)))))

(defn parseObject [expr] (parseObj (read-string expr)))