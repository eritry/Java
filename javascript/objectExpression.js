let variables = {
    "x" : 0, "y" : 1, "z" : 2
};

function Const(value) { this.value = value; }
Const.prototype.evaluate = function () { return this.value; };
Const.prototype.toString = Const.prototype.prefix = Const.prototype.postfix = function () { return '' + this.value; };
Const.prototype.diff = function () { return new Const(0); };

const ZERO = new Const(0);
const ONE = new Const(1);

function Variable(name) { this.name = name; }
Variable.prototype.evaluate = function (...args) { return args[variables[this.name]]; };
Variable.prototype.toString = Variable.prototype.prefix = Variable.prototype.postfix = function () { return this.name; };
Variable.prototype.diff = function (name) { return this.name === name ? ONE : ZERO; };

let Operation = function(){};
Operation.prototype.evaluate = function (...args) {
    return this.operations(...this.argts.map(arg => arg.evaluate(...args)));
};
Operation.prototype.toString = function () {
    return this.argts.join(" ") + " " + this.type;
};
Operation.prototype.diff = function(name) {
    return this.dff(...this.argts, ...this.argts.map(arg => arg.diff(name)))
};
Operation.prototype.prefix = function () {
    return this.argts.reduce((ret, arg) => ret + " " + arg.prefix(), "(" + this.type) + (this.argts.length ? "" : " ") + ")";
};
Operation.prototype.postfix = function() {
    return this.argts.reduce((ret, arg) => ret + arg.postfix() + " ", "(") + (this.argts.length ? "" : " ") + this.type + ")";
};

let NewOperation = function(operation, name, diff) {
    let ans = function(...args) {
        this.argts = args;
        this.operations = operation;
        this.type = name;
        this.dff = diff;
    };
    ans.prototype = Operation.prototype;
    return ans;
};

const Add = NewOperation((a, b) => (a + b), '+', (a, b, da, db) => new Add(da, db));

const Subtract = NewOperation((a, b) => (a - b), '-', (a, b, da, db) => new Subtract(da, db));

const Multiply = NewOperation((a, b) => (a * b), '*', (a, b, da, db) => new Add(new Multiply(da, b), new Multiply(a, db)));

const Divide = NewOperation((a, b) => (a / b), '/',
    (a, b, da, db) => new Divide(new Subtract(new Multiply(da, b), new Multiply(a, db)), new Multiply(b, b)));

const Negate = NewOperation(a => -a, "negate", (a, da) => new Negate(da));

const ArcTan = NewOperation(Math.atan, "atan",
    (a, da) => new Multiply(new Divide(new Const(1), new Add(new Multiply(a, a), new Const(1))), da));

const ArcTan2 = NewOperation(Math.atan2, "atan2",
    (a, b, da, db) => new Divide(new Subtract(new Multiply(da, b), new Multiply(a, db)), new Add(new Multiply(a, a), new Multiply(b, b))));

//(...arr) => arr.map(Math.exp).reduce((a, b) => (a + b))
const Sumexp = NewOperation((...arr) => arr.reduce((acc, val) => acc + Math.exp(val), 0), "sumexp",
    function (...args) {
        let ans = ZERO;
        let len = args.length;
        for (let i = 0; i < len / 2; i++) {
            ans = new Add(ans, new Multiply(new Sumexp(args[i]), args[i + len / 2]));
        }
        return ans;
    }
);

const Softmax = NewOperation((...arr) => Math.exp(arr[0]) / arr.reduce((acc, val) => acc + Math.exp(val), 0), "softmax",
    function(...args) {
        let ans = ZERO;
        let len = args.length;
        for (let i = 0; i < len / 2; i++) {
            ans = new Add(ans, new Multiply(new Sumexp(args[i]), args[i + len / 2]));
        }
        let exp = new Sumexp(...args.slice(0, len / 2));
        return new Divide(new Subtract(new Multiply(new Multiply(new Sumexp(args[0]), args[len / 2]), exp),
                                        new Multiply(ans, new Sumexp(args[0]))), new Multiply(exp, exp));
    }
);

let operations = {
    "+" : {Constructor: Add, len: 2}, "-" : {Constructor: Subtract, len: 2},
    "*" : {Constructor: Multiply, len: 2}, "/" : {Constructor: Divide, len: 2},
    "negate" : {Constructor: Negate, len: 1},
    "atan" : {Constructor: ArcTan, len: 1}, "atan2" : {Constructor: ArcTan2, len: 2},
    "sumexp" : {Constructor: Sumexp, len: 0}, "softmax" : {Constructor: Softmax, len: 0}
};

let opers = "";
for (let o in operations) {
    opers += o + ", ";
}

// **************************** //
let parse = function(s) {
    st = [];
    let tokens = s.split(" ").filter(w => w.length > 0);
    for (const token of tokens) {
        if (token in operations) {
            let args = st.splice(-operations[token].len);
            st.push(operations[token].Constructor(...args));
        } else if (token in variables) {
            st.push(new Variable(token));
        } else {
            st.push(new Const(Number(token)));
        }
    }
    return st.pop();
};

