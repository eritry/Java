let variables = {
    "x" : 0, "y" : 1, "z" : 2
};

let constants = {
    "pi" : Math.PI, "e" : Math.E,
    "one" : 1, "two" : 2
};

function operation(f) {
    return (...op) => (...args) => f(...op.map((x) => (x(...args))))
}

let variable = (name) => {
    const id = variables[name];
    return (...args) => args[id];
};

let cnst = function(value) {
    return () => (value);
};

for (let c in constants) {
    this[c] = cnst(constants[c]);
}

let add = operation((a, b) => (a + b));

let subtract = operation((a, b) => (a - b));

let multiply = operation((a, b) => (a * b));

let divide = operation((a, b) => (a / b));

let avg5 = (operation((...args) => (args.reduce((a, b) => (a + b))) / args.length));

const abs = operation(Math.abs);

let negate = operation((a) => (-a));

let med3 = operation((...args) =>  (args.sort((a, b) => (a - b))[Math.floor(args.length / 2)]));

let operations = {
    "+" : add, "-" : subtract, "*" : multiply,
    "/" : divide, "abs" : abs, "med3" : med3,
    "avg5" : avg5, "negate" : negate
};

let len = {
    "+": 2, "-": 2, "*": 2, "/": 2,
    "abs": 1, "med3" : 3, "avg5" : 5,
    "negate" : 1
};

const vars  = {};
for (let v in variables) {
    vars[v] = variable(v);
}

let parse = function(s) {
    let st = [];
    let tokens = s.split(" ").filter(function(w) { return w.length > 0;});
    for (let i = 0; i < tokens.length; i++) {
        let token = tokens[i];
        if (token in operations) {
            let args = st.splice(-len[token]);
            st.push(operations[token](...args));
        } else if (token in variables) {
            st.push(vars[token]);
        } else if (token in constants) {
            st.push(cnst(constants[token]));
        } else {
            st.push(cnst(Number(token)));
        }
    }
    return st.pop();
};