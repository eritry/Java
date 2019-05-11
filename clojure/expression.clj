;homework 8
(defn operation [f]
  (fn [& args]
    (fn [var]
      (apply f (map (fn [x] (double (x var))) args)))))

(defn min-max-operation [f]
  (fn [& args]
    (fn [var]
      (reduce f (map (fn [x] (double (x var))) args)))))

(defn div [a b] (/ (double a) (double b)))

(def add (operation +))
(def subtract (operation -))
(def multiply (operation *))
(def divide (operation div))
(def negate (operation -))

(def min (min-max-operation clojure.core/min))
(def max (min-max-operation clojure.core/max))

(defn constant [x] (constantly x))
(defn variable [x] (fn [var] (get var x)))

(defn parse [expr]
  (let [operations {'+ add, '- subtract, '/ divide, '* multiply, 'negate negate, 'min min, 'max max}]
    (cond
      (seq? expr) (let [e (first expr)] (apply (get operations e) (map parse (rest expr))))
      (number? expr) (constant expr)
      (symbol? expr) (variable (str expr)))))

(def parseFunction
  (comp parse read-string))

;homework 9
(definterface Expression (evaluate [var]) (^String toString []) (diff [name]))
(defn evaluate [expression, var] (.evaluate (expression) var))
(defn toString [expression] (.toString (expression)))
(defn diff [expression, name] (.diff (expression) name))

(deftype Const [value]
  Expression
  (evaluate [this var] value)
  (toString [this] (format "%.1f" value))
  (diff [this name] (fn [] (Const. 0))))

(deftype Var [value]
  Expression
  (evaluate [this var] (get var value))
  (toString [this] value)
  (diff [this name] (if (= name value) (fn [] (Const. 1)) (fn [] (Const. 0)))))

(deftype UnaryOperation [op type diff-f arguments]
  Expression
  (evaluate [this var] (op (evaluate arguments var)))
  (toString [this] (str "(" type " " (toString arguments) ")"))
  (diff [this name] (diff-f name arguments)))

(deftype Operation [op type diff-f arguments]
  Expression
  (evaluate [this var] (apply op (map (fn [x] (evaluate x var)) arguments)))
  (toString [this] (str "(" type " " (clojure.string/join " " (map (fn [x] (toString x)) arguments)) ")"))
  (diff [this name] (apply diff-f name arguments)))

(defn get-diff [name]
  (fn [x] (diff x name)))

(def diff-multiply)
(def diff-divide)
(def Sinh)
(def Cosh)

(defn Constant [value] (fn [] (Const. value)))
(defn Variable [value] (fn [] (Var. value)))
(defn Add [& arguments] (fn [] (Operation. + "+" (fn [name & args] (apply Add (map (get-diff name) args))) arguments)))
(defn Subtract [& arguments] (fn [] (Operation. - "-" (fn [name & args] (apply Subtract (map (get-diff name) args))) arguments)))
(defn Multiply [& arguments] (fn [] (Operation. * "*" (fn [name & args] (apply diff-multiply name args)) arguments)))
(defn Divide [& arguments] (fn [] (Operation. (fn [& args] (reduce div args)) "/" (fn [name & args] (apply diff-divide name args)) arguments)))
(defn Negate [argument] (fn [] (UnaryOperation. (fn [x] (- x)) "negate" (fn [name arg] (Negate (diff arg name))) argument)))
(defn Sinh [argument] (fn [] (UnaryOperation. (fn [x] (Math/sinh x)) "sinh" (fn [name arg] (Multiply (Cosh arg) (diff arg name))) argument)))
(defn Cosh [argument] (fn [] (UnaryOperation. (fn [x] (Math/cosh x)) "cosh" (fn [name arg] (Multiply (Sinh arg) (diff arg name))) argument)))

(defn diff-multiply
  ([name a] ((get-diff name) a))
  ([name a b] (Add (Multiply ((get-diff name) a) b) (Multiply ((get-diff name) b) a)))
  ([name a b & xs] (diff-multiply name a (diff-multiply name b xs))))

(defn diff-divide
  ([name a] ((get-diff name) a))
  ([name a b] (Divide (Subtract (Multiply ((get-diff name) a) b) (Multiply ((get-diff name) b) a)) (Multiply b b)))
  ([name a b & xs] (diff-multiply name a (diff-multiply name b xs))))

(defn parseObj [expr]
  (let [operations {'+ Add, '- Subtract, '/ Divide, '* Multiply, 'negate Negate, 'sinh Sinh, 'cosh Cosh}]
    (cond
      (seq? expr) (let [e (first expr)] (apply (get operations e) (map parseObj (rest expr))))
      (number? expr) (Constant expr)
      (symbol? expr) (Variable (str expr)))))

(defn parseObject [expr] (parseObj (read-string expr)))