function ParseException(message) {
    this.message = message;
}

ParseException.prototype = Error.prototype;
ParseException.prototype.name = "ParseException";
ParseException.prototype.constructor = ParseException;

const Parser = (mode) => {
    let len;
    let ptr = 0;
    let previousPtr = 0;
    let source;
    let index = [];

    const error = (expected, found, pos) => {
        let place = "";
        if (pos < 0) {pos = 0}
        for (let i = 0; i < pos; i++) {
            place += " ";
        }
        place += "^";

        throw new ParseException(pos + ": expected " + expected + " found " + found + " in case:\n" + source + '\n' + place + '\n');
    };

    function expect(check, expected, found = source.charAt(ptr)) {
        let next = readToken();
        if (!check(next)) {
            error(expected, "\'" + found + "\'", ptr);
        }
    }

    const Mode = {
        "prefix" : prefix,
        "postfix" : postfix
    };

    let skipWhitespace = () => {
        while (ptr < len && /\s/.test(source.charAt(ptr))) { ptr++; }
    };

    function isVariable(a) {
        return (a in variables);
    }
    function isOperator(a) {
        return (a in operations)
    }
    function isSymbol(a = source.charAt(ptr)) {
        return !(/[\s()]/).test(a);
    }
    function isNumber(a) {
        return (!isNaN(+a));
    }

    function readToken() {
        skipWhitespace();
        previousPtr = ptr;

        let result = "";
        while (ptr < len && isSymbol()) {
            result += source.charAt(ptr++);
        }
        if (ptr < len && result.length === 0) {
            result += source.charAt(ptr++);
        }
        return result;
    }

    function readArguments(cnt = 0) {
        let args = [];
        while (ptr < len && (cnt === 0 || cnt-- > 0)) {
            let token = readToken();
            if (token === "(") {
                ptr = previousPtr;
                args.push(readExpression());
                continue;
            }
            if (token === ")") {
                ptr = previousPtr;
                return args;
            }
            if (isNumber(token)) {
                args.push(new Const(Number(token)));
                continue;
            }
            if (isVariable(token)) {
                args.push(new Variable(token));
                continue;
            }
            if (mode === "postfix" && isOperator(token)) {
                ptr = previousPtr;
                break;
            }
            if (isOperator(token)) {
                error("argument", "operator: \'" + token + "\'", previousPtr - 1);
            }
            if (isSymbol(token)) {
                error("variable name", "\'" + token + "\'", previousPtr - 1);
            }
        }
        return args;
    }

    function readOperator() {
        let operator = readToken();
        if (!isOperator(operator)) {
            error("operator: " + opers, "\'" + source.charAt(previousPtr) + "\'", previousPtr);
        }
        return operator;
    }

    function readExpression() {
        expect(a => a === "(", "opening bracket '('");

        index.push(previousPtr);
        let cur = Mode[mode]();
        let [operator, cnt, args] = cur;

        if (args.length !== cnt && cnt > 0) {
            let pos = previousPtr;
            if (mode === "prefix" && args.length > cnt) pos--;
            error(cnt + " arguments", args.length + " arguments", pos);
        }

        expect((a) => {
            return (a === ")");
        }, "closing bracket ')'");
        if (index.length === 0) {
            error("opening bracket for this bracket", "nothing", previousPtr);
        }
        index.pop();
        return new operations[operator].Constructor(...args);
    }

    function prefix() {
        let operator = readOperator();
        let cnt = (operations[operator].len || 0);
        let args = readArguments(cnt);
        return [operator, cnt, args];
    }

    function postfix() {
        let args = readArguments();
        let operator = readOperator();
        let cnt = (operations[operator].len || 0);
        return [operator, cnt, args];
    }

    function parse(expression) {
        index = [];
        ptr = 0;
        previousPtr = 0;
        source = expression.trim();
        len = source.length;

        if (len === 0) {
            error("prefix expression", "nothing", ptr);
        }

        let token = readToken();
        skipWhitespace();
        if (token !== '(') {
            if (isNumber(token) && ptr === len) {
                return new Const(Number(token));
            }
            if (isVariable(token) && ptr === len) {
                return new Variable(token);
            }
            if (ptr !== len) {
                error("end of file", "\'" + source.charAt(ptr) + "\'", ptr);
            } else {
                error("number or variable", token, previousPtr);
            }
        }

        ptr = previousPtr;
        let result = readExpression();
        if (ptr !== len) {
            skipWhitespace();
            error("end of file", "\'" + source.charAt(ptr) + "\'", ptr);
        }
        if (index.length !== 0) {
            error("pair bracket bracket at position " + index[0], "nothing", index[0]);
        }
        return result;
    }
    return parse;
};

const parsePrefix = Parser("prefix");
const parsePostfix = Parser("postfix");
